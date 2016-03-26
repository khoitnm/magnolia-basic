package com.projectdemo.basic.dialog.action;

import info.magnolia.ui.admincentral.dialog.action.SaveDialogActionDefinition;

/**
 * Created by khoi.tran on 3/24/16.
 */
public class SaveNodeWithRelationDialogActionDefinition extends SaveDialogActionDefinition {
    private String relationProperty;
    private String relationWorkspace;
    private String relationNodeType;

    public SaveNodeWithRelationDialogActionDefinition() {
        setImplementationClass(SaveNodeWithRelationDialogAction.class);
    }

    public String getRelationProperty() {
        return relationProperty;
    }

    public void setRelationProperty(String relationProperty) {
        this.relationProperty = relationProperty;
    }

    public String getRelationWorkspace() {
        return relationWorkspace;
    }

    public void setRelationWorkspace(String relationWorkspace) {
        this.relationWorkspace = relationWorkspace;
    }

    public String getRelationNodeType() {
        return relationNodeType;
    }

    public void setRelationNodeType(String relationNodeType) {
        this.relationNodeType = relationNodeType;
    }
}
