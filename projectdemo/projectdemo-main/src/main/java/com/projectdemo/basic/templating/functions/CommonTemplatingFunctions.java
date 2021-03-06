package com.projectdemo.basic.templating.functions;

import info.magnolia.cms.i18n.I18nContentSupport;
import info.magnolia.context.MgnlContext;
import info.magnolia.dam.api.Asset;
import info.magnolia.dam.api.AssetProviderRegistry;
import info.magnolia.dam.jcr.DamConstants;
import info.magnolia.dam.jcr.JcrAsset;
import info.magnolia.dam.jcr.JcrAssetProvider;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.jcr.util.ContentMap;
import info.magnolia.rendering.template.assignment.TemplateDefinitionAssignment;
import info.magnolia.templating.functions.TemplatingFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static info.magnolia.jcr.util.NodeUtil.getNodeByIdentifier;
import static info.magnolia.repository.RepositoryConstants.WEBSITE;
import static javax.jcr.query.Query.JCR_SQL2;
import static org.apache.commons.lang.StringUtils.EMPTY;

/**
 * Created by khoi.tran on 3/22/16.
 */
public class CommonTemplatingFunctions {
    public static final String FUNCTIONS_NAME = "commonfn";
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonTemplatingFunctions.class);


    private DamTemplatingFunctions damTemplatingFunctions;
    private TemplatingFunctions templatingFunctions;
    private AssetProviderRegistry assetProviderRegistry;
    private TemplateDefinitionAssignment templateDefinitionAssignment;
    private Provider<I18nContentSupport> i18nContentSupport;

    //CHECKSTYLE:OFF
    @Inject
    public CommonTemplatingFunctions(Provider<I18nContentSupport> i18nContentSupport, TemplatingFunctions templatingFunctions, AssetProviderRegistry assetProviderRegistry, TemplateDefinitionAssignment templateDefinitionAssignment, DamTemplatingFunctions damTemplatingFunctions) {
        this.templatingFunctions = templatingFunctions;
        this.assetProviderRegistry = assetProviderRegistry;
        this.templateDefinitionAssignment = templateDefinitionAssignment;
        this.damTemplatingFunctions = damTemplatingFunctions;
        this.i18nContentSupport = i18nContentSupport;
    }

    //CHECKSTYLE:ON
    public Locale currentLocale() {
        return i18nContentSupport.get().getLocale();
    }

    public Locale defaultLocale() {
        return i18nContentSupport.get().getDefaultLocale();
    }

    public String propertyNameByLocale(String propertyName) {
        String propertyByLocaleSuffix = "";
        Locale currentLocale = currentLocale();
        Locale defaultLocale = defaultLocale();
        if (!currentLocale.equals(defaultLocale)) {
            propertyByLocaleSuffix = "_" + currentLocale.toString();
        }
        return propertyName + propertyByLocaleSuffix;
    }

    /**
     * @param contentMap       the input object.
     * @param basePropertyName the basic property name which is not related to locale.<br/>
     *                         For example: "productDescription", don't use locale suffix like "productDescription_de" or "productDescription_fr".
     * @return the data of property in corresponding locale.
     * @deprecated cannot get property from contentMap.
     */
    public Object propertyByLocale(ContentMap contentMap, String basePropertyName) {
        String propertyNameByLocale = propertyNameByLocale(basePropertyName);
        return contentMap.get(propertyNameByLocale);
    }

    public Asset getAsset(ContentMap assetContent) {
        return getAsset(assetContent.getJCRNode());
    }

    public Asset getAsset(Node assetNode) {
        JcrAssetProvider provider = (JcrAssetProvider) assetProviderRegistry.getProviderById(DamConstants.DEFAULT_JCR_PROVIDER_ID);
        return new JcrAsset(provider, assetNode);
    }

    public String getNodePathByIdentifier(String uuid) {
        return getNodePathByIdentifier(uuid, WEBSITE);
    }

    public String getNodePathByIdentifier(String uuid, String workspace) {
        String path = EMPTY;
        try {
            Node file = getNodeByIdentifier(workspace, uuid);
            path = file.getPath();
        } catch (RepositoryException e) {
            LOGGER.warn("Can't get node path by identifier" + e.getMessage(), e);
        }
        return path;
    }

    public Query createQuery(final String queryString, String workspace) {
        Query query = null;
        try {
            final Session jcrSession = MgnlContext.getJCRSession(workspace);
            final QueryManager queryManager = jcrSession.getWorkspace().getQueryManager();
            query = queryManager.createQuery(queryString, JCR_SQL2);
        } catch (RepositoryException e) {
            LOGGER.error("Can't create query '" + queryString + "'.", e);
        }
        return query;
    }

    public List<Node> search(Query query) {
        List<Node> itemsList = new LinkedList<>();
        NodeIterator iterator = null;
        try {
            final QueryResult result = query.execute();
            iterator = result.getNodes();
        } catch (RepositoryException e) {
            LOGGER.error("Can't get entries.", e);
        }
        if (iterator != null && iterator.getSize() > 0) {
            while (iterator.hasNext()) {
                itemsList.add(iterator.nextNode());
            }
        }
        return itemsList;
    }
}
