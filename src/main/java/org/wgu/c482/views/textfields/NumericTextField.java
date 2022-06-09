package org.wgu.c482.views.textfields;

import javafx.scene.control.TextField;

import java.util.function.Consumer;

/** TextField decorator for numeric validation checks */
public class NumericTextField extends BaseTextField {
    public NumericTextField(String label, TextField textField){
        super(label, textField);
        Consumer<String> invalidateIfNonNumeric = content -> {
            if(!content.matches("^\\d*\\.?\\d*$")){
                isInvalid(true);
                throw new IllegalArgumentException(super.getLabel() + " must be numerical!");
            }
        };

        super.addValidator(invalidateIfNonNumeric);
    }
}
