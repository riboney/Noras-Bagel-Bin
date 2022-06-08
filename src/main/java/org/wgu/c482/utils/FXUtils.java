package org.wgu.c482.utils;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.wgu.c482.views.Views;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.wgu.c482.utils.FXLoaderUtils.switchToView;
import static org.wgu.c482.views.Borders.defaultBorder;
import static org.wgu.c482.views.Borders.errorBorder;
import static org.wgu.c482.views.Dialogs.invalidActionDialog;

public class FXUtils {

    public static class TextInput {

        public static void onChangeResetBorder(TextField field) {
            field.textProperty().addListener(((observableValue, old, curr) -> field.setBorder(defaultBorder)));
        }

        public static <T> EventHandler<KeyEvent> onEnterTableLookup(TableView<T> table,
                                                                    TextField search,
                                                                    Function<String, ObservableList<T>> lookup,
                                                                    ObservableList<T> fallback) {
            return keyEvent -> {
                String query = search.getText().toLowerCase();
                boolean noSearchQuery = query.isBlank() || query.isEmpty();
                ObservableList<T> searchResults = lookup.apply(query);

                if(keyEvent.getCode().equals(KeyCode.ENTER)){
                    if(noSearchQuery){
                        search.setBorder(defaultBorder);
                        table.setItems(fallback);
                    } else if(searchResults.isEmpty()){
                        search.setBorder(errorBorder);
                        table.setItems(fallback);
                    } else {
                        search.setBorder(defaultBorder);
                        table.setItems(searchResults);
                    }
                }
            };
        }
    }

    public static class Button {
        public static EventHandler<ActionEvent> goHome = Button::goHomeAction;

        public static void goHomeAction(ActionEvent event){
            switchToView(Views.HOME, Views.HOME.getTitle(), event);
        }
    }
    public static class Table {

        public static <T> EventHandler<ActionEvent> tableButtonAction(Consumer<T> action, TableView<T> table){
            return event -> {
                Optional<T> selectedItem = getSelectedTableItem(table);

                selectedItem.ifPresentOrElse(action, tableItemNotSelectedErr);
                event.consume();
            };
        }

        public static <T> EventHandler<ActionEvent> tableButtonAction(Function<ActionEvent, Consumer<T>> action, TableView<T> table){
            return event -> {
                Optional<T> selectedItem = getSelectedTableItem(table);

                selectedItem.ifPresentOrElse(action.apply(event), tableItemNotSelectedErr);
                event.consume();
            };
        }

        public static <T> Optional<T> getSelectedTableItem(TableView<T> table){
            return Optional.ofNullable(table.getSelectionModel().getSelectedItem());
        }

        public final static Runnable tableItemNotSelectedErr = () -> invalidActionDialog("No table item selected.");

    }
}


//        public static void registerTableEventHandlers(Stage stage){
//            stage.addEventHandler(WindowEvent.WINDOW_SHOWING, windowEvent -> {
//                Scene currentScene = ((Stage) windowEvent.getSource()).getScene();
//                Parent parent = currentScene.getRoot();
//                currentScene.addEventFilter(MouseEvent.MOUSE_CLICKED, deselectRowItem(parent));
//            });
//        }

// src: https://stackoverflow.com/a/41908580/12369650
//        private static javafx.event.EventHandler<MouseEvent> deselectRowItem(Parent parent) {
//
//            return event -> {
//                List<TableView> tables = getAllTables(parent);
//                if(tables.isEmpty()) return;
//
//                Node currentNode = event.getPickResult().getIntersectedNode();
//
//                // move up through the node hierarchy until a TableRow or scene root is found
//                while(currentNode != null && !(currentNode instanceof TableRow)) {
//                    currentNode = currentNode.getParent();
//                }
//
//                Consumer<TableView> clearAllSelectedRows = table -> table.getSelectionModel().clearSelection();
//
//                if(!(currentNode instanceof TableRow)) {
//                    tables.forEach(clearAllSelectedRows);
//                } else {
//                    TableRow clickedRow = (TableRow) currentNode;
//                    TableView clickedTable = clickedRow.getTableView();
//
//                    if(clickedRow.isEmpty())
//                        tables.forEach(clearAllSelectedRows);
//                    else
//                        tables.stream().filter(table -> !table.equals(clickedTable)).forEach(clearAllSelectedRows);
//                }
//            };
//        }
//
//        private static List<TableView> getAllTables(Parent parent){
//            List<Node> nodes = new ArrayList<>();
//            getAllNodes(parent, nodes);
//
//            return nodes.stream()
//                    .filter(node -> node instanceof TableView)
//                    .map(node -> (TableView) node)
//                    .collect(Collectors.toList());
//        }
//
//        // src: https://stackoverflow.com/a/24986845/12369650
//        private static void getAllNodes(Parent parent, List<Node> nodes){
//            for(Node node: parent.getChildrenUnmodifiable()){
//                nodes.add(node);
//                if (node instanceof Parent)
//                    getAllNodes((Parent)node, nodes);
//            }
//        }