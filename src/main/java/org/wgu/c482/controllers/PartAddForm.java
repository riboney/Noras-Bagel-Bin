package org.wgu.c482.controllers;

import org.wgu.c482.models.Inventory;
import org.wgu.c482.services.PartService;

import java.util.Map;

public class PartAddForm extends PartForm {
    @Override
    protected void formAction(Map<String, Object> partDTO){
        String selectedToggle = (String) partsGroup.selectedToggleProperty().get().getUserData();

        PartService.Builder newPartBuilder = new PartService.Builder()
                .setName((String) partDTO.get(nameLabel.getText()))
                .setPrice((Double) partDTO.get(priceLabel.getText()))
                .setStock((Integer) partDTO.get(stockLabel.getText()))
                .setMin((Integer) partDTO.get(minLabel.getText()))
                .setMax((Integer) partDTO.get(maxLabel.getText()));

        if(selectedToggle.equals(INHOUSE))
            newPartBuilder.setMachineId((Integer) partDTO.get(machineCompanyLabel.getText()));
        else
            newPartBuilder.setCompanyName((String) partDTO.get(machineCompanyLabel.getText()));

        Inventory.addPart(newPartBuilder.build());
    }
}
