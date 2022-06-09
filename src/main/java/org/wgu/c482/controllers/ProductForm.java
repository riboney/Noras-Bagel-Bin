package org.wgu.c482.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Part;
import org.wgu.c482.services.PartService;
import org.wgu.c482.utils.FXUtils;
import org.wgu.c482.views.tables.PartTable;
import org.wgu.c482.views.tables.TableButton;
import org.wgu.c482.views.textfields.BaseTextField;
import org.wgu.c482.views.textfields.BaseTextFieldList;
import org.wgu.c482.views.textfields.NumericTextField;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.wgu.c482.utils.FXUtils.switchToHome;
import static org.wgu.c482.views.Dialogs.*;

public abstract class ProductForm implements Initializable {

    @FXML Label nameLabel;
    @FXML Label stockLabel;
    @FXML Label priceLabel;
    @FXML Label maxLabel;
    @FXML Label minLabel;
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

    protected ObservableList<Part> productParts = FXCollections.observableArrayList();
    protected BaseTextField name;
    protected NumericTextField stock;
    protected NumericTextField price;
    protected NumericTextField max;
    protected NumericTextField min;
    protected BaseTextFieldList allFields;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTextFields();
        initTables();
        initProductButtons();
        initTableButtons();
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
                .setQueryAlgo(PartService::searchPart)
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
                switchToHome(event);
            } catch (IllegalArgumentException e) {

                allFields.invalidateField(e.getMessage());
                showErrorDialog("Error", "Cannot add invalid product", e.getMessage());
            }
        };
        saveButton.setOnAction(saveProductAndGoHome);
        cancelButton.setOnAction(FXUtils::switchToHome);
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
}
