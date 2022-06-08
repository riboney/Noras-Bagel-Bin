package org.wgu.c482.views;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BaseStage {
    private final Stage stage;

    public BaseStage(Stage stage) {
        this.stage = stage;
    }

    public void decorate(String iconURL){
        stage.getIcons().add(new Image(iconURL));

        EventHandler<WindowEvent> initSceneOnNewWindow = windowEvent -> {
            Scene currentScene = ((Stage) windowEvent.getSource()).getScene();
            Parent parent = currentScene.getRoot();
            currentScene.addEventFilter(MouseEvent.MOUSE_CLICKED, deselectTableItemOnClick(parent));
        };

        stage.addEventHandler(WindowEvent.WINDOW_SHOWING, initSceneOnNewWindow);
    }

    // src: https://stackoverflow.com/a/41908580/12369650
    private static javafx.event.EventHandler<MouseEvent> deselectTableItemOnClick(Parent parent) {

        return event -> {
            List<TableView> tables = getCurrentSceneTables(parent);
            if(tables.isEmpty()) return;

            Node currentNode = event.getPickResult().getIntersectedNode();

            // move up through the node hierarchy until a TableRow or scene root is found
            while(currentNode != null && !(currentNode instanceof TableRow)) {
                currentNode = currentNode.getParent();
            }

            Consumer<TableView> clearAllSelectedRows = table -> table.getSelectionModel().clearSelection();

            if(!(currentNode instanceof TableRow)) {
                tables.forEach(clearAllSelectedRows);
            } else {
                TableRow clickedRow = (TableRow) currentNode;
                TableView clickedTable = clickedRow.getTableView();

                if(clickedRow.isEmpty())
                    tables.forEach(clearAllSelectedRows);
                else
                    tables.stream().filter(table -> !table.equals(clickedTable)).forEach(clearAllSelectedRows);
            }
        };
    }

    private static List<TableView> getCurrentSceneTables(Parent parent){
        List<Node> nodes = new ArrayList<>();
        getCurrentSceneNodes(parent, nodes);

        return nodes.stream()
                .filter(node -> node instanceof TableView)
                .map(node -> (TableView) node)
                .collect(Collectors.toList());
    }

    // src: https://stackoverflow.com/a/24986845/12369650
    private static void getCurrentSceneNodes(Parent parent, List<Node> nodes){
        for(Node node: parent.getChildrenUnmodifiable()){
            nodes.add(node);
            if (node instanceof Parent)
                getCurrentSceneNodes((Parent)node, nodes);
        }
    }
}
