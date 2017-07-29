package com.github.franckyi.cmpdl;


import com.github.franckyi.cmpdl.task.CleanTask;
import com.github.franckyi.cmpdl.task.DownloadModpackTask;
import com.github.franckyi.cmpdl.task.VerifyProjectTask;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

@SuppressWarnings("Duplicates")
public class InterfaceController implements Initializable {

    private boolean dark;
    private Optional<String> verified = Optional.empty();
    private Task<?> primaryTask, secondaryTask;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log(CMPDL.title());
        log("Java version " + System.getProperty("java.version"));
        destinationPath.setText(System.getProperty("user.home") + File.separator + "modpack");
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), title);
        transition.setFromY(-10);
        transition.setToY(10);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
    }

    @FXML
    private Label title;

    @FXML
    private TextField modpackURL;

    @FXML
    private TextField fileID;

    @FXML
    public TextField destinationPath;

    @FXML
    private TextArea logTextArea;

    @FXML
    private ProgressBar primaryBar;

    @FXML
    private ProgressBar secondaryBar;

    @FXML
    private ProgressIndicator primaryIndicator;

    @FXML
    private ProgressIndicator secondaryIndicator;

    @FXML
    private Label primaryLabel;

    @FXML
    private Label secondaryLabel;

    @FXML
    private Button verifyButton;

    @FXML
    private Button destinationButton;

    @FXML
    private Button downloadButton;

    @FXML
    private Button stopButton;

    @FXML
    void chooseDestination(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = new File(destinationPath.getText());
        directoryChooser.setInitialDirectory(dir.exists() ? dir : new File(System.getProperty("user.home")));
        directoryChooser.setTitle("Choose the output game / instance directory :");
        File file = directoryChooser.showDialog(CMPDL.stage);
        if (file != null) {
            destinationPath.setText(file.getAbsolutePath());
            log("Destination : " + file.getAbsolutePath());
        }
    }

    @FXML
    void logClear(ActionEvent event) {
        logTextArea.setText("");
    }

    @FXML
    void logCopy(ActionEvent event) {
        ClipboardContent content = new ClipboardContent();
        content.putString(logTextArea.getText());
        Clipboard.getSystemClipboard().setContent(content);
    }

    @FXML
    void startDownload(ActionEvent event) {
        setDisableButtons(true);
        if (!verified.isPresent()) {
            downloadButton.setText("Verifying...");
            VerifyProjectTask task = new VerifyProjectTask(fileID.getText(), modpackURL.getText());
            task.setOnSucceeded(e -> {
                log(task.getValue().isPresent() ? "The modpack is valid !" : "The modpack is invalid !");
                if (task.getValue().isPresent()) {
                    verified = task.getValue();
                    startDownload0();
                } else {
                    setDisableButtons(false);
                    downloadButton.setText("Start Download");
                }
            });
            new Thread(task).start();
        } else startDownload0();
    }

    private void startDownload0() {
        log("Download starting !");
        downloadButton.setText("Downloading...");
        stopButton.setDisable(false);
        verified.ifPresent(s -> {
            try {
                CMPDL.path = destinationPath.getText();
                CMPDL.zipFileName = new File(new URL(s).getFile()).getName().replaceAll("%20", " ");
                setPrimaryProgress(new DownloadModpackTask(s), "Step 1/3 : Downloading modpack");
                new Thread(primaryTask).start();
            } catch (IOException e) {
                trace(e);
            }
        });
    }

    @FXML
    void verify(ActionEvent event) {
        verifyButton.setText("Verifying...");
        setDisableButtons(true);
        VerifyProjectTask task = new VerifyProjectTask(fileID.getText(), modpackURL.getText());
        task.setOnSucceeded(e -> {
            log(task.getValue().isPresent() ? "The modpack is valid !" : "The modpack is invalid !");
            if (task.getValue().isPresent()) {
                verified = task.getValue();
                verifyButton.setText("Verified !");
                setDisableButtons(false);
                verifyButton.setDisable(true);
            } else {
                verifyButton.setText("Verify");
                setDisableButtons(false);
            }
        });
        new Thread(task).start();
    }

    @FXML
    void input(KeyEvent keyEvent) {
        verified = Optional.empty();
        verifyButton.setText("Verify");
        verifyButton.setDisable(false);
    }

    @FXML
    void changeTheme(MouseEvent mouseEvent) {
        CMPDL.parent.setStyle(dark ? "" : "-fx-base: rgba(60, 63, 65, 255);");
        dark = !dark;
    }

    public void log(String text) {
        logTextArea.appendText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss > ")) + text + "\n");
    }

    public void trace(Throwable t) {
        logTextArea.appendText(t.toString() + "\n");
        for (StackTraceElement traceElement : t.getStackTrace())
            logTextArea.appendText("\tat " + traceElement + "\n");
        if (t.getCause() != null) {
            trace(t.getCause());
        }
    }

    private void setDisableButtons(boolean value) {
        downloadButton.setDisable(value);
        verifyButton.setDisable(value);
        destinationButton.setDisable(value);
        modpackURL.setEditable(!value);
        fileID.setEditable(!value);
        destinationPath.setEditable(!value);
    }

    public void setPrimaryProgress(Task<?> task, String text) {
        primaryTask = task;
        primaryBar.progressProperty().bind(task.progressProperty());
        primaryIndicator.progressProperty().bind(task.progressProperty());
        primaryLabel.setText(text);
    }

    private void resetPrimaryProgress() {
        primaryTask = null;
        primaryBar.progressProperty().unbind();
        primaryBar.setProgress(0);
        primaryIndicator.progressProperty().unbind();
        primaryIndicator.setProgress(0);
        primaryLabel.setText("");
    }

    public void setSecondaryProgress(Task<?> task, String text) {
        secondaryTask = task;
        secondaryBar.progressProperty().bind(task.progressProperty());
        secondaryIndicator.progressProperty().bind(task.progressProperty());
        secondaryLabel.setText(text);
    }

    private void resetSecondaryProgress() {
        secondaryTask = null;
        secondaryBar.progressProperty().unbind();
        secondaryBar.setProgress(0);
        secondaryIndicator.progressProperty().unbind();
        secondaryIndicator.setProgress(0);
        secondaryLabel.setText("");
    }

    @FXML
    void stop(ActionEvent actionEvent) {
        if (primaryTask != null) {
            if (primaryTask.isDone() || primaryTask.cancel()) {
                log("Primary task cancelled");
                resetPrimaryProgress();
            } else
                log("Couldn't cancel primary task");

        }
        if (secondaryTask != null) {
            if (secondaryTask.isDone() || secondaryTask.cancel()) {
                log("Secondary task cancelled");
                resetSecondaryProgress();
            } else
                log("Couldn't cancel secondary task");
        }
        if ((primaryTask == null || primaryTask.isCancelled() || primaryTask.isDone()) && (secondaryTask == null || secondaryTask.isCancelled() || secondaryTask.isDone())) {
            setDisableButtons(false);
            log("Execution stopped !");
            new Thread(CleanTask::new).start();
            downloadButton.setText("Start Download");
        }
    }

    public void reset() {
        CMPDL.exceptions.clear();
        resetPrimaryProgress();
        resetSecondaryProgress();
        setDisableButtons(false);
        stopButton.setDisable(true);
        downloadButton.setText("Start Download");
    }

}