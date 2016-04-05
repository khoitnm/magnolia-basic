package com.projectdemo.basic.model;

import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.RenderableDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author dat.dang (2016-Apr-01)
 */
public class BreadcrumbModel<RD extends RenderableDefinition> extends RenderingModelImpl<RD> {
    @Inject
    private TemplatingFunctions _templatingFunctions;

    @Inject
    public BreadcrumbModel(Node content, RD definition, RenderingModel<?> parent, TemplatingFunctions templatingFunctions) {
        super(content, definition, parent);
        templatingFunctions = _templatingFunctions;
    }
//    public Collection<ContentMap> getBreadcrumb() throws RepositoryException {
//        List<Node> ancestors = ancestorsInSite(content, NodeTypes.Page.NAME);
//
//        List<ContentMap> items = new ArrayList<ContentMap>();
//        for(Node current : ancestors){
//            items.add(_templatingFunctions.asContentMap(current));
//        }
//        items.add(_templatingFunctions.asContentMap(content));
//        return items;
//    }

    public Collection<Link> getBreadcrumb() throws RepositoryException {
        List<Node> ancestors = ancestorsInSite(content, NodeTypes.Page.NAME);

        List<Link> items = new ArrayList<Link>();
        for(Node current : ancestors){
            items.add(new LinkImpl(current, _templatingFunctions));
        }
        return items;
    }


    public List<Node> ancestorsInSite(Node content, String nodeTypeName) throws RepositoryException {
        List<Node> allAncestors = _templatingFunctions.ancestors(content, nodeTypeName);
        Node siteRoot = _templatingFunctions.siteRoot(content);

        List<Node> ancestoresInSite = new ArrayList<Node>();
        for (Node current : allAncestors) {
            if (current.getDepth() >= siteRoot.getDepth()) {
                ancestoresInSite.add(current);
            }
        }
        return ancestoresInSite;
    }
}
