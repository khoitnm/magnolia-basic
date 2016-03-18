package com.projectdemo.basic.model;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.AreaDefinition;

import javax.inject.Inject;
import javax.jcr.Node;

/**
 * @author dat.dang (2016-Mar-18)
 */
public class NavigationModel extends RenderingModelImpl<AreaDefinition> {
    @Inject
    public NavigationModel(Node content, AreaDefinition definition, RenderingModel<?> parent) {
        super(content, definition, parent);
    }
}
