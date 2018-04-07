package com.github.franckyi.cmpdl.controller;

import com.github.franckyi.cmpdl.CMPDL;
import com.github.franckyi.cmpdl.model.IProjectFile;
import com.github.franckyi.cmpdl.model.Project;
import com.github.franckyi.cmpdl.model.ProjectFilesList;
import com.github.franckyi.cmpdl.task.cursemeta.GetProjectFileTask;
import com.github.franckyi.cmpdl.task.cursemeta.GetProjectFilesTask;
import com.github.franckyi.cmpdl.view.ProjectFileMinimalView;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class FilePaneController implements Initializable, IContentController {

    private Project project;
    private ProjectFilesList files = null;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label summaryLabel;

    @FXML
    private ImageView categoryImageView;

    @FXML
    private Label categoryLabel;

    @FXML
    private ListView<IProjectFile> filesListView;

    @FXML
    private Button changeViewButton;

    public void viewAllFiles() {
        if (files != null) {
            filesListView.getItems().setAll(files);
            changeViewButton.setText("View latest files...");
            changeViewButton.setOnAction(e -> viewLatestFiles());
        }
    }

    public void viewLatestFiles() {
        filesListView.getItems().setAll(project.getFiles());
        changeViewButton.setText("View all files...");
        changeViewButton.setOnAction(e -> viewAllFiles());
    }

    @FXML
    void actionViewInBrowser(ActionEvent event) {
        CMPDL.openBrowser(project.getUrl());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filesListView.setCellFactory(param -> new ProjectFileMinimalView());
        filesListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super IProjectFile>) c -> {
            if (CMPDL.currentContent == CMPDL.filePane) {
                CMPDL.mainWindow.getController().getNextButton().setDisable(c.getList().size() != 1);
            }
        });
    }

    public void setProject(Project project) {
        this.project = project;
        logoImageView.setImage(new Image(project.getLogoUrl()));
        titleLabel.setText(project.getName());
        authorLabel.setText("by " + project.getAuthor());
        summaryLabel.setText(project.getSummary());
        categoryImageView.setImage(new Image(project.getCategoryLogoUrl()));
        categoryLabel.setText(project.getCategoryName());
        CMPDL.mainWindow.getController().getNextButton().setDisable(true);
        CMPDL.mainWindow.getController().getPreviousButton().setDisable(false);
        GetProjectFilesTask task = new GetProjectFilesTask(project.getProjectId());
        task.setOnSucceeded(e -> files = task.getValue().orElse(null));
        CMPDL.EXECUTOR_SERVICE.execute(task);
    }

    @Override
    public void handleNext() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Loading file data...", ButtonType.CLOSE);
        alert.show();
        GetProjectFileTask task = new GetProjectFileTask(project.getProjectId(), filesListView.getSelectionModel().getSelectedItem().getFileId());
        task.setOnSucceeded(e -> Platform.runLater(() -> task.getValue().ifPresent(file -> {
            CMPDL.destinationPane.getController().setProjectAndFile(project, file);
            CMPDL.mainWindow.getController().setContent(CMPDL.destinationPane);
            CMPDL.mainWindow.getController().getNextButton().setDisable(true);
            alert.hide();
        })));
        CMPDL.EXECUTOR_SERVICE.execute(task);
    }

    @Override
    public void handlePrevious() {
        CMPDL.mainWindow.getController().setContent(CMPDL.modpackPane);
        CMPDL.mainWindow.getController().getNextButton().setDisable(false);
        CMPDL.mainWindow.getController().getPreviousButton().setDisable(true);
    }

    @Override
    public void handleStart() {

    }
}
