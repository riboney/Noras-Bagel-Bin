package org.wgu.c482;

import javafx.application.Application;
import javafx.stage.Stage;
import org.wgu.c482.models.InHouse;
import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Part;
import org.wgu.c482.models.Product;
import org.wgu.c482.utils.FXLoaderUtils;
import org.wgu.c482.views.BaseStage;
import org.wgu.c482.views.Views;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.wgu.c482.utils.FileUtils.getURLPath;

public class Main extends Application {
    public static Stage globalStage;

    public static void main(String[] args) {
        initData();
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

        initialParts.stream().forEach(part -> Inventory.addPart(part));
        initialProducts.stream().forEach(product -> Inventory.addProduct(product));
    }

    @Override
    public void start(Stage stage) throws Exception {

        initStage(stage);
        globalStage = stage;

//        stage.getIcons().add(new Image(iconURL.toString()));
//        FXUtils.Table.registerTableEventHandlers(stage);

        FXLoaderUtils.switchToView(Views.HOME, "Main Screen", stage);
    }

    public void initStage(Stage stage){
        Path pathToImgDir = Paths.get("..", "..", "..", "/img");
        URL iconURL = getURLPath(pathToImgDir, "wgu.jpg", getClass());

        BaseStage stageDecorator = new BaseStage(stage);
        stageDecorator.decorate(iconURL.toString());
    }
}

// NOTE: for table columns, setCol takes the name of the model getter to bind with the model attribute