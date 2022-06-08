package org.wgu.c482.views.tables;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.wgu.c482.models.Product;

import java.text.NumberFormat;

public class ProductTableInitializer implements TableInitializer<Product> {

    @Override
    public void initialize(TableView<Product> table, ObservableList<Product> products) {
        table.setItems(products);

        var tableCols = table.getColumns();

        tableCols.get(0).setCellValueFactory(product -> new ReadOnlyObjectWrapper(product.getValue().getId()));
        tableCols.get(1).setCellValueFactory(product -> new ReadOnlyObjectWrapper(product.getValue().getName()));
        tableCols.get(2).setCellValueFactory(product -> new ReadOnlyObjectWrapper(product.getValue().getStock()));
        tableCols.get(3).setCellValueFactory(product -> new ReadOnlyObjectWrapper(product.getValue().getPrice()));
        ((TableColumn<Product,Double>) tableCols.get(3)).setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(price == null ? "" : NumberFormat.getCurrencyInstance().format(price));
            }
        });
    }
}
