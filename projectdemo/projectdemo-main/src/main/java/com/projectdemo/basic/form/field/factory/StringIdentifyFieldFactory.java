package com.projectdemo.basic.form.field.factory;

import com.projectdemo.basic.form.field.definition.StringIdentifyFieldDefinition;
import com.vaadin.data.Item;
import com.vaadin.ui.Field;
import info.magnolia.ui.form.field.factory.TextFieldFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @param <D> subtype of StringIdentifyFieldDefinition.
 * @author dat.dang.
 */
public class StringIdentifyFieldFactory<D extends StringIdentifyFieldDefinition> extends TextFieldFactory {
    private static final String STRING_IDENTIFY_PREFIX = "PROJECT-DEMO";

    public StringIdentifyFieldFactory(D definition, Item relatedFieldItem) {
        super(definition, relatedFieldItem);
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

    private String getValue(){
        ZoneId defaultZone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.now(), defaultZone);
        return STRING_IDENTIFY_PREFIX + "-" + localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
