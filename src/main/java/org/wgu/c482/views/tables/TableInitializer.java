package org.wgu.c482.views.tables;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public interface TableInitializer<T> {

    void initialize(TableView<T> table, ObservableList<T> items);
}
