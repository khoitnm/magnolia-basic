package com.projectdemo.basic.form.field.definition;

import info.magnolia.ui.form.field.definition.TextFieldDefinition;

/**
 * @author khoa.nguyenkieu.
 */
public class UniqueAssetIdFieldDefinition extends TextFieldDefinition {

    private String _workspace = "";
    private String _nodeType = "";

    public String getWorkspace() {
        return _workspace;
    }

    public void setWorkspace(String workspace) {
        _workspace = workspace;
    }

    public String getNodeType() {
        return _nodeType;
    }

    public void setNodeType(String nodeType) {
        _nodeType = nodeType;
    }
}
