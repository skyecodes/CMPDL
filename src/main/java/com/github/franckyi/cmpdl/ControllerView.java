package com.github.franckyi.cmpdl;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ControllerView<T> {

    private final Parent view;
    private final T controller;
    private EnumContent enumContent;

    public ControllerView(String res) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(res));
        view = loader.load();
        controller = loader.getController();
    }

    public ControllerView(String res, EnumContent enumContent) throws IOException {
        this(res);
        this.enumContent = enumContent;
    }

    public Parent getView() {
        return view;
    }

    public T getController() {
        return controller;
    }

    public EnumContent getEnumContent() {
        return enumContent;
    }
}
