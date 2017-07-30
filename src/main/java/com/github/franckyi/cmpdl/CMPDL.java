package com.github.franckyi.cmpdl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class CMPDL extends Application {

    private static final String NAME = "Curse Modpack Downloader";
    private static final String VERSION = "2.0.0-b3";
    private static final String AUTHOR = "Franckyi (original version by Vazkii)";

    public static String title() {
        return String.format("%s v%s by %s", NAME, VERSION, AUTHOR);
    }

    public static InterfaceController controller;
    public static Parent parent;
    public static Stage stage;

    public static String path;
    public static String zipFileName;

    public static final Set<Exception> exceptions = new HashSet<>();

    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        URL interface0 = getClass().getClassLoader().getResource("interface.fxml");
        if (interface0 != null) {
            FXMLLoader loader = new FXMLLoader(interface0);
            parent = loader.load();
            controller = loader.getController();
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.setTitle(title());
            stage.show();
        } else {
            throw new RuntimeException("Impossible to load interface.fxml file : the application can't start");
        }
    }

    @Override
    public void stop() throws Exception {
        controller.stop(null);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static String getTempDirectory() {
        return path + File.separator + ".cmpdl_temp";
    }

    public static String getZipFile() {
        return getTempDirectory() + File.separator + zipFileName;
    }

    private static String getMinecraftDirectory() {
        return path + File.separator + "minecraft";
    }

    public static String getModsDirectory() {
        return getMinecraftDirectory() + File.separator + "mods";
    }

    public static String getInstanceFile() {
        return path + File.separator + "instance.cfg";
    }

    public static String getManifestFile() {
        return getTempDirectory() + File.separator + "manifest.json";
    }

    public static String toOverridePath(File file, String override) {
        return file.getPath().replace(".cmpdl_temp" + File.separator + override, "minecraft");
    }
}
