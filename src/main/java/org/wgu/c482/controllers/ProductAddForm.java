package org.wgu.c482.controllers;

import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Product;
import org.wgu.c482.services.ProductService;

import java.util.Map;

public class ProductAddForm extends ProductForm {

    @Override
    protected void formAction(Map<String, Object> productDTO){

        Product newProduct = new ProductService.Builder()
                .setName((String) productDTO.get(nameLabel.getText()))
                .setPrice((Double) productDTO.get(priceLabel.getText()))
                .setStock((Integer) productDTO.get(stockLabel.getText()))
                .setMin((Integer) productDTO.get(minLabel.getText()))
                .setMax((Integer) productDTO.get(maxLabel.getText()))
                .build();

        newProduct.getAllAssociatedParts().addAll(productParts);

        Inventory.addProduct(newProduct);
    }
}
