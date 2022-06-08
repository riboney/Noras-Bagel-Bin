package org.wgu.c482.views.textfields;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.wgu.c482.views.Borders.defaultBorder;
import static org.wgu.c482.views.Borders.errorBorder;

public class BaseTextFieldWrapper {

    private String label;
    private TextField textField;
    private final BooleanProperty invalidStatus;
    private final List<Consumer<String>> validators;

    {
        invalidStatus = new SimpleBooleanProperty();
        invalidStatus.addListener((obs, oldV, newV) -> {
            if(newV) this.textField.setBorder(errorBorder);
            else this.textField.setBorder(defaultBorder);
        });

        validators = new ArrayList<>();
        Consumer<String> invalidateIfEmpty = content -> {
            if(content.isEmpty() || content.isBlank()){
                setInvalidStatus(true);
                throw new IllegalArgumentException(this.label + " cannot be blank or empty!");
            }

        };
        validators.add(invalidateIfEmpty);

    }
    public BaseTextFieldWrapper(String label, TextField textField) {
        this.label = label;
        this.textField = textField;
        decorate();
    }

    private void decorate(){
        this.textField.setBorder(defaultBorder);

        this.textField.textProperty().addListener((obs, o, c) -> setInvalidStatus(false));
    }

    public String getLabel(){
        return label;
    }
    public TextField getTextField() {
        return textField;
    }

    public final boolean getInvalidStatus() {
        return invalidStatus.get();
    }

    public final void setInvalidStatus(boolean isInvalid){
        invalidStatus.set(isInvalid);
    }

    public final List<Consumer<String>> getValidators(){
        return this.validators;
    }
    public final void addValidator(Consumer<String> validator){
        this.validators.add(validator);
    }

    public final void validate(){
        validators.forEach(validator -> validator.accept(textField.getText()));
    }
}
