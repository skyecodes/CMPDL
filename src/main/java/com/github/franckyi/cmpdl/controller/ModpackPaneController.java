package com.github.franckyi.cmpdl.controller;

import com.github.franckyi.cmpdl.CMPDL;
import com.github.franckyi.cmpdl.task.cursemeta.GetProjectIdTask;
import com.github.franckyi.cmpdl.task.cursemeta.GetProjectTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ModpackPaneController implements Initializable, IContentController {

    @FXML
    private TextField urlField;

    @FXML
    private TextField idField;

    @FXML
    private RadioButton urlButton;

    @FXML
    private RadioButton idButton;

    @FXML
    private ToggleGroup modpack;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        urlField.disableProperty().bind(urlButton.selectedProperty().not());
        idField.disableProperty().bind(idButton.selectedProperty().not());
    }

    @Override
    public void handleNext() {
        if (modpack.getSelectedToggle() == urlButton) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Parsing project ID...", ButtonType.CLOSE);
            alert.show();
            GetProjectIdTask task = new GetProjectIdTask(urlField.getText());
            task.setOnSucceeded(event -> {
                alert.hide();
                handleNext(task.getValue().orElse(-1));
            });
            CMPDL.EXECUTOR_SERVICE.execute(task);
        } else {
            int projectId = -1;
            try {
                projectId = Integer.parseInt(idField.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "The project ID must be an integer", ButtonType.OK).show();
            }
            handleNext(projectId);
        }
    }

    @Override
    public void handlePrevious() {

    }

    @Override
    public void handleStart() {

    }

    public void handleNext(int projectId) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Loading project data...", ButtonType.CLOSE);
        alert.show();
        GetProjectTask task = new GetProjectTask(projectId);
        task.setOnSucceeded(event -> Platform.runLater(() -> task.getValue().ifPresent(project -> {
            if (project.isModpack()) {
                CMPDL.filePane.getController().setProject(project);
                CMPDL.filePane.getController().viewLatestFiles();
                CMPDL.mainWindow.getController().setContent(CMPDL.filePane);
                alert.hide();
            } else {
                alert.hide();
                new Alert(Alert.AlertType.ERROR, "The project isn't a modpack !", ButtonType.OK).show();
            }
        })));
        CMPDL.EXECUTOR_SERVICE.execute(task);
    }
}
