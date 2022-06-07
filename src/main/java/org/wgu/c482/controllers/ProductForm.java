package org.wgu.c482.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Part;
import org.wgu.c482.models.Product;
import org.wgu.c482.utils.FXUtils;
import org.wgu.c482.utils.InventoryUtils;
import org.wgu.c482.views.Tables;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.wgu.c482.models.Inventory.getAllParts;
import static org.wgu.c482.utils.FXUtils.Button.goHomeAction;
import static org.wgu.c482.utils.FXUtils.Button.goHome;
import static org.wgu.c482.utils.FXUtils.Table.tableButtonAction;
import static org.wgu.c482.utils.FXUtils.TextInput.onEnterTableLookup;
import static org.wgu.c482.utils.InventoryUtils.ProductUtils.createNewProduct;
import static org.wgu.c482.views.Dialogs.*;
import static org.wgu.c482.views.Borders.defaultBorder;
import static org.wgu.c482.views.Borders.errorBorder;

public abstract class ProductForm implements Initializable {

    @FXML TextField nameField;
    @FXML TextField stockField;
    @FXML TextField priceField;
    @FXML TextField maxField;
    @FXML TextField minField;

    @FXML TextField partSearch;
    @FXML TableView<Part> partsTable;
    @FXML TableView<Part> productPartsTable;

    @FXML Button addPartButton;
    @FXML Button removePartButton;
    @FXML Button saveButton;
    @FXML Button cancelButton;

    protected List<TextField> allTextFields;
    private TextFieldHelper textFieldHelper;
    protected ProductHelper productHelper;
    protected ObservableList<Part> productParts = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.allTextFields = List.of(nameField, stockField, priceField, maxField, minField);
        textFieldHelper = new TextFieldHelper(this.allTextFields);
        textFieldHelper.initTextFields();

        productHelper = new ProductHelper();

        Tables.initPartTable(partsTable, Inventory.getAllParts());
        Tables.initPartTable(productPartsTable, productParts);

        initButtons();
        initSearchBar();
    }

    private void initButtons(){
        saveButton.setOnAction(productHelper.saveProductAndGoHome);
        addPartButton.setOnAction(tableButtonAction(productHelper.addToProductParts, partsTable));
        removePartButton.setOnAction(tableButtonAction(productHelper.confirmThenRemoveProductPart, productPartsTable));
        cancelButton.setOnAction(goHome);
    }

    private void initSearchBar(){
        partSearch.setOnKeyPressed(onEnterTableLookup(partsTable, partSearch, InventoryUtils.PartUtils::searchPart, getAllParts()));

    }

    protected abstract void formAction();

    private class TextFieldHelper {
        private final List<TextField> textFields;

        private TextFieldHelper(List<TextField> textFields) {
            this.textFields = textFields;
        }

        public void resetFieldBorders(){
            this.textFields.forEach(textField -> textField.setBorder(defaultBorder));
        }

        public void initTextFields(){
            this.textFields.forEach(FXUtils.TextInput::onChangeResetBorder);
        }

        public void invalidateFields(){
            Consumer<TextField> emptyFieldsInvalidator = textField -> {
                if(textField.getText().isBlank() || textField.getText().isEmpty()){
                    textField.setBorder(errorBorder);
                }
            };

            Predicate<TextField> numericalFieldsFilter = textField -> !textField.equals(nameField);

            Consumer<TextField> nonDigitInvalidator  = textField -> {
                if(!textField.getText().matches("^\\d*\\.?\\d*$"))
                    textField.setBorder(errorBorder);
            };

            this.textFields.stream()
                    .peek(emptyFieldsInvalidator)
                    .filter(numericalFieldsFilter)
                    .forEach(nonDigitInvalidator);
        }

        public void invalidateField(String errorMsg){
            String target = errorMsg.split(" ")[0].toLowerCase();
            this.textFields.stream()
                    .filter(textField -> textField.getId().startsWith(target))
                    .findFirst().ifPresent(textField -> textField.setBorder(errorBorder));
        }

        public void verifyFieldsHaveValue(){
            this.textFields.forEach(textField -> {
                if(textField.getText().isEmpty() || textField.getText().isBlank()){
                    throw new IllegalArgumentException("One or more fields are blank or empty!");
                }
            });
        }

        public void verifyNumericalFields(){
            try {
                Double.parseDouble(priceField.getText());
                Integer.parseInt(stockField.getText());
                Integer.parseInt(minField.getText());
                Integer.parseInt(maxField.getText());

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value must be numerical!");
            }
        }
    }

    protected class ProductHelper {

        private final Consumer<Part> addToProductParts = part -> productParts.add(part);
//        private final EventHandler<ActionEvent> modifyProductParts = event -> {
//          Optional<Part> selectedPart = getSelectedTableItem(partsTable);
//          Consumer<Part> addToProductParts = part -> productParts.add(part);
//          Runnable showErrorMsg = () -> invalidActionDialog("Please select a part to add to Product's parts");
//
//          selectedPart.ifPresentOrElse(addToProductParts, showErrorMsg);
//          event.consume();
//        };

        private final Consumer<Part> confirmThenRemoveProductPart = part ->  {
            boolean removePart = showConfirmDialog("Product parts", "Removing: " + part.getName(), "Do you want to remove this part from the Product?");
            if(removePart) productParts.remove(part);
        };

//        private final EventHandler<ActionEvent> deleteProductPart = event -> {
//            Optional<Part> selectedPart = Optional.ofNullable(partsTable.getSelectionModel().getSelectedItem());
//            Consumer<Part> confirmThenRemove = part ->  {
//                boolean removePart = showConfirmDialog("Product parts", "Removing: " + part.getName(), "Do you want to remove this part from the Product?");
//                if(removePart) productParts.remove(part);
//            };
//            Runnable showErrorMsg = () -> invalidActionDialog("Please select a part to add to Product's parts");
//
//
//            selectedPart.ifPresentOrElse(confirmThenRemove, r);
//        };

        private final EventHandler<ActionEvent> saveProductAndGoHome = event -> {
            try {
                textFieldHelper.resetFieldBorders();
                textFieldHelper.invalidateFields();
                textFieldHelper.verifyFieldsHaveValue();
                textFieldHelper.verifyNumericalFields();
                formAction();
                goHomeAction(event);
            } catch (IllegalArgumentException e){

                textFieldHelper.invalidateField(e.getMessage());

                showErrorDialog("Error", "Cannot add invalid part", e.getMessage());
            }
        };

        protected Product generateProduct(){
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            int min = Integer.parseInt(minField.getText());
            int max = Integer.parseInt(maxField.getText());

            Product newProduct = createNewProduct(name, price, stock, min, max);
            newProduct.getAllAssociatedParts().addAll(productParts);

            return newProduct;
        }

        protected Product generateProduct(int id){
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            int min = Integer.parseInt(minField.getText());
            int max = Integer.parseInt(maxField.getText());

            Product product = new Product(id, name, price, stock, min, max);
            product.getAllAssociatedParts().addAll(productParts);

            return product;
        }
    }
}
