package com.spring.batch.configuration;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.boot.context.properties.bind.BindException;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomFieldSetMapper<T> implements FieldSetMapper<T> {

    private final Class<T> type;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public CustomFieldSetMapper(Class<T> type) {
        this.type = type;
    }

    @Override
    public T mapFieldSet(FieldSet fieldSet) throws BindException {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(type);
        wrapper.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(LocalDateTime.parse(text, formatter));
            }
        });

        wrapper.setPropertyValues(new MutablePropertyValues(fieldSet.getProperties()));
        return (T) wrapper.getWrappedInstance();
    }
}
