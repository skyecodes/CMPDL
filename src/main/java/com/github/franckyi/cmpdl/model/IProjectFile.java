package com.github.franckyi.cmpdl.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Comparator;

public interface IProjectFile extends Comparable<IProjectFile> {

    int getFileId();

    /**
     * Parses the integer type provided by api to FileReleaseType
     *
     * @param releaseType the integer received from API
     * @return FileReleaseType
     * @throws Exception Thrown when the release type could not be determined
     */
    default FileReleaseType parseFileType(int releaseType) throws Exception {
        switch (releaseType) {
            case 1:
                return FileReleaseType.RELEASE;
            case 2:
                return FileReleaseType.BETA;
            case 3:
                return FileReleaseType.ALPHA;
            default:
                throw new Exception("Failed to find release type for id " + releaseType);
        }
    }

    String getFileName();

    String getGameVersion();

    FileReleaseType getFileType();

    @Override
    default int compareTo(IProjectFile o) {
        return Comparator.comparingInt(IProjectFile::getFileId).reversed().compare(this, o);
    }

    default Paint getColor() {
        switch (getFileType()) {
            case ALPHA:
                return Color.web("#E49788");
            case BETA:
                return Color.web("#7FA5C4");
            case RELEASE:
                return Color.web("#8CAF62");
            default:
                return Color.BLACK;
        }
    }
}
