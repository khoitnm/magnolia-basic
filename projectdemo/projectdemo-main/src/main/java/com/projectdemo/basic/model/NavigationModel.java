package com.projectdemo.basic.model;

import info.magnolia.jcr.predicate.NodeTypePredicate;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * @author dat.dang (2016-Mar-18)
 */
public class NavigationModel extends RenderingModelImpl<AreaDefinition> {
    private static final Logger LOGGER = Logger.getLogger(NavigationModel.class);

    @Inject
    private TemplatingFunctions _templatingFunctions;


    private static final String CURRENT_ACTIVE_CSS_CLASS = "active";
    private static final String CHILD_ACTIVE_CSS_CLASS = "child-active";
    private static final String DEMO_ABOUT_TEMPLATE_SUBTYPE = "demo-about";

    @Inject
    public NavigationModel(Node content, AreaDefinition definition, RenderingModel<?> parent) {
        super(content, definition, parent);
    }

    public Locale getLocale(String language) {
        return LocaleUtils.toLocale(language);
    }

    public List<NavigationItem> getChildPages() {
        try {
            int depth = content.getDepth();

            if (depth == 2) {
                return getChildPages(content);
            } else if (depth > 2) {
                Node parent = content;
                while (depth > 2) {
                    parent = parent.getParent();
                    depth--;
                }
                return getChildPages(parent);
            }
        } catch (RepositoryException e) {
            LOGGER.error("Could not retrieve pages for navigation.", e);
        }
        return Collections.emptyList();
    }

    private List<NavigationItem> getChildPages(Node parent) throws RepositoryException {
        final List<NavigationItem> navigationItems = new LinkedList();

        final Iterator<Node> nodeIterator = NodeUtil.getNodes(parent, NodeTypes.Page.NAME).iterator();
        while (nodeIterator.hasNext()) {
            final Node pageNode = nodeIterator.next();
            final boolean hide = PropertyUtil.getBoolean(pageNode, NavigationItem.PROPERTY_NAME_HIDE_PAGE, false);
            if (!hide) {
                try {
                    final NavigationItem navigationItem = getNavigationItem(pageNode);
                    navigationItems.add(navigationItem);
                } catch (RepositoryException e) {
                    LOGGER.error("Could not create page object from node.", e);
                }
            }
        }
        return navigationItems;
    }

    private NavigationItem getNavigationItem(Node node) throws RepositoryException {
        final NavigationItem navigationItem = new NavigationItem();
        String title = PropertyUtil.getString(node, NavigationItem.PROPERTY_NAME_NAVIGATION_TITLE);
        if (title == null) {
            title = PropertyUtil.getString(node, NavigationItem.PROPERTY_NAME_TITLE, node.getName());
        }

        navigationItem.setName(title);
        if (isActive(node)) {
            navigationItem.setCssClass(CURRENT_ACTIVE_CSS_CLASS);
        } else if (isChildActive(node)) {
            navigationItem.setCssClass(CHILD_ACTIVE_CSS_CLASS);
        }
        navigationItem.setLink(_templatingFunctions.link(node));
        return navigationItem;
    }

    private boolean isActive(Node pageNode) throws RepositoryException {
        return pageNode.getPath().equals(content.getPath());
    }

    private boolean isChildActive(Node node) throws RepositoryException {
        final Iterator<Node> nodeIterator = NodeUtil.collectAllChildren(node, new NodeTypePredicate(NodeTypes.Page.NAME)).iterator();
        while (nodeIterator.hasNext()) {
            final Node child = nodeIterator.next();
            if (content.getPath().equals(child.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Simple Pojo for navigation items.
     */
    public class NavigationItem {

        public static final String PROPERTY_NAME_HIDE_PAGE = "hideInNav";
        public static final String PROPERTY_NAME_TITLE = "title";
        public static final String PROPERTY_NAME_NAVIGATION_TITLE = "navigationTitle";

        private String name;
        private String link;
        private String cssClass;

        public String getName() {
            return name;
        }

        public String getLink() {
            return link;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setCssClass(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }
    }
}
