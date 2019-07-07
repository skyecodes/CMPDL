package com.github.franckyi.cmpdl.controller;

import com.github.franckyi.cmpdl.CMPDL;
import com.github.franckyi.cmpdl.api.response.Addon;
import com.github.franckyi.cmpdl.api.response.AddonFile;
import com.github.franckyi.cmpdl.api.response.Attachment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DestinationPaneController implements Initializable, IContentController {

    private Addon addon;
    private AddonFile addonFile;

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
    private Label fileNameLabel;

    @FXML
    private Label mcVersionLabel;

    @FXML
    private Label releaseTypeLabel;

    @FXML
    private TextField destinationField;

    @FXML
    void actionChooseDestination(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose the destination folder :");
        String currentChosenDirectory = destinationField.getText();
        if (currentChosenDirectory != null && ! "".equals(currentChosenDirectory)) {
            dc.setInitialDirectory(new File(currentChosenDirectory));
        }
        File dst = dc.showDialog(CMPDL.stage);
        if (dst != null) {
            destinationField.setText(dst.getAbsolutePath());
        }
    }

    @FXML
    void actionViewInBrowser(ActionEvent event) {
        CMPDL.openBrowser(addon.getWebsiteUrl());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        destinationField.textProperty().addListener((o, oldValue, newValue) -> CMPDL.mainWindow.getController().getStartButton().setDisable(newValue.isEmpty()));
        releaseTypeLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize()));
    }

    @Override
    public void handleNext() {

    }

    @Override
    public void handlePrevious() {
        CMPDL.mainWindow.getController().setContent(CMPDL.filePane);
        CMPDL.mainWindow.getController().getStartButton().setDisable(true);
        CMPDL.mainWindow.getController().getNextButton().setDisable(false);
    }

    @Override
    public void handleStart() {
        File dst = new File(destinationField.getText());
        if (dst.isDirectory()) {
            if (!dst.exists()) {
                dst.mkdirs();
            }
            if (!dst.canWrite()) {
                new Alert(Alert.AlertType.ERROR, "Permission denied. Please choose another destination folder.", ButtonType.OK).show();
            } else {
                CMPDL.progressPane.getController().setData(addonFile, dst);
                CMPDL.mainWindow.getController().setContent(CMPDL.progressPane);
                CMPDL.mainWindow.getController().getStartButton().setDisable(true);
                CMPDL.mainWindow.getController().getPreviousButton().setDisable(true);
                CMPDL.progressPane.getController().start();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "The destination must be a folder.", ButtonType.OK).show();
        }
    }

    public void setAddonAndFile(Addon addon, AddonFile file) {
        this.addon = addon;
        this.addonFile = file;
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
        fileNameLabel.setText(file.getDisplayName());
        mcVersionLabel.setText(String.join(", ", file.getGameVersion()));
        releaseTypeLabel.setText(file.getReleaseType().toString());
        releaseTypeLabel.setBackground(new Background(new BackgroundFill(file.getReleaseType().getColor(), new CornerRadii(5), new Insets(-2, -5, -2, -5))));
        releaseTypeLabel.setTextFill(Color.WHITE);

    }
}
