package org.wgu.c482.views.textfields;

import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class NumericTextFieldWrapper extends BaseTextFieldWrapper {
    public NumericTextFieldWrapper(String label, TextField textField){
        super(label, textField);
        Consumer<String> invalidateIfNonNumeric = content -> {
            if(!content.matches("^\\d*\\.?\\d*$")){
                setInvalidStatus(true);
                throw new IllegalArgumentException(super.getLabel() + " must be numerical!");
            }
        };

        super.addValidator(invalidateIfNonNumeric);
    }
}
