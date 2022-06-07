package org.wgu.c482.controllers;

import org.wgu.c482.models.Inventory;

public class ProductAddForm extends ProductForm {

    @Override
    protected void formAction(){
        Inventory.addProduct(this.productHelper.generateProduct());
    }
}
