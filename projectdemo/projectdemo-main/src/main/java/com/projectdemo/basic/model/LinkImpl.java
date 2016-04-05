package com.projectdemo.basic.model;

import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.templating.functions.TemplatingFunctions;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.jcr.Node;

/**
 * @author dat.dang (2016-Apr-01)
 */
public class LinkImpl implements Link {
    private Node node;
    private TemplatingFunctions templatingFunctions;

    @Inject
    public LinkImpl(Node node, TemplatingFunctions templatingFunctions) {
        this.node = node;
        this.templatingFunctions = templatingFunctions;
    }

    @Override
    public String getTitle(){
        String title = PropertyUtil.getString(node, "title");
        String name = NodeUtil.getName(node);
        return StringUtils.defaultIfEmpty(title, name);
    }

    @Override
    public String getNavigationTitle(){
        String navigationTitle = PropertyUtil.getString(node, "navigationTitle");
        String title = PropertyUtil.getString(node, "title");
        String name = NodeUtil.getName(node);
        return StringUtils.defaultIfEmpty(StringUtils.defaultIfEmpty(navigationTitle, title), name);
    }

    @Override
    public String getHref(){
        return templatingFunctions.link(node);
    }
}
