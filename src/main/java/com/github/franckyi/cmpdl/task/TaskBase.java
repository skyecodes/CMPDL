package com.github.franckyi.cmpdl.task;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.util.Optional;

public abstract class TaskBase<V> extends Task<Optional<V>> {

    @Override
    protected Optional<V> call() {
        try {
            return Optional.ofNullable(call0());
        } catch (Throwable t) {
            t.printStackTrace();
            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, t.getMessage()).show());
            return Optional.empty();
        }
    }

    protected abstract V call0() throws Throwable;

}
