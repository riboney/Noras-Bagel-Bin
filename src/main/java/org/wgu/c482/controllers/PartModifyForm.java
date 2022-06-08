package org.wgu.c482.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.wgu.c482.models.InHouse;
import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Outsourced;
import org.wgu.c482.models.Part;

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

    protected void formAction(Map<String, String> partDTO){
        String selectedToggle = (String) partsGroup.selectedToggleProperty().get().getUserData();

        Part updatedPart;
        int id = part.getId();
        String name = partDTO.get(nameLabel.getText());
        double price = Double.parseDouble(partDTO.get(priceLabel.getText()));
        int stock = Integer.parseInt(partDTO.get(stockLabel.getText()));
        int min = Integer.parseInt(partDTO.get(minLabel.getText()));
        int max = Integer.parseInt(partDTO.get(maxLabel.getText()));

        if(selectedToggle.equals(INHOUSE)){
            int machineId = Integer.parseInt(machineCompanyField.getText());
            updatedPart = new InHouse(id, name, price, stock, min, max, machineId);
        } else {
            String companyName = machineCompanyField.getText();
            updatedPart = new Outsourced(id, name, price, stock, min, max, companyName);
        }

        Inventory.updatePart(partIndex, updatedPart);
    }
}
