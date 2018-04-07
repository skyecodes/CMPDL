package com.github.franckyi.cmpdl.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ControllerView<T> {

    private final Parent view;
    private final T controller;

    public ControllerView(String res) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(res));
        view = loader.load();
        controller = loader.getController();
    }

    public Parent getView() {
        return view;
    }

    public T getController() {
        return controller;
    }

}
