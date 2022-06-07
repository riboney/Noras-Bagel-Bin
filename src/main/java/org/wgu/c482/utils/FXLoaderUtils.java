package org.wgu.c482.utils;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.wgu.c482.views.Views;

import java.io.IOException;

public class FXLoaderUtils {
    private static final double WIDTH = 1200;
    private static final double LENGTH = 500;

    public static void switchToView(Views view, String title, Stage stage) {
        createScene(stage, title, getFXTree(view), WIDTH, LENGTH);
    }

    public static void switchToView(Views view, String title, ActionEvent actionEvent) {
        Stage stage = eventGetStage(actionEvent);
        Parent tree = getFXTree(view);

        createScene(stage, title, tree, tree.prefWidth(WIDTH), tree.prefHeight(LENGTH));
    }

    public static void switchToView(Views view, String title, ActionEvent actionEvent, Initializable controller) {
        Stage stage = eventGetStage(actionEvent);
        Parent tree = getFXTree(view, controller);

        createScene(stage, title, tree, tree.prefWidth(WIDTH), tree.prefHeight(LENGTH));
    }

    private static void createScene(Stage stage, String title, Parent tree, double width, double length) {
        stage.setTitle(title);
        stage.setScene(new Scene(tree, width, length));
        stage.show();
    }

    private static Parent getFXTree(Views fileName) {
        Parent parent = null;

        try {
            parent = FXMLLoader.load(fileName.getPath());
        } catch (IOException e) {
            System.out.println("Error loading FXML file!");
            e.printStackTrace();
            Platform.exit();
        }

        return parent;
    }

    private static Parent getFXTree(Views fileName, Initializable controller) {
        Parent parent = null;

        try {
            FXMLLoader loader = new FXMLLoader(fileName.getPath());
            loader.setController(controller);
            parent = loader.load();
        } catch (IOException e) {
            System.out.println("Error loading FXML file!");
            e.printStackTrace();
            Platform.exit();
        }

        return parent;
    }

//    private static Parent getFXTree(View fileName, Initializable controller) {
//        Parent parent = null;
//
//        try {
//            FXMLLoader loader = new FXMLLoader();
//            loader.setController(controller);
//            parent = loader.load(fileName.getPath());
//
//        } catch (IOException e) {
//            System.out.println("Error loading FXML file!");
//            e.printStackTrace();
//            Platform.exit();
//        }
//
//        return parent;
//    }

    private static Stage eventGetStage(ActionEvent event){
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}


