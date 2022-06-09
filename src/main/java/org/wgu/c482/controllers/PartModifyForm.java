package org.wgu.c482.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.wgu.c482.models.InHouse;
import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Outsourced;
import org.wgu.c482.models.Part;
import org.wgu.c482.services.PartService;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class PartModifyForm extends PartForm {

    @FXML TextField idField;
    private final Part part;
    private final int partIndex;

    public PartModifyForm(Part part, int partIndex){
        this.part = part;
        this.partIndex = partIndex;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        if(part instanceof Outsourced){
            this.inHouseRadio.setSelected(false);
            this.outsourcedRadio.setSelected(true);
        }

        populateFields();
    }

    private void populateFields(){
        this.idField.setText(part.getId() + " (Auto-generated)");
        this.nameField.setText(part.getName());
        this.stockField.setText(Integer.toString(part.getStock()));
        this.priceField.setText(String.format("%.2f", part.getPrice()));
        this.maxField.setText(Integer.toString(part.getMax()));
        this.minField.setText(Integer.toString(part.getMin()));

        if(part instanceof InHouse)
            this.machineCompanyField.setText(Integer.toString((((InHouse) part).getMachineId())));
        else
            this.machineCompanyField.setText(((Outsourced) part).getCompanyName());
    }

    @Override
    protected void formAction(Map<String, Object> partDTO){
        String selectedToggle = (String) partsGroup.selectedToggleProperty().get().getUserData();

        PartService.Builder updatedPartBuilder = new PartService.Builder()
                .setId(part.getId())
                .setName((String) partDTO.get(nameLabel.getText()))
                .setPrice((Double) partDTO.get(priceLabel.getText()))
                .setStock((Integer) partDTO.get(stockLabel.getText()))
                .setMin((Integer) partDTO.get(minLabel.getText()))
                .setMax((Integer) partDTO.get(maxLabel.getText()));

        if(selectedToggle.equals(INHOUSE))
            updatedPartBuilder.setMachineId((Integer) partDTO.get(machineCompanyLabel.getText()));
        else
            updatedPartBuilder.setCompanyName((String) partDTO.get(machineCompanyLabel.getText()));

        Inventory.updatePart(partIndex, updatedPartBuilder.build());
    }
}
