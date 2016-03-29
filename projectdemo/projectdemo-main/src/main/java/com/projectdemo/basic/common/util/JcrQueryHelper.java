package com.projectdemo.basic.common.util;


import com.projectdemo.basic.common.constant.JcrConstants;
import com.projectdemo.basic.common.exception.ProjectJcrException;
import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.cms.util.QueryUtil;
import info.magnolia.context.MgnlContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper class using IoC, not static methods.
 * <p>
 * This class is the wrapper of default {@link info.magnolia.cms.util.QueryUtil}.
 * At this moment, it will help us to use Date param more easily.
 * For example:<br/>
 * {@code searchNodes("qnlVideoWorkspace", "select * from ?0 where [jcr:created] > ?1", new Object[] {"[mgnl:content]", new Date()})}.
 *
 * @author khoi.tran (2015-Jul-10)
 */
public class JcrQueryHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JcrQueryHelper.class);
    @Inject
    private ServerConfiguration serverConfiguration;

    public static final String PARAM_PREFIX_CHAR = "?";

    public static String getOrderByNewestTime() {
        return " order by [jcr:created] desc ";
    }

    public String createCriteriaIsPublished() {
        String criteria = StringUtils.EMPTY;
        if (serverConfiguration.isAdmin()) {
            criteria = " [mgnl:activationStatus] = true ";
        }
        return criteria;
    }

    /**
     * Get node by name where name is persisted as String.(It just suitable for Category content apps).
     *
     * @param nodeType  node type.
     * @param workspace workspace of content app.
     * @param nodeName  node name value.
     * @return available node or null.
     */
    public Node searchPublishedNodeByName(final String nodeType, final String workspace, final String nodeName) {
        final String statement = String.format("select name from [%s] where (name = '%s') AND (%s)", nodeType, nodeName, getActivationCriteria(StringUtils.EMPTY));
        return searchNode(workspace, statement);
    }

    public List<Node> searchNodesByUuids(String workspace, String nodeType, List<String> uuids) {
        List<Node> result = new ArrayList<>();
        if (uuids == null || uuids.isEmpty()) {
            return result;
        }
        StringBuilder jcr2Query = new StringBuilder("SELECT * FROM [").append(nodeType).append("] WHERE ");
        String uuidsCriteria = uuids.stream()
            .map(uuid -> "[jcr:uuid] = '" + uuid + "'")
            .collect(Collectors.joining(" OR ", "(", ")"));
        jcr2Query.append(uuidsCriteria);
        return searchNodes(workspace, jcr2Query.toString());
    }

    /**
     * Get node by name where name is persisted as Long.
     *
     * @param nodeType  node type.
     * @param workspace workspace of content app.
     * @param nodeName  node name value.
     * @return available node or null.
     */
    public Node searchNodeByNodeName(final String nodeType, final String workspace, final long nodeName, boolean isNeedCondition) {
        String statement = String.format("select * from [%s] where (name() = '%s') AND (%s)", nodeType, nodeName, getActivationCriteria(StringUtils.EMPTY));
        if (!isNeedCondition) {
            statement = String.format("select * from [%s] where (name() = '%s')", nodeType, nodeName);
        }
        return searchNode(workspace, statement);
    }


    public String getActivationCriteria(final String publishStatus) {
        String criteria = "[mgnl:created] is not null";
        if (serverConfiguration.isAdmin()) {
            if (StringUtils.isEmpty(publishStatus) || StringUtils.equalsIgnoreCase(JcrConstants.STATUS_PUBLISHED, publishStatus)) {
                criteria = "[mgnl:activationStatus] = 'true'";
            } else if (StringUtils.equalsIgnoreCase(JcrConstants.STATUS_UNPUBLISHED, publishStatus)) {
                criteria = "[mgnl:activationStatus] is null or [mgnl:activationStatus] = 'false'";
            }
        }
        return criteria;
    }

    public String andCriteriaIsPublished() {
        String result = createCriteriaIsPublished();
        if (StringUtils.isNotBlank(result)) {
            result = " AND " + result;
        }
        return result;
    }

    public static List<Node> searchRandomNodes(String workspace, String jcrSql2Query, int numNodes) {
        List<Node> result = new ArrayList<>();

        List<Node> nodeList = searchNodes(workspace, jcrSql2Query);

        if (nodeList != null) {
            if (nodeList.size() > numNodes) {
                Random random = new Random();
                while (result.size() != numNodes) {
                    int i = random.nextInt(nodeList.size());
                    while (result.contains(nodeList.get(i))) {
                        i = random.nextInt(nodeList.size());
                    }
                    result.add(nodeList.get(i));
                }
            } else {
                Collections.shuffle(nodeList);
                result = nodeList;
            }
        }

        return result;
    }

    public static long countNodes(String workspace, String jcrSql2Query) {
        long result = 0;
        try {
            LOGGER.trace("Executing query:\n {}", jcrSql2Query);
            Query query = MgnlContext.getJCRSession(workspace).getWorkspace().getQueryManager().createQuery(jcrSql2Query, Query.JCR_SQL2);
            QueryResult queryResult = query.execute();
            result = queryResult.getRows().getSize();
        } catch (InvalidQueryException ex) {
            LOGGER.error("The query is invalid: {}", jcrSql2Query, ex);
            throw new ProjectJcrException(String.format("Cannot search nodes in workspace '%s'. Invalid query, please view logInfo for more information.", workspace), ex);
        } catch (NoSuchWorkspaceException ex) {
            LOGGER.error("Cannot run query: {} \nRootcause: there is no workspace {}", jcrSql2Query, workspace, ex);
            //TODO should throw this Exception when we have enough ContentApp.
        } catch (RepositoryException ex) {
            LOGGER.error("Cannot run query: {}", jcrSql2Query, workspace, ex);
            throw new ProjectJcrException(String.format("Cannot search nodes in workspace '%s'. Please view logInfo for more information.", workspace), ex);
        }
        return result;
    }

    public static List<Node> searchNodes(String workspace, String jcrSql2Query, QueryOffset queryOffset) {
        List<Node> result = new ArrayList<>();
        try {
            LOGGER.trace("Executing query:\n {}", jcrSql2Query);
            Query query = MgnlContext.getJCRSession(workspace).getWorkspace().getQueryManager().createQuery(jcrSql2Query, Query.JCR_SQL2);
            if (queryOffset != null) {
                if (queryOffset.getItemOffset() != null) {
                    query.setOffset(queryOffset.getItemOffset());
                }
                if (queryOffset.getLimitSize() != null) {
                    query.setLimit(queryOffset.getLimitSize());
                }
            }
            QueryResult queryResult = query.execute();
            NodeIterator nodeIterator = queryResult.getNodes();
            while (nodeIterator.hasNext()) {
                result.add(nodeIterator.nextNode());
            }
        } catch (InvalidQueryException ex) {
            LOGGER.error("The query is invalid: {}", jcrSql2Query, ex);
            throw new ProjectJcrException(String.format("Cannot search nodes in workspace '%s'. Invalid query, please view logInfo for more information.", workspace), ex);
        } catch (NoSuchWorkspaceException ex) {
            LOGGER.error("Cannot run query: {} \nRootcause: there is no workspace {}", jcrSql2Query, workspace, ex);
            //TODO should throw this Exception when we have enough ContentApp.
        } catch (RepositoryException ex) {
            LOGGER.error("Cannot run query: {}", jcrSql2Query, workspace, ex);
            throw new ProjectJcrException(String.format("Cannot search nodes in workspace '%s'. Please view logInfo for more information.", workspace), ex);
        }
        return result;
    }

    public static List<Node> searchNodes(String workspace, String query) {
        List<Node> result = new ArrayList<>();
        try {
            LOGGER.trace("Executing query:\n {}", query);
            NodeIterator nodeIterator = QueryUtil.search(workspace, query, Query.JCR_SQL2);
            while (nodeIterator.hasNext()) {
                result.add(nodeIterator.nextNode());
            }
        } catch (InvalidQueryException e) {
            LOGGER.error("The query is invalid: {}", query, e);
            throw new ProjectJcrException(String.format("Invalid JCR query on workspace '%s'", workspace), e);
        } catch (NoSuchWorkspaceException e) {
            LOGGER.error("Not found workspace {}, so cannot run query '{}'", workspace, query, e);
            throw new ProjectJcrException(String.format("Cannot run JCR Query because of not existing workspace '%s'", workspace), e);
        } catch (RepositoryException e) {
            LOGGER.error("Unknown error when running JCR query '{}' on workspace '{}'", query, workspace, e);
            throw new ProjectJcrException(String.format("Cannot run JCR Query on workspace '%s'", workspace), e);
        }
        return result;
    }

    public static NodeIterator searchBaseNodes(String workspace, String jcrSql2Query) {
        try {
            LOGGER.trace("Executing query:\n {}", jcrSql2Query);
            final Query query = MgnlContext.getJCRSession(workspace).getWorkspace().getQueryManager().createQuery(jcrSql2Query, Query.JCR_SQL2);
            final QueryResult queryResult = query.execute();
            return queryResult.getNodes();
        } catch (InvalidQueryException ex) {
            LOGGER.error("The query is invalid: {}", jcrSql2Query, ex);
            throw new ProjectJcrException(String.format("Cannot search nodes in workspace '%s'. Invalid query, please view logInfo for more information.", workspace), ex);
        } catch (NoSuchWorkspaceException ex) {
            LOGGER.error("Cannot run query: {} \nRootcause: there is no workspace {}", jcrSql2Query, workspace, ex);
            //TODO should throw this Exception when we have enough ContentApp.
        } catch (RepositoryException ex) {
            LOGGER.error("Cannot run query: {}", jcrSql2Query, workspace, ex);
            throw new ProjectJcrException(String.format("Cannot search nodes in workspace '%s'. Please view logInfo for more information.", workspace), ex);
        }
        return null;
    }


    public static Node searchNode(String workspace, String query) {
        Node result = null;
        List<Node> nodes = searchNodes(workspace, query);
        if (nodes.size() == 1) {
            result = nodes.get(0);
        } else if (nodes.size() > 1) {
            String msg = String.format("The query '%s' return %s nodes which is different from your expectation: only 1 node.", query, nodes.size());
            throw new ProjectJcrException(msg);
        }
        return result;
    }

    public static List<Node> searchNodes(String workspace, String query, Object[] params) {
        String parameterizeQuery = putParamsToQuery(query, params);
        return searchNodes(workspace, parameterizeQuery);
    }

    /**
     * @param query  query pattern.
     * @param params the list of parameters.
     * @return query with formatted parameters.
     */
    public static String putParamsToQuery(String query, Object... params) {
        String parameterizeQuery = query;
        int lastIndex = params.length - 1;
        for (int i = lastIndex; i >= 0; i--) {
            Object param = params[i];
            String formattedParam = formatParam(param);
            parameterizeQuery = putFormattedParamToQuery(parameterizeQuery, i, formattedParam);
        }
        return parameterizeQuery;
    }

    /**
     * @param jcrMethod       example: ISDESCENDANTNODE(['?0'])
     * @param values          the list of values will be put into methods.
     * @param logicalOperator the logical operator.
     * @return the criteria with And/Or conditions.
     * <pre>
     * Example:
     * jcrMethod = ISDESCENDANTNODE(['?0'])
     * values = ['A', 'B']
     * logicalOperator = AND
     * Result:
     * ISDESCENDANTNODE(['A']) AND ISDESCENDANTNODE(['B'])
     * </pre>.
     */
    public static String createCriteriaWithLogicalOperations(String jcrMethod, List<String> values, QnlLogicalOperator logicalOperator) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        StringBuilder criteria = new StringBuilder("(");
        for (String value : values) {
            if (criteria.length() > 1) {
                criteria.append(logicalOperator.getQueryOperator());
            }
            criteria.append(JcrQueryHelper.putParamsToQuery(jcrMethod, value));
        }
        criteria.append(")");
        return criteria.toString();
    }


    public static void andCriteria(StringBuilder originalCriteria, String newCriteria) {
        if (StringUtils.isNotBlank(newCriteria)) {
            if (StringUtils.isNotBlank(originalCriteria)) {
                originalCriteria.append(" AND ");
            }
            originalCriteria.append(newCriteria);
        }
    }


    private static String putFormattedParamToQuery(String query, int paramIndex, String formatedParam) {
        return query.replace(PARAM_PREFIX_CHAR + paramIndex, formatedParam);
    }

    /**
     * @param param value of query parameter.
     * @return formatted of param.
     * <pre>
     * Example 1:
     * param = new Date()
     * return "cast('2015-07-10T16:39:50.758+07:00' as date)"
     *
     * Example 2:
     * param = "256"
     * return "256"
     * </pre>
     */
    private static String formatParam(Object param) {
        String result = null;
        if (param instanceof String) {
            result = (String) param;
        } else if (param instanceof Date) {
            result = formatDateParam((Date) param);
        } else {
            result = param.toString();
        }
        return result;
    }

    /**
     * @param date date param which will be formatted.
     * @return return formatted date which will be used for JCR query.
     * <strong>Example:</strong>
     * <code>
     * Input: new Date()
     * Output: 'cast('2015-07-10T16:39:50.758+07:00' as date)'
     * </code>
     * We use this format because it's used by JCR Query.
     */
    private static String formatDateParam(Date date) {
        String result = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String formattedDate = format.format(date);
        formattedDate = formattedDate.substring(0, 26) + ":" + formattedDate.substring(26);
        result = String.format("CAST('%s' AS DATE)", formattedDate);
        return result;
    }

    /**
     * The logical operator.
     *
     * @author khoi.tran
     */
    public enum QnlLogicalOperator {
        AND(" and "), OR(" or ");
        private final String queryOperator;

        QnlLogicalOperator(String queryOperator) {
            this.queryOperator = queryOperator;
        }

        public String getQueryOperator() {
            return queryOperator;
        }
    }


    //CHECKSTYLE:OFF


}
