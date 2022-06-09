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
import org.wgu.c482.views.tables.PartTable;
import org.wgu.c482.views.tables.TableButton;
import org.wgu.c482.views.textfields.BaseTextField;
import org.wgu.c482.views.textfields.BaseTextFieldList;
import org.wgu.c482.views.textfields.NumericTextField;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
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

    @FXML
    Label nameLabel;
    @FXML
    Label stockLabel;
    @FXML
    Label priceLabel;
    @FXML
    Label maxLabel;
    @FXML
    Label minLabel;
    @FXML
    TextField nameField;
    @FXML
    TextField stockField;
    @FXML
    TextField priceField;
    @FXML
    TextField maxField;
    @FXML
    TextField minField;

    @FXML
    TextField partSearch;
    @FXML
    TableView<Part> partsTable;
    @FXML
    TableView<Part> productPartsTable;

    @FXML
    Button addPartButton;
    @FXML
    Button removePartButton;
    @FXML
    Button saveButton;
    @FXML
    Button cancelButton;

//    protected List<TextField> allTextFields;
//    private TextFieldHelper textFieldHelper;
//    protected ProductHelper productHelper;
    protected ObservableList<Part> productParts = FXCollections.observableArrayList();

    protected BaseTextField name;
    protected NumericTextField stock;
    protected NumericTextField price;
    protected NumericTextField max;
    protected NumericTextField min;
    protected BaseTextFieldList allFields;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        this.allTextFields = List.of(nameField, stockField, priceField, maxField, minField);
//        textFieldHelper = new TextFieldHelper(this.allTextFields);
//        textFieldHelper.initTextFields();
//
//        productHelper = new ProductHelper();
//
//        Tables.initPartTable(partsTable, Inventory.getAllParts());
//        Tables.initPartTable(productPartsTable, productParts);

        initTextFields();
        initTables();
        initProductButtons();
        initTableButtons();
//        initSearchBar();
    }

    public void initTextFields() {
        name = new BaseTextField(nameLabel.getText(), nameField);
        stock = new NumericTextField(stockLabel.getText(), stockField);
        price = new NumericTextField(priceLabel.getText(), priceField);
        max = new NumericTextField(maxLabel.getText(), maxField);
        min = new NumericTextField(minLabel.getText(), minField);

        ArrayList<BaseTextField> list = new ArrayList<>(Arrays.asList(name, stock, price, max, min));

        allFields = new BaseTextFieldList(list);
    }

    private void initTables(){
        new PartTable.Decorator()
                .setTable(partsTable)
                .setTableItems(Inventory.getAllParts())
                .setSearchField(partSearch)
                .setQueryAlgo(InventoryUtils.PartUtils::searchPart)
                .decorate();

        new PartTable.Decorator()
                .setTable(productPartsTable)
                .setTableItems(productParts)
                .decorate();
    }

    private void initProductButtons() {
        EventHandler<ActionEvent> saveProductAndGoHome = event -> {
            try {
                allFields.validateFields();
                Map<String, Object> productDTO = generateProductDTO();
                formAction(productDTO);
                goHomeAction(event);
            } catch (IllegalArgumentException e) {

                allFields.invalidateField(e.getMessage());
                showErrorDialog("Error", "Cannot add invalid product", e.getMessage());
            }
        };
        saveButton.setOnAction(saveProductAndGoHome);
        cancelButton.setOnAction(goHome);
    }

    private void initTableButtons() {
        Function<ActionEvent, Consumer<Part>> addToProductParts = event -> part -> productParts.add(part);
        new TableButton.Decorator<Part>()
                .setButton(addPartButton)
                .setTable(partsTable)
                .setOnSelectedItemAction(addToProductParts)
                .decorate();

        Function<ActionEvent, Consumer<Part>> confirmThenRemovePart = event -> part -> {
            String title = "Product parts";
            String header = "Removing: " + part.getName();
            String content = "Do you want to remove this part from the Product?";
            boolean removePart = showConfirmDialog(title, header, content);
            if (removePart) productParts.remove(part);
        };
        new TableButton.Decorator<Part>()
                .setButton(removePartButton)
                .setTable(productPartsTable)
                .setOnSelectedItemAction(confirmThenRemovePart)
                .decorate();
    }


//    private void initSearchBar() {
//        partSearch.setOnKeyPressed(onEnterTableLookup(partsTable, partSearch, InventoryUtils.PartUtils::searchPart, getAllParts()));
//
//    }

//    private final EventHandler<ActionEvent> saveProductAndGoHome = event -> {
//        try {
//            allFields.validateFields();
//            Map<String, Object> productDTO = generateProductDTO();
//            formAction(productDTO);
//            goHomeAction(event);
//        } catch (IllegalArgumentException e){
//
//            allFields.invalidateField(e.getMessage());
//            showErrorDialog("Error", "Cannot add invalid product", e.getMessage());
//        }
//    };

    protected Map<String, Object> generateProductDTO() {
        Map<String, Object> productDTO = new HashMap<>();

        productDTO.put(nameLabel.getText(), nameField.getText());
        productDTO.put(priceLabel.getText(), Double.parseDouble(priceField.getText()));
        productDTO.put(stockLabel.getText(), Integer.parseInt(stockField.getText()));
        productDTO.put(minLabel.getText(), Integer.parseInt(minField.getText()));
        productDTO.put(maxLabel.getText(), Integer.parseInt(maxField.getText()));

        return productDTO;
    }

    protected abstract void formAction(Map<String, Object> productDTO);

