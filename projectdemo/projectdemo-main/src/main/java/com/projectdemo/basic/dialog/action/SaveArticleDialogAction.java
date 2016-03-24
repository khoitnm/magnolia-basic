package com.projectdemo.basic.dialog.action;

import com.vaadin.data.Item;
import info.magnolia.ui.admincentral.dialog.action.SaveDialogAction;
import info.magnolia.ui.form.EditorCallback;
import info.magnolia.ui.form.EditorValidator;

/**
 * Created by khoi.tran on 3/24/16.
 */
public class SaveArticleDialogAction extends SaveDialogAction<SaveArticleDialogActionDefinition> {

    public SaveArticleDialogAction(SaveArticleDialogActionDefinition definition, Item item, EditorValidator validator, EditorCallback callback) {
        super(definition, item, validator, callback);
    }
}
