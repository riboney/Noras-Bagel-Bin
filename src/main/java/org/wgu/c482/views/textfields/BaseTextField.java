package org.wgu.c482.views.textfields;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.wgu.c482.views.Borders.defaultBorder;
import static org.wgu.c482.views.Borders.errorBorder;
import static org.wgu.c482.views.Dialogs.showErrorDialog;

/** TextField decorator for validation checks */
public class BaseTextField {

    private String label;
    private TextField textField;
    private final BooleanProperty invalidStatus;
    private Optional<String> invalidStatusMessage;
    private final List<Consumer<String>> validators;

    {
        invalidStatus = new SimpleBooleanProperty();
        invalidStatus.addListener((obs, oldV, newV) -> {
            if(newV) {
                this.textField.setBorder(errorBorder);
                invalidStatusMessage.ifPresent(msg -> showErrorDialog("Failed search",
                                                                    "No results found for \"" + msg + "\"",
                                                                    "Please try again!"));
            }
            else this.textField.setBorder(defaultBorder);
        });

        validators = new ArrayList<>();
        Consumer<String> invalidateIfEmpty = content -> {
            if(content.isEmpty() || content.isBlank()){
                isInvalid(true);
                throw new IllegalArgumentException(this.label + " cannot be blank or empty!");
            }
        };
        validators.add(invalidateIfEmpty);

    }
    public BaseTextField(String label, TextField textField) {
        this.label = label;
        this.textField = textField;
        invalidStatusMessage = Optional.empty();
        decorate();
    }

    private void decorate(){
        this.textField.setBorder(defaultBorder);

        this.textField.textProperty().addListener((obs, o, c) -> isInvalid(false));
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

    public final void isInvalid(boolean isInvalid){
        invalidStatus.set(isInvalid);
    }

    public final void isInvalid(boolean isInvalid, String errorMessage){
        invalidStatusMessage = Optional.ofNullable(errorMessage);
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
