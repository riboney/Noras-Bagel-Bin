package org.wgu.c482.views;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.wgu.c482.utils.FileUtils.getURLPath;

public enum Scenes {
    HOME("home", "Home"),
    ADD_PART("partAddForm", "Add New Part"),
    ADD_PRODUCT("productAddForm", "Add New Product"),
    MODIFY_PART("partModifyForm","Modify Part:"),
    MODIFY_PRODUCT("productModifyForm","Modify Product:");

    private final String fileName;
    private final String title;

    Scenes(String fileName, String title){
        this.fileName = fileName;
        this.title = title;
    }

    public URL getPath(){
        Path fxmlRelPath =  Paths.get("..", "..", "..", "..", "fxml");

        return getURLPath(fxmlRelPath, this.fileName + ".fxml", getClass());
    }

    public String getTitle(){
        return this.title;
    }
}
