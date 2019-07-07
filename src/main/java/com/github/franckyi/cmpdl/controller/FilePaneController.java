package com.github.franckyi.cmpdl.controller;

import com.github.franckyi.cmpdl.CMPDL;
import com.github.franckyi.cmpdl.api.response.Addon;
import com.github.franckyi.cmpdl.api.response.AddonFile;
import com.github.franckyi.cmpdl.api.response.Attachment;
import com.github.franckyi.cmpdl.task.api.CallTask;
import com.github.franckyi.cmpdl.view.AddonFileMinimalView;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class FilePaneController implements Initializable, IContentController {

    private Addon addon;
    private List<AddonFile> files = null;

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
    private ToggleGroup file;

    @FXML
    private RadioButton directButton;

    @FXML
    private VBox directPane;

    @FXML
    private ListView<AddonFile> filesListView;

    @FXML
    private Button changeViewButton;

    @FXML
    private RadioButton idButton;

    @FXML
    private TextField idField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        directPane.disableProperty().bind(directButton.selectedProperty().not());
        idField.disableProperty().bind(idButton.selectedProperty().not());
        filesListView.setCellFactory(param -> new AddonFileMinimalView());
        filesListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super AddonFile>) c -> {
            if (CMPDL.currentContent == CMPDL.filePane && directButton.isSelected()) {
                CMPDL.mainWindow.getController().getNextButton().setDisable(c.getList().size() != 1);
            }
        });
        idButton.selectedProperty().addListener((observable, oldValue, newValue) ->
                CMPDL.mainWindow.getController().getNextButton().setDisable(!newValue && filesListView.getSelectionModel().getSelectedItems().isEmpty()));
    }

    private void viewAllFiles() {
        if (files != null) {
            filesListView.getItems().setAll(files);
            changeViewButton.setText("View latest files...");
            changeViewButton.setOnAction(e -> viewLatestFiles());
        }
    }

    public void viewLatestFiles() {
        filesListView.getItems().setAll(addon.getLatestFiles());
        changeViewButton.setText("View all files...");
        changeViewButton.setOnAction(e -> viewAllFiles());
    }

    @FXML
    void actionViewInBrowser(ActionEvent event) {
        CMPDL.openBrowser(addon.getWebsiteUrl());
    }

    public void setAddon(Addon addon) {
        this.addon = addon;
        addon.getLatestFiles().sort(Comparator.comparing(AddonFile::getFileDate).reversed());
        addon.getAttachments().stream()
            .filter(Attachment::isDefault)
            .findFirst()
            .ifPresent(a -> logoImageView.setImage(new Image(a.getThumbnailUrl())));
        titleLabel.setText(addon.getName());
        authorLabel.setText("by " + addon.getAuthors().get(0).getName());
        summaryLabel.setText(addon.getSummary());
        addon.getCategories().stream()
            .filter(category -> category.getCategoryId() == addon.getPrimaryCategoryId())
            .findFirst()
            .ifPresent(c -> {
                categoryImageView.setImage(new Image(c.getAvatarUrl()));
                categoryLabel.setText(c.getName());
            });
        CMPDL.mainWindow.getController().getNextButton().setDisable(true);
        CMPDL.mainWindow.getController().getPreviousButton().setDisable(false);
        CallTask<List<AddonFile>> task = new CallTask<>(String.format("Getting addon files for addon %d", addon.getId()), CMPDL.getAPI().getAddonFiles(addon.getId()));
        task.setOnSucceeded(e -> files = task.getValue().orElse(null));
        if (files != null) {
            files.sort(Comparator.comparing(AddonFile::getFileDate).reversed());
        }
        CMPDL.EXECUTOR_SERVICE.execute(task);
    }

    @Override
    public void handleNext() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Loading file data...", ButtonType.CLOSE);
        alert.show();
        int fileId = file.getSelectedToggle() == directButton ? filesListView.getSelectionModel().getSelectedItem().getId() : Integer.parseInt(idField.getText());
        CallTask<AddonFile> task = new CallTask<>(String.format("Getting project file %d:%d", addon.getId(), fileId), CMPDL.getAPI().getFile(addon.getId(), fileId));
        task.setOnSucceeded(e -> Platform.runLater(() -> task.getValue().ifPresent(file -> {
            CMPDL.destinationPane.getController().setAddonAndFile(addon, file);
            CMPDL.mainWindow.getController().setContent(CMPDL.destinationPane);
            CMPDL.mainWindow.getController().getNextButton().setDisable(true);
            alert.hide();
        })));
        CMPDL.EXECUTOR_SERVICE.execute(task);
    }

    @Override
    public void handlePrevious() {
        CMPDL.mainWindow.getController().getStartButton().disableProperty().bind(CMPDL.modpackPane.getController().getZipButton().selectedProperty().not());
        CMPDL.mainWindow.getController().getNextButton().disableProperty().bind(CMPDL.modpackPane.getController().getZipButton().selectedProperty());
        CMPDL.mainWindow.getController().setContent(CMPDL.modpackPane);
        CMPDL.mainWindow.getController().getNextButton().disableProperty().unbind();
        CMPDL.mainWindow.getController().getNextButton().setDisable(false);
        CMPDL.mainWindow.getController().getPreviousButton().setDisable(true);
    }

    @Override
    public void handleStart() {

    }
}
