package com.github.franckyi.cmpdl.controller;

import javafx.application.Platform;

public interface IContentController {

    void handleNext();

    void handlePrevious();

    default void handleClose() {
        Platform.exit();
    }

    void handleStart();

}
