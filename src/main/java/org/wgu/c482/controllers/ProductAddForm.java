package org.wgu.c482.controllers;

import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Product;
import org.wgu.c482.utils.InventoryUtils;

import java.util.Map;

public class ProductAddForm extends ProductForm {

    @Override
    protected void formAction(Map<String, Object> productDTO){
        Product newProduct = InventoryUtils.ProductUtils.createNewProduct(
                (String) productDTO.get(nameLabel.getText()),
                (Double) productDTO.get(priceLabel.getText()),
                (Integer) productDTO.get(stockLabel.getText()),
                (Integer) productDTO.get(minLabel.getText()),
                (Integer) productDTO.get(maxLabel.getText())
        );

        newProduct.getAllAssociatedParts().addAll(productParts);

        Inventory.addProduct(newProduct);
    }
}
