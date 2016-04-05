package com.projectdemo.basic.form.field.factory;

import com.projectdemo.basic.common.util.JcrNodeHelper;
import com.projectdemo.basic.form.field.definition.UniqueAssetIdFieldDefinition;
import com.vaadin.data.Item;
import com.vaadin.ui.Field;
import info.magnolia.ui.form.field.factory.TextFieldFactory;

/**
 * @param <D> subtype of UniqueAssetIdFieldDefinition.
 * @author khoa.nguyenkieu.
 */
public class UniqueAssetIdFieldFactory<D extends UniqueAssetIdFieldDefinition> extends TextFieldFactory {

    private UniqueAssetIdFieldDefinition _definition;

    public UniqueAssetIdFieldFactory(D definition, Item relatedFieldItem) {
        super(definition, relatedFieldItem);
        _definition = definition;
    }

    @Override
    public Field<String> createField() {
        final Field field = super.createField();
        if (field.getValue() == null) {
            field.setValue(String.valueOf(getValue()));
        }
        //disabled field
        field.setEnabled(false);
        return field;
    }

    private Long getValue(){
        return JcrNodeHelper.getNextMaxAvailableId(_definition.getNodeType(), _definition.getWorkspace(), _definition.getName());
    }
}
