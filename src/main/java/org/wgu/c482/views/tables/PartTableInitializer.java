package org.wgu.c482.views.tables;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.wgu.c482.models.Part;

import java.text.NumberFormat;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class PartTableInitializer implements TableInitializer<Part> {

    @Override
    public void initialize(TableView<Part> table, ObservableList<Part> parts) {
        table.setItems(parts);

        var tableCols = table.getColumns();

        tableCols.get(0).setCellValueFactory(part -> new ReadOnlyObjectWrapper(part.getValue().getId()));
        tableCols.get(1).setCellValueFactory(part -> new ReadOnlyObjectWrapper(part.getValue().getName()));
        tableCols.get(2).setCellValueFactory(part -> new ReadOnlyObjectWrapper(part.getValue().getStock()));
        tableCols.get(3).setCellValueFactory(part -> new ReadOnlyObjectWrapper(part.getValue().getPrice()));
        ((TableColumn<Part,Double>) tableCols.get(3)).setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(price == null ? "" : NumberFormat.getCurrencyInstance().format(price));
            }
        });
    }
}
