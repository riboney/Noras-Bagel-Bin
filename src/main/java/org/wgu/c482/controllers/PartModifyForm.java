package org.wgu.c482.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.wgu.c482.models.InHouse;
import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Outsourced;
import org.wgu.c482.models.Part;

import java.net.URL;
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
        this.priceField.setText(Double.toString(part.getPrice()));
        this.maxField.setText(Integer.toString(part.getMax()));
        this.minField.setText(Integer.toString(part.getMin()));

        if(part instanceof InHouse)
            this.machineCompanyField.setText(Integer.toString((((InHouse) part).getMachineId())));
        else
            this.machineCompanyField.setText(((Outsourced) part).getCompanyName());
    }

    @Override
    protected void formAction(){
        Inventory.updatePart(partIndex, partHelper.generatePart(part.getId()));
    }
}
