package org.wgu.c482.views;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.wgu.c482.utils.FileUtils.getURLPath;

public enum Views {
    HOME("home", "Home"),
    ADD_PART("partAddForm", "Add New Part"),
    ADD_PRODUCT("productAddForm", "Add New Product"),
    MODIFY_PART("partModifyForm","Modify Part:"),
    MODIFY_PRODUCT("productModifyForm","Modify Product:");

    private String fileName;
    private String title;

    Views(String fileName, String title){
        this.fileName = fileName;
        this.title = title;
    }

//    public URL getPath(){
//        String FXML_DIR = Paths.get("..", "..", "..", "..", "fxml").toString();
//        String FILE = this.fileName + ".fxml";
//
//        String PATH_TO_FXML_DIR = Paths.get(FXML_DIR, FILE).toString();
//
//        return getClass().getResource(PATH_TO_FXML_DIR);
//    }

    public URL getPath(){
        Path fxmlRelPath =  Paths.get("..", "..", "..", "..", "fxml");

        return getURLPath(fxmlRelPath, this.fileName + ".fxml", getClass());
    }

    public String getTitle(){
        return this.title;
    }
}
