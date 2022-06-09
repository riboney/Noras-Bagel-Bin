package org.wgu.c482.views.tables;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.wgu.c482.models.Part;

import java.text.NumberFormat;
import java.util.function.Function;

public class PartTable extends BaseTable<Part> {

    private PartTable(TableView<Part> table, ObservableList<Part> tableItems, TextField searchField, Function<String, ObservableList<Part>> queryAlgo) {
        super(table, tableItems, searchField, queryAlgo);
    }

    @Override
    public void initTableColumns() {
        ObservableList<TableColumn<Part, ?>> tableCols = this.table.getColumns();

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

    public static class Decorator extends BaseTable.Decorator<Part>{
        @Override
        public BaseTable<Part> decorate(){
            return new PartTable(table, tableItems, searchField, queryAlgo);
        }
    }
}
