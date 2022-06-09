package org.wgu.c482.controllers;

import org.wgu.c482.models.InHouse;
import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Outsourced;
import org.wgu.c482.models.Part;
import org.wgu.c482.utils.InventoryUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class PartAddForm extends PartForm {

    @Override
    protected void formAction(Map<String, Object> part){
        String selectedToggle = (String) partsGroup.selectedToggleProperty().get().getUserData();
        Part newPart = null;

        if(selectedToggle.equals(INHOUSE)){
            newPart = InventoryUtils.PartUtils.createNewInHousePart(
                    (String) part.get(nameLabel.getText()),
                    (Double) part.get(priceLabel.getText()),
                    (Integer) part.get(stockLabel.getText()),
                    (Integer) part.get(minLabel.getText()),
                    (Integer) part.get(maxLabel.getText()),
                    (Integer) part.get(machineCompanyLabel.getText())
            );
        } else {
            newPart = InventoryUtils.PartUtils.createNewOutsourcedPart(
                    (String) part.get(nameLabel.getText()),
                    (Double) part.get(priceLabel.getText()),
                    (Integer) part.get(stockLabel.getText()),
                    (Integer) part.get(minLabel.getText()),
                    (Integer) part.get(maxLabel.getText()),
                    (String) part.get(machineCompanyLabel.getText())
            );
        }

        Inventory.addPart(newPart);
    }

//    @Override
//    protected void formAction(Map<String, String> part){
//        String selectedToggle = (String) partsGroup.selectedToggleProperty().get().getUserData();
//
//        Part newPart;
//        String name = part.get(nameLabel.getText());
//        double price = Double.parseDouble(part.get(priceLabel.getText()));
//        int stock = Integer.parseInt(part.get(stockLabel.getText()));
//        int min = Integer.parseInt(part.get(minLabel.getText()));
//        int max = Integer.parseInt(part.get(maxLabel.getText()));
//
//        if(selectedToggle.equals(INHOUSE)){
//            int machineId = Integer.parseInt(machineCompanyField.getText());
//
//            newPart = InventoryUtils.PartUtils.createNewInHousePart(name, price, stock, min, max, machineId);
//        } else {
//            String companyName = machineCompanyField.getText();
//
//            newPart = InventoryUtils.PartUtils.createNewOutsourcedPart(name, price, stock, min, max, companyName);
//        }
//
//        Inventory.addPart(newPart);
//    }
}
