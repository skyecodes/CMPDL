package com.github.franckyi.cmpdl;

import com.github.franckyi.cmpdl.controller.*;
import com.github.franckyi.cmpdl.core.ContentControllerView;
import com.github.franckyi.cmpdl.core.ControllerView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CMPDL extends Application {

    public static final String NAME = "CMPDL";
    public static final String VERSION = "2.1.0";
    public static final String AUTHOR = "Franckyi";
    public static final String TITLE = String.format("%s v%s by %s", NAME, VERSION, AUTHOR);

    public static final String USER_AGENT = String.format("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0 %s/%s (%s)", NAME, VERSION, AUTHOR);

    public static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public static Stage stage;

    public static ControllerView<MainWindowController> mainWindow;
    public static ContentControllerView<ModpackPaneController> modpackPane;
    public static ContentControllerView<FilePaneController> filePane;
    public static ContentControllerView<DestinationPaneController> destinationPane;
    public static ContentControllerView<ProgressPaneController> progressPane;

    public static ContentControllerView<?> currentContent;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        modpackPane = new ContentControllerView<>("ModpackPane.fxml");
        filePane = new ContentControllerView<>("FilePane.fxml");
        destinationPane = new ContentControllerView<>("DestinationPane.fxml");
        progressPane = new ContentControllerView<>("ProgressPane.fxml");
        mainWindow = new ControllerView<>("MainWindow.fxml");
        stage.setScene(new Scene(mainWindow.getView()));
        stage.setTitle(TITLE);
        stage.setOnCloseRequest(e -> currentContent.getController().handleClose());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        EXECUTOR_SERVICE.shutdown();
    }

    public static void openBrowser(String url) {
        if (Desktop.isDesktopSupported()) {
            EXECUTOR_SERVICE.execute(() -> {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException e) {
                    Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Can't open URL", ButtonType.OK).show());
                    e.printStackTrace();
                }
            });
        } else {
            new Alert(Alert.AlertType.ERROR, "Desktop not supported", ButtonType.OK).show();
        }
    }
}
