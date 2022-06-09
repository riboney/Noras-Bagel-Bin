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

import static org.wgu.c482.services.PartService.deleteFromParts;
import static org.wgu.c482.services.ProductService.deleteFromProducts;
import static org.wgu.c482.utils.FXUtils.*;
import static org.wgu.c482.views.Dialogs.showConfirmDialog;


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
        EventHandler<ActionEvent> addPart = event -> switchToView(Scenes.ADD_PART, Scenes.ADD_PART.getTitle(), event);
        new TableButton.Decorator<Part>()
                .setButton(partsAddButton)
                .setTable(partsTable)
                .setOnAction(addPart)
                .decorate();

        Function<ActionEvent, Consumer<Part>> modifyPart = event -> part -> {
            PartModifyForm controller = new PartModifyForm(part, partsTable.getSelectionModel().getSelectedIndex());
            String title = String.join(" ", Scenes.MODIFY_PART.getTitle(), part.getName());
            switchToView(Scenes.MODIFY_PART, title, event, controller);
        };
        new TableButton.Decorator<Part>()
                .setButton(partsModifyButton)
                .setTable(partsTable)
                .setOnSelectedItemAction(modifyPart)
                .decorate();

        Function<ActionEvent, Consumer<Part>> deletePart = event -> part -> {
            String title = "Parts Table";
            String header = "Delete: " + part.getName();
            String content = "Do you want to delete this part?";
            boolean confirmDelete = showConfirmDialog(title, header, content);
            if(confirmDelete) deleteFromParts(part);
        };
        new TableButton.Decorator<Part>()
                .setButton(partsDeleteButton)
                .setTable(partsTable)
                .setOnSelectedItemAction(deletePart)
                .decorate();
    }

    private void initProductTableButtons(){
        EventHandler<ActionEvent> addProduct = event -> switchToView(Scenes.ADD_PRODUCT, Scenes.ADD_PRODUCT.getTitle(), event);
        new TableButton.Decorator<Product>()
                .setButton(productsAddButton)
                .setTable(productsTable)
                .setOnAction(addProduct)
                .decorate();

        Function<ActionEvent, Consumer<Product>> modifyProduct = event -> product -> {
            ProductModifyForm controller = new ProductModifyForm(product, productsTable.getSelectionModel().getSelectedIndex());
            String title = String.join(" ", Scenes.MODIFY_PRODUCT.getTitle(), product.getName());
            switchToView(Scenes.MODIFY_PRODUCT, title, event, controller);
        };
        new TableButton.Decorator<Product>()
                .setButton(productsModifyButton)
                .setTable(productsTable)
                .setOnSelectedItemAction(modifyProduct)
                .decorate();

        Function<ActionEvent, Consumer<Product>> deleteProduct = event -> product -> {
            String title = "Parts Table";
            String header = "Delete: " + product.getName();
            String content = "Do you want to delete this part?";
            boolean confirmDelete = showConfirmDialog(title, header, content);
            if(confirmDelete) deleteFromProducts(product);
        };
        new TableButton.Decorator<Product>()
                .setButton(productsDeleteButton)
                .setTable(productsTable)
                .setOnSelectedItemAction(deleteProduct)
                .decorate();

    }
}
