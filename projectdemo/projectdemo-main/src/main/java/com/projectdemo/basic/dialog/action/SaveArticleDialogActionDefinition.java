package com.projectdemo.basic.dialog.action;

import info.magnolia.ui.admincentral.dialog.action.SaveDialogAction;
import info.magnolia.ui.admincentral.dialog.action.SaveDialogActionDefinition;

/**
 * Created by khoi.tran on 3/24/16.
 */
public class SaveArticleDialogActionDefinition extends SaveDialogActionDefinition {

    public SaveArticleDialogActionDefinition() {
        setImplementationClass(SaveArticleDialogAction.class);
    }
}
