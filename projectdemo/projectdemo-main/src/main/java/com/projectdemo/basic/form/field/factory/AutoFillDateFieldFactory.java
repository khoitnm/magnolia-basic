package com.projectdemo.basic.form.field.factory;

import com.projectdemo.basic.form.field.definition.AutoFillDateFieldDefinition;
import com.vaadin.data.Item;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Field;
import info.magnolia.ui.form.field.definition.DateFieldDefinition;
import info.magnolia.ui.form.field.factory.DateFieldFactory;

import java.time.Instant;
import java.util.Date;

/**
 * @author dat.dang (2016-Apr-05)
 */
public class AutoFillDateFieldFactory<D extends AutoFillDateFieldDefinition> extends DateFieldFactory {

    public AutoFillDateFieldFactory(DateFieldDefinition definition, Item relatedFieldItem) {
        super(definition, relatedFieldItem);
    }

    @Override
    public Field<Date> createField() {
        Field<Date> field = super.createField();
        field.setWidth(Sizeable.SIZE_UNDEFINED, Sizeable.Unit.PIXELS);
        field.setEnabled(false);

        if (field.getValue() == null) {
            field.setValue(getValue());
        }
        return field;
    }

    public Date getValue() {
        return Date.from(Instant.now());
    }
}
