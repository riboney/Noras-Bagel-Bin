package org.wgu.c482.views;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/** Customized dialog Boxes for application */
public class Dialogs {

    public static Stage appStage;
    public static boolean showConfirmDialog(String title, String header, String content) {
        Alert alert = newDialog(Alert.AlertType.CONFIRMATION, title, header, content);

        return alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .isPresent();
    }

    public static void invalidActionDialog(String content) {
        Alert alert = newDialog(Alert.AlertType.INFORMATION, "System Message", "Cannot perform action", content);

        alert.showAndWait();
    }

    public static void showErrorDialog(String title, String header, String content) {
        Alert alert = newDialog(Alert.AlertType.ERROR, title, header, content);

        alert.showAndWait();
    }

    private static Alert newDialog(Alert.AlertType alertType, String title, String header, String content){
        Alert alert = new Alert(alertType);

        alert.initOwner(appStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        return alert;
    }
}
