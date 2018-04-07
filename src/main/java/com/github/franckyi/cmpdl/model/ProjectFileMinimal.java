package com.github.franckyi.cmpdl.model;

import org.json.JSONObject;

public class ProjectFileMinimal implements IProjectFile {

    private final String fileName;
    private final String gameVersion;
    private final String fileType;
    private final int fileId;

    public ProjectFileMinimal(JSONObject json) {
        fileName = json.getString("ProjectFileName");
        gameVersion = json.getString("GameVesion");
        fileType = json.getString("FileType");
        fileId = json.getInt("ProjectFileID");
    }

    public String getFileName() {
        return fileName;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public String getFileType() {
        return fileType;
    }

    public int getFileId() {
        return fileId;
    }
}
