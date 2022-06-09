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
import org.wgu.c482.services.PartService;
import org.wgu.c482.services.ProductService;
import org.wgu.c482.views.Scenes;
import org.wgu.c482.views.tables.PartTable;
import org.wgu.c482.views.tables.ProductTable;
import org.wgu.c482.views.tables.TableButton;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.wgu.c482.utils.FXUtils.*;
import static org.wgu.c482.views.Dialogs.showConfirmDialog;
import static org.wgu.c482.views.Dialogs.showErrorDialog;


public class Home implements Initializable {
    @FXML Button exitButton;
    @FXML TextField partSearch;
    @FXML Button partsAddButton;
    @FXML Button partsModifyButton;
    @FXML Button partsDeleteButton;
    @FXML TableView<Part> partsTable;
    @FXML TableColumn<Part, Integer> partIdCol;
    @FXML TableColumn<Part, String> partNameCol;
    @FXML TableColumn<Part, Integer> partInventoryCol;
    @FXML TableColumn<Part, Double> partPriceCol;

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
        initTables();
        this.exitButton.setOnAction(event -> Platform.exit());
        initPartTableButtons();
        initProductTableButtons();
    }

    private void initTables(){
        new PartTable.Decorator()
                .setTable(partsTable)
                .setTableItems(Inventory.getAllParts())
                .setSearchField(partSearch)
                .setQueryAlgo(PartService::searchPart)
                .decorate();

        new ProductTable.Decorator()
                .setTable(productsTable)
                .setTableItems(Inventory.getAllProducts())
                .setSearchField(productSearch)
                .setQueryAlgo(ProductService::searchProduct)
                .decorate();
    }

    private void initPartTableButtons(){
        EventHandler<ActionEvent> addPartAction = event -> switchToView(Scenes.ADD_PART, Scenes.ADD_PART.getTitle(), event);
        var addPart = new TableButton.Decorator<Part>()
                .setButton(partsAddButton)
                .setTable(partsTable)
                .setOnAction(addPartAction)
                .decorate();

        Function<ActionEvent, Consumer<Part>> modifyPartAction = event -> part -> {
            PartModifyForm controller = new PartModifyForm(part, partsTable.getSelectionModel().getSelectedIndex());
            String title = String.join(" ", Scenes.MODIFY_PART.getTitle(), part.getName());
            switchToView(Scenes.MODIFY_PART, title, event, controller);
        };
        var modifyPart = new TableButton.Decorator<Part>()
                .setButton(partsModifyButton)
                .setTable(partsTable)
                .setOnSelectedItemAction(modifyPartAction)
                .decorate();

        Function<ActionEvent, Consumer<Part>> deletePartAction = event -> part -> {
            String title = "Parts Table";
            String header = "Delete: " + part.getName();
            String content = "Do you want to delete this part?";
            boolean confirmDelete = showConfirmDialog(title, header, content);
            if(confirmDelete) Inventory.deletePart(part);
        };
        var deletePart = new TableButton.Decorator<Part>()
                .setButton(partsDeleteButton)
                .setTable(partsTable)
                .setOnSelectedItemAction(deletePartAction)
                .decorate();
    }

    private void initProductTableButtons(){
        EventHandler<ActionEvent> addProductAction = event -> switchToView(Scenes.ADD_PRODUCT, Scenes.ADD_PRODUCT.getTitle(), event);
        var addProduct = new TableButton.Decorator<Product>()
                .setButton(productsAddButton)
                .setTable(productsTable)
                .setOnAction(addProductAction)
                .decorate();

        Function<ActionEvent, Consumer<Product>> modifyProductAction = event -> product -> {
            ProductModifyForm controller = new ProductModifyForm(product, productsTable.getSelectionModel().getSelectedIndex());
            String title = String.join(" ", Scenes.MODIFY_PRODUCT.getTitle(), product.getName());
            switchToView(Scenes.MODIFY_PRODUCT, title, event, controller);
        };
        var modifyProduct =  new TableButton.Decorator<Product>()
                .setButton(productsModifyButton)
                .setTable(productsTable)
                .setOnSelectedItemAction(modifyProductAction)
                .decorate();

        Function<ActionEvent, Consumer<Product>> deleteProductAction = event -> product -> {
            String title = "Parts Table";
            String header = "Delete: " + product.getName();
            String content = "Do you want to delete this part?";
            boolean confirmDelete = showConfirmDialog(title, header, content);

            if(confirmDelete) {
                if(product.getAllAssociatedParts().isEmpty())
                    Inventory.deleteProduct(product);
                else{
                    title = "Error!";
                    header = "Can not delete product " + product.getName();
                    content = product.getName() + " has associated parts! Please remove these parts before deleting the product";
                    showErrorDialog(title, header, content);
                }
            }
        };
        var deleteProduct = new TableButton.Decorator<Product>()
                .setButton(productsDeleteButton)
                .setTable(productsTable)
                .setOnSelectedItemAction(deleteProductAction)
                .decorate();
    }
}
