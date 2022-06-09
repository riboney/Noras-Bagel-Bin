package org.wgu.c482.views.textfields;

import java.util.List;

/** Collection of BaseTextField objects for multiple validation checks */
public class BaseTextFieldList {
    private final List<BaseTextField> fields;

    public BaseTextFieldList(List<BaseTextField> fields) {
        this.fields = fields;
    }

    public List<BaseTextField> getFields() {
        return fields;
    }

    public void resetFields(){
        this.fields.forEach(field -> field.isInvalid(false));
    }

    public void validateFields(){
        this.fields.forEach(BaseTextField::validate);
    }

    public void invalidateField(String fieldName){
        this.fields.stream()
                .filter(field -> field.getTextField().getId().startsWith(fieldName))
                .findFirst()
                .ifPresent(field -> field.isInvalid(true));
    }
}
