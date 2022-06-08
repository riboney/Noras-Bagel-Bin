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

    public static class Decorator {
        private TableView<Part> table;
        private ObservableList<Part> tableItems;
        private TextField searchField;
        private Function<String, ObservableList<Part>> queryAlgo;

        public Decorator setTable(TableView<Part> table) {
            this.table = table;
            return this;
        }

        public Decorator setTableItems(ObservableList<Part> tableItems) {
            this.tableItems = tableItems;
            return this;
        }

        public Decorator setSearchField(TextField searchField) {
            this.searchField = searchField;
            return this;
        }

        public Decorator setQueryAlgo(Function<String, ObservableList<Part>> queryAlgo) {
            this.queryAlgo = queryAlgo;
            return this;
        }

        public PartTable decorate() {
            return new PartTable(table, tableItems, searchField, queryAlgo);
        }
    }
}
