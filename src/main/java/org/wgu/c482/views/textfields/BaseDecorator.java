package org.wgu.c482.views.textfields;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.TextField;

import static org.wgu.c482.views.Borders.defaultBorder;
import static org.wgu.c482.views.Borders.errorBorder;

public class BaseDecorator {

    private final TextField textField;
    private BooleanProperty invalidStatus;

    public BaseDecorator(TextField textField) {
        this.textField = textField;
        this.invalidStatus = initInvalidStatus();
    }

    public final boolean getInvalidStatus() { return invalidStatus.get();}
    public final void setInvalidStatus(boolean isInvalid){ invalidStatus.set(isInvalid);}

    private void decorate(){
        this.textField.setBorder(defaultBorder);

        this.textField.textProperty().addListener((obs, o, c) -> setInvalidStatus(false));
    }

    private BooleanProperty initInvalidStatus(){
        BooleanProperty status = new SimpleBooleanProperty();

        status.addListener((obs, oldV, newV) -> {
            if(newV) this.textField.setBorder(errorBorder);
            else this.textField.setBorder(defaultBorder);
        });

        return status;
    }
}