//    private class TextFieldHelper {
//        private final List<TextField> textFields;
//
//        private TextFieldHelper(List<TextField> textFields) {
//            this.textFields = textFields;
//        }
//
//        public void resetFieldBorders(){
//            this.textFields.forEach(textField -> textField.setBorder(defaultBorder));
//        }
//
//        public void initTextFields(){
//            this.textFields.forEach(FXUtils.TextInput::onChangeResetBorder);
//        }
//
//        public void invalidateFields(){
//            Consumer<TextField> emptyFieldsInvalidator = textField -> {
//                if(textField.getText().isBlank() || textField.getText().isEmpty()){
//                    textField.setBorder(errorBorder);
//                }
//            };
//
//            Predicate<TextField> numericalFieldsFilter = textField -> !textField.equals(nameField);
//
//            Consumer<TextField> nonDigitInvalidator  = textField -> {
//                if(!textField.getText().matches("^\\d*\\.?\\d*$"))
//                    textField.setBorder(errorBorder);
//            };
//
//            this.textFields.stream()
//                    .peek(emptyFieldsInvalidator)
//                    .filter(numericalFieldsFilter)
//                    .forEach(nonDigitInvalidator);
//        }
//
//        public void invalidateField(String errorMsg){
//            String target = errorMsg.split(" ")[0].toLowerCase();
//            this.textFields.stream()
//                    .filter(textField -> textField.getId().startsWith(target))
//                    .findFirst().ifPresent(textField -> textField.setBorder(errorBorder));
//        }
//
//        public void verifyFieldsHaveValue(){
//            this.textFields.forEach(textField -> {
//                if(textField.getText().isEmpty() || textField.getText().isBlank()){
//                    throw new IllegalArgumentException("One or more fields are blank or empty!");
//                }
//            });
//        }
//
//        public void verifyNumericalFields(){
//            try {
//                Double.parseDouble(priceField.getText());
//                Integer.parseInt(stockField.getText());
//                Integer.parseInt(minField.getText());
//                Integer.parseInt(maxField.getText());
//
//            } catch (NumberFormatException e) {
//                throw new IllegalArgumentException("Value must be numerical!");
//            }
//        }
//    }

//    protected class ProductHelper {

//        private final Consumer<Part> addToProductParts = part -> productParts.add(part);
//        private final EventHandler<ActionEvent> modifyProductParts = event -> {
//          Optional<Part> selectedPart = getSelectedTableItem(partsTable);
//          Consumer<Part> addToProductParts = part -> productParts.add(part);
//          Runnable showErrorMsg = () -> invalidActionDialog("Please select a part to add to Product's parts");
//
//          selectedPart.ifPresentOrElse(addToProductParts, showErrorMsg);
//          event.consume();
//        };

//        private final Consumer<Part> confirmThenRemoveProductPart = part -> {
//            boolean removePart = showConfirmDialog("Product parts", "Removing: " + part.getName(), "Do you want to remove this part from the Product?");
//            if (removePart) productParts.remove(part);
//        };

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

//        private final EventHandler<ActionEvent> saveProductAndGoHome = event -> {
//            try {
//                allFields.validateFields();
//                Map<String, Object> productDTO = generateProductDTO();
//                formAction();
//                goHomeAction(event);
//            } catch (IllegalArgumentException e){
//
//                allFields.invalidateField(e.getMessage());
//                showErrorDialog("Error", "Cannot add invalid product", e.getMessage());
//            }
//        };
//
//        protected Map<String, Object> generateProductDTO(){
//            Map<String, Object> productDTO = new HashMap<>();
//
//            productDTO.put(nameLabel.getText(), nameField.getText());
//            productDTO.put(priceLabel.getText(), Double.parseDouble(priceField.getText()));
//            productDTO.put(stockLabel.getText(), Integer.parseInt(stockField.getText()));
//            productDTO.put(minLabel.getText(), Integer.parseInt(minField.getText()));
//            productDTO.put(maxLabel.getText(), Integer.parseInt(maxField.getText()));
//
//            return productDTO;
//        }
//
//        protected Product generateProduct(){
//            String name = nameField.getText();
//            double price = Double.parseDouble(priceField.getText());
//            int stock = Integer.parseInt(stockField.getText());
//            int min = Integer.parseInt(minField.getText());
//            int max = Integer.parseInt(maxField.getText());
//
//            Product newProduct = createNewProduct(name, price, stock, min, max);
//            newProduct.getAllAssociatedParts().addAll(productParts);
//
//            return newProduct;
//        }
//
//        protected Product generateProduct(int id){
//            String name = nameField.getText();
//            double price = Double.parseDouble(priceField.getText());
//            int stock = Integer.parseInt(stockField.getText());
//            int min = Integer.parseInt(minField.getText());
//            int max = Integer.parseInt(maxField.getText());
//
//            Product product = new Product(id, name, price, stock, min, max);
//            product.getAllAssociatedParts().addAll(productParts);
//
//            return product;
//        }
//    }
}
