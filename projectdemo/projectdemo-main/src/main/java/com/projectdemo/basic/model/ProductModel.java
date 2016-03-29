package com.projectdemo.basic.model;

import com.projectdemo.basic.common.constant.ProductConstants;
import info.magnolia.jcr.util.ContentMap;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.RenderableDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * @author dat.dang (2016-Mar-22)
 */
public class ProductModel<RD extends RenderableDefinition> extends RenderingModelImpl<RD> {

    @Inject
    private TemplatingFunctions _templatingFunctions;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductModel.class);

    @Inject
    public ProductModel(Node content, RD definition, RenderingModel<?> parent) {
        super(content, definition, parent);
    }

    public ContentMap getProductInfo(String productUUID) {
        if (StringUtils.isNotEmpty(productUUID)) {
            try {
                Node nodeProduct = NodeUtil.getNodeByIdentifier(ProductConstants.APP_PRODUCT, productUUID);

                return nodeProduct == null ? null : _templatingFunctions.asContentMap(nodeProduct);
            } catch (RepositoryException e) {
                LOGGER.error("Cannot get node by product UUID");
            }
        }
        return null;
    }
}
