package org.wgu.c482.views;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;

public class Borders {

    public static Border errorBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(2)));
    public static Border defaultBorder = new Border(new BorderStroke(null, null, null, null));
}
