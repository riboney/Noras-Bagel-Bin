package org.wgu.c482.controllers;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.wgu.c482.views.textfields.BaseTextFieldWrapper;
import org.wgu.c482.views.textfields.NumericTextFieldWrapper;
import org.wgu.c482.views.textfields.TextFieldWrapperList;

import java.net.URL;
import java.util.*;

import static org.wgu.c482.utils.FXUtils.Button.goHomeAction;
import static org.wgu.c482.utils.FXUtils.Button.goHome;
import static org.wgu.c482.views.Dialogs.showErrorDialog;

public abstract class PartForm implements Initializable {
    @FXML RadioButton inHouseRadio;
    @FXML RadioButton outsourcedRadio;
    @FXML Label nameLabel;
    @FXML Label stockLabel;
    @FXML Label priceLabel;
    @FXML Label maxLabel;
    @FXML Label minLabel;

    @FXML Label machineCompanyLabel;
    @FXML TextField nameField;
    @FXML TextField stockField;
    @FXML TextField priceField;
    @FXML TextField maxField;
    @FXML TextField minField;
    @FXML TextField machineCompanyField;
    @FXML Button saveButton;
    @FXML Button cancelButton;

    protected BaseTextFieldWrapper name;
    protected BaseTextFieldWrapper companyName;
    protected NumericTextFieldWrapper stock;
    protected NumericTextFieldWrapper price;
    protected NumericTextFieldWrapper max;
    protected NumericTextFieldWrapper min;
    protected NumericTextFieldWrapper machineId;
    protected TextFieldWrapperList allFields;

    protected ToggleGroup partsGroup = new ToggleGroup();

    protected final String INHOUSE = "inhouse";
    protected final String OUTSOURCED = "outsourced";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTextFields();
        initRadioButtons();
        initButtons();
    }

    private void initTextFields(){
         name = new BaseTextFieldWrapper(nameLabel.getText(), nameField);
         stock = new NumericTextFieldWrapper(stockLabel.getText(), stockField);
         price = new NumericTextFieldWrapper(priceLabel.getText(), priceField);
         max = new NumericTextFieldWrapper(maxLabel.getText(), maxField);
         min = new NumericTextFieldWrapper(minLabel.getText(), minField);

         companyName = new BaseTextFieldWrapper(OUTSOURCED, machineCompanyField);
         machineId = new NumericTextFieldWrapper(INHOUSE, machineCompanyField);
         ArrayList<BaseTextFieldWrapper> list = new ArrayList<>(Arrays.asList(name, stock, price, max, min));

         allFields = new TextFieldWrapperList(list);
    }

    public void initRadioButtons(){
        ChangeListener<? super Toggle> changeLabelAndResetFieldsOnToggle = (observableValue, previousToggle, newToggle) -> {
            Optional<Toggle> toggle = Optional.ofNullable(newToggle);

            toggle.ifPresent(t -> {
                String newToggleValue = t.getUserData().toString();
                String labelText = newToggleValue.equals(OUTSOURCED) ? "Company Name" : "Machine ID";

                if(newToggleValue.equals(OUTSOURCED)){
                    machineCompanyLabel.setText("Company Name");
                    allFields.getFields().add(companyName);
                    allFields.getFields().remove(machineId);
                } else {
                    machineCompanyLabel.setText("Machine ID");
                    allFields.getFields().add(machineId);
                    allFields.getFields().remove(companyName);
                }

                machineCompanyLabel.setText(labelText);

                allFields.resetFields();
            });
        };

        partsGroup.selectedToggleProperty().addListener(changeLabelAndResetFieldsOnToggle);


        inHouseRadio.setSelected(true);
        inHouseRadio.setUserData(INHOUSE);
        inHouseRadio.setToggleGroup(partsGroup);

        outsourcedRadio.setSelected(false);
        outsourcedRadio.setUserData(OUTSOURCED);
        outsourcedRadio.setToggleGroup(partsGroup);
    }

    private void initButtons(){
        EventHandler<ActionEvent> savePartAndGoHome = event -> {
            try {
                allFields.validateFields();
                Map<String, String> part = generatePartFromFields();
                formAction(part);
                goHomeAction(event);
            } catch (IllegalArgumentException e){
                e.printStackTrace();
                allFields.invalidateField(e.getMessage());
                showErrorDialog("Error", "Cannot add invalid part", e.getMessage());
            }
        };

        this.saveButton.setOnAction(savePartAndGoHome);
        this.cancelButton.setOnAction(goHome);
    }

    protected Map<String, String> generatePartFromFields(){
        Map<String, String> partDTO = new HashMap<>();

        allFields.getFields().forEach(field ->
                partDTO.put(field.getLabel(), field.getTextField().getText()));

        return partDTO;
    }

    protected abstract void formAction(Map<String, String> part);
}
