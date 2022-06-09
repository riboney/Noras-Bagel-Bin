package org.wgu.c482.views.tables;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.wgu.c482.models.Part;
import org.wgu.c482.views.textfields.BaseTextField;

import java.util.function.Function;

import static org.wgu.c482.utils.InvUtils.isInteger;

public abstract class BaseTable<T> {
    protected final TableView<T> table;
    protected final ObservableList<T> tableItems;
    protected BaseTextField tableSearch;
    protected BaseTable(TableView<T> table, ObservableList<T> tableItems, TextField searchField, Function<String, ObservableList<T>> queryAlgo) {
        this.table = table;
        this.tableItems = tableItems;
        table.setItems(tableItems);
        initTableColumns();

        if(searchField != null){
            this.tableSearch = new BaseTextField("Table search", searchField);
            initSearchOperation(queryAlgo);
        }
    }

    public TableView<T> getTable(){
        return table;
    }
    protected abstract void initTableColumns();

    protected void initSearchOperation(Function<String, ObservableList<T>> queryAlgo){
        EventHandler<KeyEvent> populateWithSearchResultsOnEnter = keyEvent -> {
            String query = tableSearch.getTextField().getText().toLowerCase();
            boolean queryIsEmpty = query.isEmpty() || query.isBlank();
            ObservableList<T> queryResults = queryAlgo.apply(query);

            if(keyEvent.getCode().equals(KeyCode.ENTER)){
                if(queryIsEmpty)
                    resetSearch();
                else if(isInteger(query) && !queryResults.isEmpty())
                    highlightSearch(queryResults.get(0));
                else if(!queryResults.isEmpty())
                    displaySearch(queryResults);
                else
                    failSearch();
            }
        };

        tableSearch.getTextField().setOnKeyPressed(populateWithSearchResultsOnEnter);
    }

    private void highlightSearch(T idMatchedItem){
        tableSearch.isInvalid(false);
        table.setItems(tableItems);
        table.getSelectionModel().select(idMatchedItem);
        int selectedRowIndex = table.getSelectionModel().getSelectedIndex();
        table.scrollTo(selectedRowIndex);
    }

    private void resetSearch(){
        tableSearch.isInvalid(false);
        table.setItems(tableItems);
    }

    private void failSearch(){
        tableSearch.isInvalid(true);
        table.setItems(tableItems);
    }

    private void displaySearch(ObservableList<T> results){
        tableSearch.isInvalid(false);
        table.setItems(results);
    }

    public static class Decorator<T> {
        protected TableView<T> table;
        protected ObservableList<T> tableItems;
        protected TextField searchField;
        protected Function<String, ObservableList<T>> queryAlgo;

        public Decorator<T> setTable(TableView<T> table) {
            this.table = table;
            return this;
        }

        public Decorator<T> setTableItems(ObservableList<T> tableItems) {
            this.tableItems = tableItems;
            return this;
        }

        public Decorator<T> setSearchField(TextField searchField) {
            this.searchField = searchField;
            return this;
        }

        public Decorator<T> setQueryAlgo(Function<String, ObservableList<T>> queryAlgo) {
            this.queryAlgo = queryAlgo;
            return this;
        }

        public BaseTable<T> decorate() {
           return null;
        }
    }
}
