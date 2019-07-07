package com.github.franckyi.cmpdl.controller;

import com.github.franckyi.cmpdl.CMPDL;
import com.github.franckyi.cmpdl.api.response.AddonFile;
import com.github.franckyi.cmpdl.model.ModpackManifest;
import com.github.franckyi.cmpdl.task.mpimport.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProgressPaneController implements Initializable, IContentController {

    private Task<?> task1, task2;
    private boolean done = false;

    private AddonFile addonFile;
    private File destination, zipFile, unzipFolder, minecraft, modsFolder, temp, progressFile;
    private ModpackManifest manifest;

    @FXML
    private DialogPane root;

    @FXML
    private Label titleLabel;

    @FXML
    private Label subLabel1;

    @FXML
    private ProgressBar progressBar1;

    @FXML
    private ProgressIndicator progressIndicator1;

    @FXML
    private Label subLabel2;

    @FXML
    private ProgressBar progressBar2;

    @FXML
    private ProgressIndicator progressIndicator2;

    @FXML
    private TextArea console;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.expandedProperty().addListener((observable, oldValue, newValue) -> CMPDL.stage.sizeToScene());
    }

    @Override
    public void handleNext() {

    }

    @Override
    public void handlePrevious() {

    }

    @Override
    public void handleStart() {

    }

    @Override
    public void handleClose() {
        if (!done) {
            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel the modpack download ?", ButtonType.YES, ButtonType.NO).showAndWait();
            if (buttonType.orElse(null) == ButtonType.YES) {
                if (task2 != null) task2.cancel();
                if (task1 != null) task1.cancel();
                Platform.exit();
            }
        } else {
            Platform.exit();
        }
    }

    public void setData(AddonFile addonFile, File destination) {
        this.addonFile = addonFile;
        this.destination = destination;
        minecraft = new File(destination, "minecraft");
        minecraft.mkdirs();
        modsFolder = new File(minecraft, "mods");
        modsFolder.mkdirs();
        temp = new File(destination, ".cmpdl_temp");
        temp.mkdirs();
        progressFile = new File(temp, ".progress");
        zipFile = new File(temp, addonFile.getFileName());
        unzipFolder = new File(temp, addonFile.getFileName().replace(".zip", ""));
        unzipFolder.mkdirs();
    }

    public void setData(File zipFile, File dstFolder) {
        this.zipFile = zipFile;
        this.destination = dstFolder;
        minecraft = new File(destination, "minecraft");
        minecraft.mkdirs();
        modsFolder = new File(minecraft, "mods");
        modsFolder.mkdirs();
        temp = new File(destination, ".cmpdl_temp");
        temp.mkdirs();
        progressFile = new File(temp, ".progress");
        unzipFolder = new File(temp, zipFile.getName().replace(".zip", ""));
        unzipFolder.mkdirs();
    }

    private void setTask1(Task<?> task) {
        task1 = task;
        bind(task, subLabel1, progressBar1, progressIndicator1);
    }

    private void setTask2(Task<?> task) {
        task2 = task;
        bind(task, subLabel2, progressBar2, progressIndicator2);
    }

    private void bind(Task<?> task, Label subLabel, ProgressBar progressBar, ProgressIndicator progressIndicator) {
        if (task != null) {
            subLabel.textProperty().bind(task.titleProperty());
            progressBar.progressProperty().bind(task.progressProperty());
            progressIndicator.progressProperty().bind(task.progressProperty());
        } else {
            subLabel.textProperty().unbind();
            subLabel.setText("");
            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);
            progressIndicator.progressProperty().unbind();
            progressIndicator.setProgress(0);
        }
    }

    public void log(String s, Object... args) {
        String s0 = String.format(s, args);
        System.out.println(s0);
        Platform.runLater(() -> console.appendText(s0 + "\n"));
    }

    public void start() {
        if (progressFile.exists()) {
            readManifest();
        } else {
            downloadModpack();
        }
    }

    private void downloadModpack() {
        log("Downloading modpack");
        DownloadFileTask task = new DownloadFileTask(addonFile.getDownloadUrl(), new File(temp, addonFile.getFileName()));
        task.setOnSucceeded(e -> {
            log("Modpack downloaded successfully");
            unzipModpack();
        });
        setTask1(task);
        CMPDL.EXECUTOR_SERVICE.execute(task);
    }

    public void unzipModpack() {
        log("Unzipping modpack");
        UnzipFileTask task = new UnzipFileTask(zipFile, unzipFolder);
        task.setOnSucceeded(e -> {
            log("Modpack unzipped successfully");
            readManifest();
        });
        setTask1(task);
        CMPDL.EXECUTOR_SERVICE.execute(task);
    }

    private void readManifest() {
        log("Reading manifest");
        ReadManifestTask task = new ReadManifestTask(new File(unzipFolder, "manifest.json"));
        task.setOnSucceeded(e -> task.getValue().ifPresent(manifest -> {
            log("Manifest read successfully");
            this.manifest = manifest;
            log("### Manifest content :");
            log(manifest.toString());
            downloadMods();
        }));
        setTask1(task);
        CMPDL.EXECUTOR_SERVICE.execute(task);
    }

    private void downloadMods() {
        log("Downloading mods");
        DownloadModsTask task = new DownloadModsTask(modsFolder, progressFile, manifest.getMods());
        task.setOnSucceeded(e -> {
            log("Downloaded mods successfully");
            copyOverrides();
        });
        setTask1(task);
        task.taskProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) setTask2(newValue);
        });
        CMPDL.EXECUTOR_SERVICE.execute(task);
    }

    private void copyOverrides() {
        setTask2(null);
        log("Copying overrides");
        CopyOverridesTask task = new CopyOverridesTask(new File(unzipFolder, manifest.getOverrides()), minecraft);
        task.setOnSucceeded(e -> {
            log("Copied overrides successfully");
            clean();
        });
        setTask1(task);
        CMPDL.EXECUTOR_SERVICE.execute(task);
    }

    private void clean() {
        log("Cleaning");
        CleanTask task = new CleanTask(temp);
        task.setOnSucceeded(e -> finish());
        setTask1(task);
        CMPDL.EXECUTOR_SERVICE.execute(task);
    }

    private void finish() {
        done = true;
        setTask1(null);
        subLabel1.setText("!!! Modpack imported successfully !!!");
        subLabel2.setText(String.format("Make sure to install %s before playing.", manifest.getForge()));
        log("Cleaned successfully");
        log("!!! Modpack imported successfully !!!");
        log("Make sure to install %s before playing.", manifest.getForge());
    }

}
