package com.github.franckyi.cmpdl;

import com.github.franckyi.cmpdl.controller.*;
import com.mashape.unirest.http.Unirest;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CMPDL extends Application {

    public static final String NAME = "CMPDL";
    public static final String VERSION = "2.1.0-b1";
    public static final String AUTHOR = "Franckyi";
    public static final String TITLE = String.format("%s v%s by %s", NAME, VERSION, AUTHOR);

    public static Stage stage;

    public static ControllerView<MainWindowController> mainWindow;
    public static ControllerView<ModpackPaneController> modpackPane;
    public static ControllerView<FilePaneController> filePane;
    public static ControllerView<DestinationPaneController> destinationPane;
    public static ControllerView<ProgressPaneController> progressPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        modpackPane = new ControllerView<>("ModpackPane.fxml", EnumContent.MODPACK);
        filePane = new ControllerView<>("FilePane.fxml", EnumContent.FILE);
        destinationPane = new ControllerView<>("DestinationPane.fxml", EnumContent.DESTINATION);
        progressPane = new ControllerView<>("ProgressPane.fxml", EnumContent.PROGRESS);
        mainWindow = new ControllerView<>("MainWindow.fxml");
        stage = primaryStage;
        stage.setScene(new Scene(mainWindow.getView()));
        stage.setTitle(TITLE);
        stage.show();
    }

    public static void main(String[] args) {
        Unirest.clearDefaultHeaders();
        Unirest.setDefaultHeader("User-Agent", String.format("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0 %s/%s (%s)", NAME, VERSION, AUTHOR));
        if (args.length == 0) {
            launch(args);
        } else {
            // DO SOMETHING
        }
    }
}
