package com.projectdemo.basic.dialog.action;

import com.projectdemo.basic.common.constant.JcrConstants;
import com.projectdemo.basic.common.util.JcrRelatedNodesHelper;
import com.vaadin.data.Item;
import info.magnolia.ui.admincentral.dialog.action.SaveDialogAction;
import info.magnolia.ui.api.action.ActionExecutionException;
import info.magnolia.ui.form.EditorCallback;
import info.magnolia.ui.form.EditorValidator;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.RepositoryException;

/**
 * @author khoi.tran on 3/24/16.
 */
public class SaveNodeWithRelationDialogAction extends SaveDialogAction<SaveNodeWithRelationDialogActionDefinition> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveDialogAction.class);
    private static final String PROP_RELATED_PAGES = "relatedPages";
    private static final String WORKSPACE = JcrConstants.WORKSPACE_PAGES;
    private static final String NODETYPE = JcrConstants.NODETYPE_PAGES;

    @Inject
    private JcrRelatedNodesHelper jcrRelatedNodesHelper;

    private SaveNodeWithRelationDialogActionDefinition definition;

    public SaveNodeWithRelationDialogAction(SaveNodeWithRelationDialogActionDefinition definition, Item item, EditorValidator validator, EditorCallback callback) {
        super(definition, item, validator, callback);
        this.definition = definition;
    }

    @Override
    public void execute() throws ActionExecutionException {
        try {
            this.validator.showValidation(true);
            if (this.validator.isValid()) {
                if (!(item instanceof JcrNodeAdapter)) {
                    LOGGER.warn("Cannot save related pages because item is not an instance of JcrNodeAdapter.");
                    return;
                }
                JcrNodeAdapter jcrNodeAdapter = (JcrNodeAdapter) item;
                jcrRelatedNodesHelper.saveRelatedNodesGroup(jcrNodeAdapter, definition.getRelationProperty(), definition.getRelationWorkspace(), definition.getRelationNodeType());
                super.execute();
            } else {
                LOGGER.info("Validation error(s) occurred. No save performed.");
            }
        } catch (RepositoryException e) {
            throw new ActionExecutionException(e);
        }
    }

}
