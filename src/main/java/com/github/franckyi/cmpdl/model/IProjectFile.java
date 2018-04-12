package com.github.franckyi.cmpdl.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Comparator;

public interface IProjectFile extends Comparable<IProjectFile> {

    int getFileId();

    String getFileName();

    String getGameVersion();

    String getFileType();

    @Override
    default int compareTo(IProjectFile o) {
        return Comparator.comparingInt(IProjectFile::getFileId).reversed().compare(this, o);
    }

    default Paint getColor() {
        switch (getFileType()) {
            case "Alpha":
                return Color.web("#E49788");
            case "Beta":
                return Color.web("#7FA5C4");
            case "Release":
                return Color.web("#8CAF62");
            default:
                return Color.BLACK;
        }
    }
}
