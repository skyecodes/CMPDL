package com.github.franckyi.cmpdl.controller;

import com.github.franckyi.cmpdl.CMPDL;
import com.github.franckyi.cmpdl.core.ContentControllerView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    private boolean darkTheme = false;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private Button closeButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button startButton;

    @FXML
    void actionClose(ActionEvent event) {
        CMPDL.currentContent.getController().handleClose();
    }

    @FXML
    void actionStart(ActionEvent event) {
        CMPDL.currentContent.getController().handleStart();
    }

    @FXML
    void actionNext(ActionEvent event) {
        CMPDL.currentContent.getController().handleNext();
    }

    @FXML
    void actionPrevious(ActionEvent event) {
        CMPDL.currentContent.getController().handlePrevious();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setContent(CMPDL.modpackPane);
        startButton.disableProperty().bind(CMPDL.modpackPane.getController().getZipButton().selectedProperty().not());
        nextButton.disableProperty().bind(CMPDL.modpackPane.getController().getZipButton().selectedProperty());
    }

    public void setContent(ContentControllerView<?> cv) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(cv.getView());
        CMPDL.currentContent = cv;
        CMPDL.stage.sizeToScene();
    }

    public Button getPreviousButton() {
        return previousButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public Button getStartButton() {
        return startButton;
    }

    public void switchTheme(MouseEvent mouseEvent) {
        if (darkTheme) {
            CMPDL.mainWindow.getView().setStyle("-fx-base:#ececec;");
        } else {
            CMPDL.mainWindow.getView().setStyle("-fx-base:black;");
        }
        darkTheme = !darkTheme;
    }
}
