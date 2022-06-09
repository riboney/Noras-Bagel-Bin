package org.wgu.c482;

import javafx.application.Application;
import javafx.stage.Stage;
import org.wgu.c482.models.InHouse;
import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Part;
import org.wgu.c482.models.Product;
import org.wgu.c482.utils.FXUtils;
import org.wgu.c482.views.Dialogs;
import org.wgu.c482.views.Stages;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.wgu.c482.utils.FileUtils.getURLPath;

/** Initializes and starts JavaFX application
 * <p><b>
 * Location to JavaDoc folder is in the root directory of the project
 * </b></p>
 *
 *  <p>
 *      <b>RUNTIME ERROR:</b> I had an error with loading the FXML files in the resources folder.
 *      I fixed it by using Java Class, URL, and Paths classes to obtain the correct, relative file paths for the FXML files
 *  </p>
 *   <p>
 *      <b>FUTURE ENHANCEMENT:</b> One feature I would add is the functionality of changing the UI theme to dark or light modes with a button
 *  </p>
 */
public class Main extends Application {
    public static int nextPartId = 1;
    public static int nextProductId = 1;
    public static void main(String[] args) {
        // initData();
        launch();
    }

    private static void initData(){
        List<Part> initialParts = List.of(
                new InHouse(1, "Brakes", 15.00,10, 1, 20, 111),
                new InHouse(2, "Wheel", 11.00,16, 1, 20, 111),
                new InHouse(3, "Seat", 15.00,10, 1, 20, 111)
        );

        List<Product> initialProducts = List.of(
                new Product('1', "Giant Bike", 299.99, 5, 1, 10),
                new Product('2', "Tricycle", 99.99, 3, 1, 10)
        );

        initialParts.forEach(Inventory::addPart);
        initialProducts.forEach(Inventory::addProduct);

        nextPartId = Inventory.getAllParts().get(Inventory.getAllParts().size() - 1).getId() + 1;
        nextProductId = Inventory.getAllProducts().get(Inventory.getAllProducts().size() - 1).getId() + 1;
    }

    @Override
    public void start(Stage stage) {

        initStage(stage);
        Dialogs.appStage = stage;

        FXUtils.switchToHome(stage);
    }

    private void initStage(Stage stage){
        Path pathToImgDir = Paths.get("..", "..", "..", "/img");
        URL iconURL = getURLPath(pathToImgDir, "wgu.jpg", getClass());

        Stages stageDecorator = new Stages(stage);
        stageDecorator.decorate(iconURL.toString());
    }
}
