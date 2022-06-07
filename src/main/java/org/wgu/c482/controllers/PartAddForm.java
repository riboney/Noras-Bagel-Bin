package org.wgu.c482.controllers;

import org.wgu.c482.models.Inventory;

public class PartAddForm extends PartForm {

    @Override
    protected void formAction(){
        Inventory.addPart(this.partHelper.generatePart());
    }
}
