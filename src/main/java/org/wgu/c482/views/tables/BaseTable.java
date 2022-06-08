package org.wgu.c482.views.tables;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.wgu.c482.views.textfields.BaseTextField;

import java.util.List;
import java.util.function.Function;

public abstract class BaseTable<T> {
    protected final TableView<T> table;
    protected final ObservableList<T> tableItems;
    protected final BaseTextField tableSearch;
    protected BaseTable(TableView<T> table, ObservableList<T> tableItems, TextField searchField, Function<String, ObservableList<T>> queryAlgo) {
        this.table = table;
        this.tableItems = tableItems;
        table.setItems(tableItems);
        initTableColumns();

        this.tableSearch = new BaseTextField("Table search", searchField);
        setSearchAlgo(queryAlgo);
    }

    public TableView<T> getTable(){
        return table;
    }
    protected abstract void initTableColumns();

    protected void setSearchAlgo(Function<String, ObservableList<T>> queryAlgo){
        EventHandler<KeyEvent> populateWithSearchResultsOnEnter = keyEvent -> {
            String query = tableSearch.getTextField().getText().toLowerCase();
            boolean queryIsEmpty = query.isEmpty() || query.isBlank();
            ObservableList<T> queryResults = queryAlgo.apply(query);

            if(keyEvent.getCode().equals(KeyCode.ENTER)){
                if(queryIsEmpty)
                    resetSearch();
                else if(queryResults.isEmpty())
                    failSearch();
                else
                    displaySearch(queryResults);
            }
        };

        tableSearch.getTextField().setOnKeyPressed(populateWithSearchResultsOnEnter);
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
}
