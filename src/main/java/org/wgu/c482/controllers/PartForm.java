package org.wgu.c482.controllers;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.wgu.c482.models.InHouse;
import org.wgu.c482.models.Outsourced;
import org.wgu.c482.models.Part;
import org.wgu.c482.utils.FXUtils;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.wgu.c482.utils.FXUtils.Button.goHomeAction;
import static org.wgu.c482.utils.FXUtils.Button.goHome;
import static org.wgu.c482.utils.InventoryUtils.PartUtils.createNewInHousePart;
import static org.wgu.c482.utils.InventoryUtils.PartUtils.createNewOutsourcedPart;
import static org.wgu.c482.views.Dialogs.showErrorDialog;
import static org.wgu.c482.views.Borders.defaultBorder;
import static org.wgu.c482.views.Borders.errorBorder;

public abstract class PartForm implements Initializable {
    @FXML RadioButton inHouseRadio;
    @FXML RadioButton outsourcedRadio;
    @FXML Label machineCompanyLabel;
    @FXML TextField nameField;
    @FXML TextField stockField;
    @FXML TextField priceField;
    @FXML TextField maxField;
    @FXML TextField minField;
    @FXML TextField machineCompanyField;
    @FXML Button saveButton;
    @FXML Button cancelButton;

    protected List<TextField> allTextFields;
    protected ToggleGroup partsGroup = new ToggleGroup();

    protected final String INHOUSE = "inhouse";
    protected final String OUTSOURCED = "outsourced";
    private TextFieldHelper textFieldHelper;
    protected PartHelper partHelper;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.allTextFields = List.of(nameField, stockField, priceField, maxField, minField, machineCompanyField);
        textFieldHelper = new TextFieldHelper(this.allTextFields);
        textFieldHelper.initTextFields();

        (new RadioButtonHelper()).initRadioButtons();

        partHelper = new PartHelper();

        this.saveButton.setOnAction(partHelper.savePartAndGoHome);
        this.cancelButton.setOnAction(goHome);
    }
    protected abstract void formAction();

    private class RadioButtonHelper {
        public void initRadioButtons(){
            this.setToggleEventHandler();

            inHouseRadio.setSelected(true);
            inHouseRadio.setUserData(INHOUSE);
            inHouseRadio.setToggleGroup(partsGroup);

            outsourcedRadio.setSelected(false);
            outsourcedRadio.setUserData(OUTSOURCED);
            outsourcedRadio.setToggleGroup(partsGroup);
        }

        private void setToggleEventHandler(){
            partsGroup.selectedToggleProperty().addListener(partsGroupEventHandler);
        }

        private final ChangeListener<? super Toggle> partsGroupEventHandler = (observableValue, previousToggle, newToggle) -> {
            Optional<Toggle> toggle = Optional.ofNullable(newToggle);

            toggle.ifPresent(t -> {
                String newToggleValue = t.getUserData().toString();
                String labelText = newToggleValue.equals(OUTSOURCED) ? "Company Name" : "Machine ID";
                machineCompanyLabel.setText(labelText);
                textFieldHelper.resetFieldBorders();
            });
        };
    }

    private class TextFieldHelper {
        private final List<TextField> textFields;

        private TextFieldHelper(List<TextField> textFields) {
            this.textFields = textFields;
        }

        public void resetFieldBorders(){
            this.textFields.forEach(textField -> textField.setBorder(defaultBorder));
        }

        public void initTextFields(){
            this.textFields.forEach(FXUtils.TextInput::onChangeResetBorder);
        }

        public void invalidateFields(){
            Consumer<TextField> emptyFieldsInvalidator = textField -> {
                if(textField.getText().isBlank() || textField.getText().isEmpty()){
                    textField.setBorder(errorBorder);
                }
            };

            Predicate<TextField> numericalFieldsFilter = textField -> {
                boolean notNameField = !textField.equals(nameField);
                boolean notMachineCompanyField = !textField.equals(machineCompanyField);

                if(outsourcedRadio.isSelected())
                    return notNameField && notMachineCompanyField;
                else
                    return notNameField;
            };

            Consumer<TextField> nonDigitInvalidator  = textField -> {
                if(!textField.getText().matches("^\\d*\\.?\\d*$"))
                    textField.setBorder(errorBorder);
            };

            this.textFields.stream()
                    .peek(emptyFieldsInvalidator)
                    .filter(numericalFieldsFilter)
                    .forEach(nonDigitInvalidator);
        }

        public void invalidateField(String errorMsg){
            String target = errorMsg.split(" ")[0].toLowerCase();
            this.textFields.stream()
                    .filter(textField ->
                            !textField.equals(machineCompanyField) && textField.getId().startsWith(target))
                    .findFirst().ifPresent(textField -> textField.setBorder(errorBorder));
        }

        public void verifyFieldsHaveValue(){
            this.textFields.forEach(textField -> {
                if(textField.getText().isEmpty() || textField.getText().isBlank()){
                    throw new IllegalArgumentException("One or more fields are blank or empty!");
                }
            });
        }

        public void verifyNumericalFields(){
            try {
                Double.parseDouble(priceField.getText());
                Integer.parseInt(stockField.getText());
                Integer.parseInt(minField.getText());
                Integer.parseInt(maxField.getText());
                if(inHouseRadio.isSelected())
                    Integer.parseInt(machineCompanyField.getText());

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value must be numerical!");
            }
        }
    }

    protected class PartHelper {

        protected EventHandler<ActionEvent> savePartAndGoHome = event -> {
            try {
                textFieldHelper.resetFieldBorders();
                textFieldHelper.invalidateFields();
                textFieldHelper.verifyFieldsHaveValue();
                textFieldHelper.verifyNumericalFields();
                formAction();
                goHomeAction(event);
            } catch (IllegalArgumentException e){
                if(e.getMessage().startsWith("Company"))
                    machineCompanyField.setBorder(errorBorder);

                textFieldHelper.invalidateField(e.getMessage());

                showErrorDialog("Error", "Cannot add invalid part", e.getMessage());
            }
        };
        protected Part generatePart(){
            Part newPart;

            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            int min = Integer.parseInt(minField.getText());
            int max = Integer.parseInt(maxField.getText());
            String machineCompany = machineCompanyField.getText();

            if(inHouseRadio.isSelected())
                newPart = createNewInHousePart(name, price, stock, min, max, Integer.parseInt(machineCompany));
            else
                newPart = createNewOutsourcedPart(name, price, stock, min, max, machineCompany);

            return newPart;
        }

        protected Part generatePart(int id){
            Part newPart;

            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            int min = Integer.parseInt(minField.getText());
            int max = Integer.parseInt(maxField.getText());
            String machineCompany = machineCompanyField.getText();

            if(inHouseRadio.isSelected())
                newPart = new InHouse(id, name, price, stock, min, max, Integer.parseInt(machineCompany));
            else
                newPart = new Outsourced(id, name, price, stock, min, max, machineCompany);

            return newPart;
        }
    }
}

