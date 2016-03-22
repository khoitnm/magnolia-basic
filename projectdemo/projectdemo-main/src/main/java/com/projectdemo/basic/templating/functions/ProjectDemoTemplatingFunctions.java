package com.projectdemo.basic.templating.functions;

import info.magnolia.dam.api.Asset;
import info.magnolia.dam.jcr.JcrAsset;
import info.magnolia.dam.jcr.JcrAssetProvider;
import info.magnolia.jcr.util.ContentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import static info.magnolia.dam.jcr.DamConstants.DEFAULT_JCR_PROVIDER_ID;
import static info.magnolia.jcr.util.NodeUtil.getNodeByIdentifier;
import static info.magnolia.repository.RepositoryConstants.WEBSITE;
import static org.apache.commons.lang.StringUtils.EMPTY;

/**
 * Created by khoi.tran on 3/22/16.
 */
public class ProjectDemoTemplatingFunctions {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDemoTemplatingFunctions.class);

    public Asset getAsset(ContentMap assetContent) {
        return getAsset(assetContent.getJCRNode());
    }

    public Asset getAsset(Node assetNode) {
        JcrAssetProvider provider = (JcrAssetProvider) _assetProviderRegistry.getProviderById(DEFAULT_JCR_PROVIDER_ID);
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
}
