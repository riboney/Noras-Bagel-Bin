package org.wgu.c482.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;
import org.wgu.c482.models.InHouse;
import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Outsourced;
import org.wgu.c482.models.Product;

import java.awt.*;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ProductModifyForm extends ProductForm{

    @FXML TextField idField;
    private final Product product;
    private final int productIndex;

    public ProductModifyForm(Product product, int productIndex) {
        this.product = product;
        this.productIndex = productIndex;
        this.productParts.addAll(this.product.getAllAssociatedParts());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        super.initialize(url, resourceBundle);

        populateFields();
    }

    private void populateFields(){
        this.idField.setText(product.getId() + " (Auto-generated)");
        this.nameField.setText(product.getName());
        this.stockField.setText(Integer.toString(product.getStock()));
        this.priceField.setText(Double.toString(product.getPrice()));
        this.maxField.setText(Integer.toString(product.getMax()));
        this.minField.setText(Integer.toString(product.getMin()));
    }

    @Override
    protected void formAction(Map<String, Object> productDTO) {
        Product updatedProduct = new Product(
                product.getId(),
                (String) productDTO.get(nameLabel.getText()),
                (Double) productDTO.get(priceLabel.getText()),
                (Integer) productDTO.get(stockLabel.getText()),
                (Integer) productDTO.get(minLabel.getText()),
                (Integer) productDTO.get(maxLabel.getText())
        );

        updatedProduct.getAllAssociatedParts().addAll(productParts);

        Inventory.updateProduct(productIndex, updatedProduct);
    }
}
