package com.github.franckyi.cmpdl.controller;

import com.github.franckyi.cmpdl.CMPDL;
import com.github.franckyi.cmpdl.ControllerView;
import com.github.franckyi.cmpdl.EnumContent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    private EnumContent content;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private Button closeButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button finishButton;

    @FXML
    void actionClose(ActionEvent event) {

    }

    @FXML
    void actionFinish(ActionEvent event) {

    }

    @FXML
    void actionNext(ActionEvent event) {
        if (content == EnumContent.MODPACK) {
            int projectId = CMPDL.modpackPane.getController().getProjectID();

        }
    }

    @FXML
    void actionPrevious(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setContent(CMPDL.modpackPane);
    }

    public void setContent(ControllerView<?> cv) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(cv.getView());
        content = cv.getEnumContent();
    }

}
