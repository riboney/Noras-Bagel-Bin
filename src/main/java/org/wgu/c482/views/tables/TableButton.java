package org.wgu.c482.views.tables;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.wgu.c482.views.Dialogs.invalidActionDialog;

public class TableButton<T> {
    private final Button button;
    private final TableView<T> table;
    private final Function<ActionEvent, Consumer<T>> onSelectedItemAction;
    private EventHandler<ActionEvent> onAction;

    public final Runnable noTableItemSelectedError = () -> invalidActionDialog("No table item selected.");

    private TableButton(Button button, TableView<T> table, Function<ActionEvent, Consumer<T>> onSelectedItemAction, EventHandler<ActionEvent> onAction) {
        this.button = button;
        this.table = table;
        this.onSelectedItemAction = onSelectedItemAction;
        this.onAction = onAction;
        initOnAction();
    }

//    public void onAction(Consumer<T> buttonAction){
//        button.setOnAction(event -> {
//            Optional<T> selectedTableItem = getSelectedTableItem();
//
//            selectedTableItem.ifPresentOrElse(buttonAction, noTableItemSelectedError);
//        });
//    }

    private void initOnAction(){
        if(this.onSelectedItemAction == null)
            button.setOnAction(onAction);
        else
            button.setOnAction(event -> {
                Optional<T> selectedTableItem = getSelectedTableItem();

                selectedTableItem.ifPresentOrElse(onSelectedItemAction.apply(event), noTableItemSelectedError);
            });
    }

    private Optional<T> getSelectedTableItem(){
        return Optional.ofNullable(table.getSelectionModel().getSelectedItem());
    }

    public static class Decorator<T>{
        private Button button;
        private TableView<T> table;
        private Function<ActionEvent, Consumer<T>> onSelectedItemAction;
        private EventHandler<ActionEvent> onAction;

        public Decorator<T> setButton(Button button){
            this.button = button;
            return this;
        }

        public Decorator<T> setTable(TableView<T> table){
            this.table = table;
            return this;
        }

        public Decorator<T> setOnSelectedItemAction(Function<ActionEvent, Consumer<T>> onSelectedItemAction){
            this.onSelectedItemAction = onSelectedItemAction;
            this.onAction = null;
            return this;
        }

        public Decorator<T> setOnAction(EventHandler<ActionEvent> onAction){
            this.onAction = onAction;
            this.onSelectedItemAction = null;
            return this;
        }

        public TableButton<T> decorate(){
            return new TableButton<T>(button, table, onSelectedItemAction, onAction);
        }
    }
}
