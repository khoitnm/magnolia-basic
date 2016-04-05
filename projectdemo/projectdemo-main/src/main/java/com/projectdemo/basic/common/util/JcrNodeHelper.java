package com.projectdemo.basic.common.util;

import com.projectdemo.basic.common.exception.ProjectJcrException;
import com.projectdemo.basic.common.exception.ProjectRuntimeException;
import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.cms.util.QueryUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author khoi.tran
 */
public class JcrNodeHelper {

    public static final Logger LOGGER = LoggerFactory.getLogger(JcrNodeHelper.class);

    @Inject
    private ServerConfiguration serverConfiguration;


    public static void save(Node node) {
        try {
            node.getSession().save();
        } catch (RepositoryException e) {
            String msg = String.format("Cannot save node '%s'. Error: %s", node, e.getMessage());
            throw new ProjectJcrException(msg, e);
        }
    }

    public static boolean checkPermissionEdit(Node node) {
        return checkPermission(node, Session.ACTION_ADD_NODE + "," + Session.ACTION_REMOVE + "," + Session.ACTION_SET_PROPERTY);
    }

    /**
     * @param node   the node.
     * @param action for example: {@link Session#ACTION_ADD_NODE} + "," + {@link Session#ACTION_REMOVE}.
     * @return true if current user has permission to perform action on the node.
     */
    public static boolean checkPermission(Node node, String action) {
        boolean result;
        try {
            getSession(node).checkPermission(getPath(node), action);
            result = true;
        } catch (AccessControlException e) {
            result = false;
        } catch (RepositoryException e) {
            throw new ProjectRuntimeException("Could not access the JCR permissions for the following node identifier " + node, e);
        }
        return result;
    }

    public static Session getSession(final Node node) throws RepositoryException {
        if (node.getSession().isLive()) {
            return node.getSession();
        }
        return MgnlContext.getJCRSession(node.getSession().getWorkspace().getName());
    }

    public static String getPath(final Node node) throws RepositoryException {
        if (node.getSession().isLive()) {
            return node.getPath();
        }
        return getSession(node).getNodeByIdentifier(node.getIdentifier()).getPath();
    }

    public static List<String> getNodesIdentifiers(List<Node> nodes) {
        List<String> result = new ArrayList<>(nodes.size());
        for (Node node : nodes) {
            String jcrUuid = NodeUtil.getNodeIdentifierIfPossible(node);
            result.add(jcrUuid);
        }
        return result;
    }

    public static String getNodeIdentifier(Node node) {
        return NodeUtil.getNodeIdentifierIfPossible(node);
    }

    public int getActivationStatus(final Node node) throws RepositoryException {
        if (!serverConfiguration.isAdmin()) {
            return NodeTypes.Activatable.ACTIVATION_STATUS_ACTIVATED;
        }
        int result;
        if (node.getSession().isLive()) {
            result = NodeTypes.Activatable.getActivationStatus(node);
        } else {
            Node temp = MgnlContext.getJCRSession(node.getSession().getWorkspace().getName()).getNodeByIdentifier(node.getIdentifier());
            result = NodeTypes.Activatable.getActivationStatus(temp);
        }
        return result;
    }

    public boolean isPublishedNode(final Node node) {
        try {
            return (getActivationStatus(node) == NodeTypes.Activatable.ACTIVATION_STATUS_ACTIVATED) || (getActivationStatus(node) == NodeTypes.Activatable.ACTIVATION_STATUS_MODIFIED);
        } catch (RepositoryException e) {
            LOGGER.error("Getting the activation node is failed", e);
        }
        return false;
    }

    public static boolean nodeHasProperty(final Node node, final String property, final String lang) {
        try {
            return node.hasProperty(property) || node.hasProperty(property + lang);
        } catch (RepositoryException e) {
            LOGGER.error("Doesn't exist path", e);
        }
        return false;
    }

    public static int getNodeType(final Node node, final String fieldName, final String lang) {
        try {
            Property property;
            String propertyName = fieldName;
            if (StringUtils.isNotBlank(lang) && !JcrPropertyHelper.isMagnoliaProperty(fieldName)) {
                propertyName = fieldName + lang;
            }
            if (node.hasProperty(propertyName)) {
                property = node.getProperty(propertyName);
                if (property != null) {
                    return property.getType();
                }
            }
        } catch (RepositoryException e) {
            LOGGER.error("Can't get node type", e);
        }
        return -1;
    }

    public static Node getJcrNode(final Node node) throws RepositoryException {
        return getNodeCrossSession(node).getNode(JcrConstants.JCR_CONTENT);
    }

    private static Node getNodeCrossSession(final Node node) throws RepositoryException {
        if (node.getSession().isLive()) {
            return node;
        }
        return MgnlContext.getJCRSession(node.getSession().getWorkspace().getName()).getNodeByIdentifier(node.getIdentifier());
    }

    public static Node getNodeByIdentifier(final String workspace, final String id) {
        try {
            return NodeUtil.getNodeByIdentifier(workspace, id);
        } catch (RepositoryException e) {
            LOGGER.error("cannot get node by uuid", e);
        }
        return null;
    }

    public static List<String> getListUuidInFolder(final Node folder, final String workspace, final String nodeType) throws RepositoryException {
        final List<String> uuids = new ArrayList<>();
        List<Node> nodes = getListNodeInFolder(folder, workspace, nodeType);
        for (Node node : nodes) {
            uuids.add(node.getIdentifier());
        }
        return uuids;
    }

    public static List<Node> getListNodeInFolder(final Node folder, final String workspace, final String nodeType) throws RepositoryException {
        final String query = String.format("select * from [%s] where isdescendantnode('%s')", nodeType, folder.getPath() + "/");
        final NodeIterator iterator = QueryUtil.search(workspace, query);
        final List<Node> nodes = new ArrayList<>();
        if (iterator.hasNext()) {
            final Node firstNode = iterator.nextNode();
            final Iterable<Node> listNodes = NodeUtil.getNodes(firstNode.getParent());
            final Iterator<Node> it = listNodes.iterator();
            while (it.hasNext()) {
                nodes.add(it.next());
            }
        }
        return nodes;
    }

    public static synchronized long getNextMaxAvailableId(final String nodeType, final String workspace, final String fieldName) {
        long maxId = getMaxIdByNodeTypeWorkspace(nodeType, workspace, fieldName);
        return maxId + 1;
    }

    private static long getMaxIdByNodeTypeWorkspace(final String nodeType, final String workspace, final String fieldName) {
        final String statement = String.format("select * from [%s] where [jcr:uuid] is not null order by name desc", nodeType);
        try {
            Query query = MgnlContext.getJCRSession(workspace).getWorkspace().getQueryManager().createQuery(statement, Query.JCR_SQL2);
            query.setLimit(1);

            QueryResult result = query.execute();
            final NodeIterator iterator = result.getNodes();
            if (iterator.hasNext()) {
                final Node node = iterator.nextNode();
                return PropertyUtil.getLong(node, fieldName, 0L);
            }

        } catch (RepositoryException e) {
            LOGGER.error("cannot get maximum id", e);
        }
        return 0L;
    }
}
