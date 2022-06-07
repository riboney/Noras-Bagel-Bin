package org.wgu.c482.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Part;
import org.wgu.c482.models.Product;
import org.wgu.c482.views.Tables;
import org.wgu.c482.views.Views;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.wgu.c482.models.Inventory.getAllProducts;
import static org.wgu.c482.utils.FXUtils.Table.*;
import static org.wgu.c482.models.Inventory.getAllParts;
import static org.wgu.c482.utils.FXLoaderUtils.*;
import static org.wgu.c482.utils.FXUtils.TextInput.onEnterTableLookup;
import static org.wgu.c482.utils.InventoryUtils.*;
import static org.wgu.c482.views.Dialogs.showConfirmDialog;


public class Home implements Initializable {
    @FXML Button exitButton;
    @FXML TextField partSearch;
    @FXML Button partsAddButton;
    @FXML Button partsModifyButton;
    @FXML TableView<Part> partsTable;
    @FXML TableColumn<Part, Integer> partIdCol;
    @FXML TableColumn<Part, String> partNameCol;
    @FXML TableColumn<Part, Integer> partInventoryCol;
    @FXML TableColumn<Part, Double> partPriceCol;

    @FXML Button partsDeleteButton;
    @FXML TextField productSearch;
    @FXML Button productsAddButton;
    @FXML Button productsModifyButton;
    @FXML Button productsDeleteButton;
    @FXML TableView<Product> productsTable;
    @FXML TableColumn<Product, Integer> productIdCol;
    @FXML TableColumn<Product, String> productNameCol;
    @FXML TableColumn<Product, Integer> productInventoryCol;
    @FXML TableColumn<Product, Double> productPriceCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Tables.initPartTable(partsTable, Inventory.getAllParts());
        Tables.initProductTable(productsTable, Inventory.getAllProducts());
        initButtons();
        initSearchBars();
    }

    private void initButtons(){
        this.exitButton.setOnAction(event -> Platform.exit());
        this.partsAddButton.setOnAction(goToPartAddForm);
        this.partsModifyButton.setOnAction(tableButtonAction(goToPartModifyForm, partsTable));
        this.partsDeleteButton.setOnAction(tableButtonAction(confirmThenDeletePart, partsTable));

        this.productsAddButton.setOnAction(goToProductAddForm);
        this.productsModifyButton.setOnAction(tableButtonAction(goToProductModifyForm, productsTable));
        this.productsDeleteButton.setOnAction(tableButtonAction(confirmThenDeleteProduct, productsTable));
    }

    private void initSearchBars(){
        partSearch.setOnKeyPressed(onEnterTableLookup(partsTable, partSearch, PartUtils::searchPart, getAllParts()));
        productSearch.setOnKeyPressed(onEnterTableLookup(productsTable, productSearch, ProductUtils::searchProduct, getAllProducts()));
    }

    private final EventHandler<ActionEvent> goToPartAddForm = event -> switchToView(Views.ADD_PART, Views.ADD_PART.getTitle(), event);

    private final EventHandler<ActionEvent> goToProductAddForm = event -> switchToView(Views.ADD_PRODUCT, Views.ADD_PRODUCT.getTitle(), event);

    private final Function<ActionEvent, Consumer<Part>> goToPartModifyForm = event -> part -> {
        PartModifyForm controller = new PartModifyForm(part, partsTable.getSelectionModel().getSelectedIndex());
        String title = String.join(" ", Views.MODIFY_PART.getTitle(), part.getName());

        switchToView(Views.MODIFY_PART, title, event, controller);
    };

//    private final EventHandler<ActionEvent> modifyPart = event -> {
//        Optional<Part> selectedPart = getSelectedTableItem(partsTable);
//
//        Consumer<Part> switchToModifyScreen = part -> {
//            PartModifyForm controller = new PartModifyForm(part, partsTable.getSelectionModel().getSelectedIndex());
//            String title = String.join(" ", Views.MODIFY_PART.getTitle(), part.getName());
//
//            switchToView(Views.MODIFY_PART, title, event, controller);
//        };
//
//        selectedPart.ifPresentOrElse(switchToModifyScreen, tableItemNotSelectedErr);
//        event.consume();
//    };

    private final Function<ActionEvent, Consumer<Product>> goToProductModifyForm = event -> product -> {
        System.out.println(product);
        ProductModifyForm controller = new ProductModifyForm(product, productsTable.getSelectionModel().getSelectedIndex());
        String title = String.join(" ", Views.MODIFY_PRODUCT.getTitle(), product.getName());

        switchToView(Views.MODIFY_PRODUCT, title, event, controller);
    };

//    private final EventHandler<ActionEvent> modifyProduct = event -> {
//        Optional<Product> selectedProduct = getSelectedTableItem(productsTable);
//
//        Consumer<Product> switchToModifyScreen = product -> {
//            ProductModifyForm controller = new ProductModifyForm(product, productsTable.getSelectionModel().getSelectedIndex());
//            String title = String.join(" ", Views.MODIFY_PART.getTitle(), product.getName());
//
//            switchToView(Views.MODIFY_PRODUCT, title, event, controller);
//        };
//
//        Runnable showErrorMsg = () -> invalidActionDialog("Please select a part to modify from Parts table");
//
//        selectedProduct.ifPresentOrElse(switchToModifyScreen, showErrorMsg);
//        event.consume();
//    };

    private final Consumer<Part> confirmThenDeletePart = part -> {
        boolean confirmDelete = showConfirmDialog(
                "Parts Table",
                "Delete: " + part.getName(),
                "Do you want to delete this part?");

        if(confirmDelete) PartUtils.deleteFromParts(part);
    };

//    private final EventHandler<ActionEvent> deletePart = event -> {
//        Optional<Part> selectedPart = getSelectedTableItem(partsTable);
//
//        Consumer<Part> confirmThenDelete = part -> {
//            boolean confirmDelete = showConfirmDialog(
//                    "Parts Table",
//                    "Delete: " + part.getName(),
//                    "Do you want to delete this part?");
//
//            if(confirmDelete) PartUtils.deleteFromParts(part);
//        };
//
//        Runnable showErrorMessage = () -> {
//            invalidActionDialog("Please select a part to delete from Parts table");
//        };
//
//        selectedPart.ifPresentOrElse(confirmThenDelete, showErrorMessage);
//        event.consume();
//    };

    private final Consumer<Product> confirmThenDeleteProduct = product -> {
        boolean confirmDelete = showConfirmDialog(
                "Products Table",
                "Delete: " + product.getName(),
                "Do you want to delete this part?");

        if(confirmDelete) ProductUtils.deleteFromProducts(product);
    };

//    private final EventHandler<ActionEvent> deleteProduct = event -> {
//        Optional<Product> selectedProduct = getSelectedTableItem(productsTable);
//
//        Consumer<Product> confirmAndDelete = product -> {
//            boolean confirmDelete = showConfirmDialog(
//                    "Products Table",
//                    "Delete: " + product.getName(),
//                    "Do you want to delete this part?");
//
//            if(confirmDelete) ProductUtils.deleteFromProducts(product);
//        };
//
//        Runnable showErrorMessage = () -> {
//            invalidActionDialog("Please select a part to delete from Parts table");
//        };
//
//        selectedProduct.ifPresentOrElse(confirmAndDelete, showErrorMessage);
//        event.consume();
//    };

}
