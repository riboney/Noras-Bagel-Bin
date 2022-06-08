package org.wgu.c482.views.textfields;

import java.util.List;

public class TextFieldWrapperList {
    private final List<BaseTextFieldWrapper> fields;

    public TextFieldWrapperList(List<BaseTextFieldWrapper> fields) {
        this.fields = fields;
    }

    public List<BaseTextFieldWrapper> getFields() {
        return fields;
    }

    public void resetFields(){
        this.fields.forEach(field -> field.setInvalidStatus(false));
    }

    public void validateFields(){
        this.fields.forEach(BaseTextFieldWrapper::validate);
    }

    public void invalidateField(String fieldName){
        this.fields.stream()
                .filter(field -> field.getTextField().getId().startsWith(fieldName))
                .findFirst()
                .ifPresent(field -> field.setInvalidStatus(true));
    }
}
