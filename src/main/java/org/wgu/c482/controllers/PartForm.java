package org.wgu.c482.controllers;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.wgu.c482.models.Part;
import org.wgu.c482.views.tables.TableButton;
import org.wgu.c482.views.textfields.BaseTextField;
import org.wgu.c482.views.textfields.NumericTextField;
import org.wgu.c482.views.textfields.BaseTextFieldList;

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

    protected BaseTextField name;
    protected BaseTextField companyName;
    protected NumericTextField stock;
    protected NumericTextField price;
    protected NumericTextField max;
    protected NumericTextField min;
    protected NumericTextField machineId;
    protected BaseTextFieldList allFields;

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
         name = new BaseTextField(nameLabel.getText(), nameField);
         stock = new NumericTextField(stockLabel.getText(), stockField);
         price = new NumericTextField(priceLabel.getText(), priceField);
         max = new NumericTextField(maxLabel.getText(), maxField);
         min = new NumericTextField(minLabel.getText(), minField);

         companyName = new BaseTextField(OUTSOURCED, machineCompanyField);
         machineId = new NumericTextField(INHOUSE, machineCompanyField);
         ArrayList<BaseTextField> list = new ArrayList<>(Arrays.asList(name, stock, price, max, min));

         allFields = new BaseTextFieldList(list);
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
                Map<String, Object> partDTO = generatePartDTO();
                formAction(partDTO);
                goHomeAction(event);
            } catch (IllegalArgumentException e){
                allFields.invalidateField(e.getMessage());
                showErrorDialog("Error", "Cannot add invalid part", e.getMessage());
            }
        };
        this.saveButton.setOnAction(savePartAndGoHome);
        this.cancelButton.setOnAction(goHome);
    }

//    protected Map<String, String> generatePartFromFields(){
//        Map<String, String> partDTO = new HashMap<>();
//
//        allFields.getFields().forEach(field ->
//                partDTO.put(field.getLabel(), field.getTextField().getText()));
//
//        return partDTO;
//    }

    protected Map<String, Object> generatePartDTO(){
        Map<String, Object> partDTO = new HashMap<>();

        partDTO.put(nameLabel.getText(), nameField.getText());
        partDTO.put(priceLabel.getText(), Double.parseDouble(priceField.getText()));
        partDTO.put(stockLabel.getText(), Integer.parseInt(stockField.getText()));
        partDTO.put(minLabel.getText(), Integer.parseInt(minField.getText()));
        partDTO.put(maxLabel.getText(), Integer.parseInt(maxField.getText()));

        String selectedToggle = (String) partsGroup.selectedToggleProperty().get().getUserData();
        if(selectedToggle.equals(INHOUSE))
            partDTO.put(machineCompanyLabel.getText(), Integer.parseInt(machineCompanyField.getText()));
        else
            partDTO.put(machineCompanyLabel.getText(), machineCompanyField.getText());

        return partDTO;
    }

    protected abstract void formAction(Map<String, Object> partDTO);
}
