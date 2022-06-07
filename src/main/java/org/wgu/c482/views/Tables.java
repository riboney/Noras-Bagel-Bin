package org.wgu.c482.views;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.wgu.c482.models.Part;
import org.wgu.c482.models.Product;

import java.text.NumberFormat;

public class Tables {
    public static void initPartTable(TableView<Part> table, ObservableList<Part> parts){
        table.setItems(parts);

        ObservableList<TableColumn<Part, ?>> columns = table.getColumns();
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("stock"));
        columns.get(3).setCellValueFactory(new PropertyValueFactory<>("price"));
        ((TableColumn<Part,Double>) columns.get(3)).setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(price == null ? "" : NumberFormat.getCurrencyInstance().format(price));
            }
        });
    }

    public static void initProductTable(TableView<Product> table, ObservableList<Product> products){
        table.setItems(products);

        ObservableList<TableColumn<Product, ?>> columns = table.getColumns();
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("stock"));
        columns.get(3).setCellValueFactory(new PropertyValueFactory<>("price"));
        ((TableColumn<Product,Double>) columns.get(3)).setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(price == null ? "" : NumberFormat.getCurrencyInstance().format(price));
            }
        });
    }
}
