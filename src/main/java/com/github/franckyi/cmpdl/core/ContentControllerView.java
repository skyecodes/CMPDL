package com.github.franckyi.cmpdl.core;

import com.github.franckyi.cmpdl.controller.IContentController;

import java.io.IOException;

public class ContentControllerView<T extends IContentController> extends ControllerView<T> {

    public ContentControllerView(String res) throws IOException {
        super(res);
    }

}
