package com.projectdemo.basic.dialog.action;

import com.projectdemo.basic.common.constant.JcrConstants;
import com.projectdemo.basic.common.util.JcrNodeHelper;
import com.projectdemo.basic.common.util.JcrPropertyHelper;
import com.projectdemo.basic.common.util.JcrQueryHelper;
import com.vaadin.data.Item;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.ui.admincentral.dialog.action.SaveDialogAction;
import info.magnolia.ui.api.action.ActionExecutionException;
import info.magnolia.ui.form.EditorCallback;
import info.magnolia.ui.form.EditorValidator;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by khoi.tran on 3/24/16.
 */
public class SaveArticleDialogAction extends SaveDialogAction<SaveArticleDialogActionDefinition> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveDialogAction.class);
    @Inject
    private JcrQueryHelper jcrQueryHelper;

    public SaveArticleDialogAction(SaveArticleDialogActionDefinition definition, Item item, EditorValidator validator, EditorCallback callback) {
        super(definition, item, validator, callback);
    }

    @Override
    public void execute() throws ActionExecutionException {
        super.execute();
        //Save related pages
        if (!(item instanceof JcrNodeAdapter)) {
            LOGGER.debug("Cannot save related pages because item is not an instance of JcrNodeAdapter.");
        }
        JcrNodeAdapter jcrNodeAdapter = (JcrNodeAdapter) item;
        if (!jcrNodeAdapter.isNode()) {
            LOGGER.debug("Cannot save related pages because jcrNodeAdapter is not a Node.");
        }
        Node currentPage = jcrNodeAdapter.getJcrItem();
        List<String> relatedPagesIdsToCurrentPage = JcrPropertyHelper.tryGetListStrings(currentPage, "relatedPages");
        List<Node> relatedPagesToCurrentPage = jcrQueryHelper.searchNodesByUuids(JcrConstants.WORKSPACE_PAGES, JcrConstants.NODETYPE_PAGES, relatedPagesIdsToCurrentPage);

        List<Node> relatedPagesGroup = new ArrayList<>();
        relatedPagesGroup.add(currentPage);
        relatedPagesGroup.addAll(relatedPagesToCurrentPage);

        saveRelatedPages(relatedPagesGroup);
        LOGGER.info("prop:" + relatedPagesIdsToCurrentPage);
        LOGGER.info("Pages:" + relatedPagesToCurrentPage.stream().map(page -> PropertyUtil.getString(page, "title")).collect(Collectors.toList()));
    }

    private void saveRelatedPages(List<Node> relatedPagesGroups) {
        for (Node page : relatedPagesGroups) {
            List<Node> relatedPages = getOtherPages(relatedPagesGroups, page);
        }
    }

    private List<Node> getOtherPages(List<Node> pages, Node page) {
        return null;

    }

    private void addRelatedPage(Node sourceNode, Node relatedNode) {

    }
}
