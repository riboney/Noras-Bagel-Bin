package org.wgu.c482.utils;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Helper methods for file operations*/
public class FileUtils {
    public static URL getURLPath(Path relPathToDir, String file, Class relClass){
        String relPath = Paths.get(relPathToDir.toString(), file).toString();

        return relClass.getResource(relPath);
    }
}
