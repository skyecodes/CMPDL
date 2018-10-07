package com.github.franckyi.cmpdl.model;

import org.json.JSONObject;

public class ProjectFileMinimal implements IProjectFile {

    private final String fileName;
    private final String gameVersion;
    private FileReleaseType fileType;
    private final int fileId;

    ProjectFileMinimal(JSONObject json) {
        fileName = json.getString("projectFileName");
        gameVersion = json.getString("gameVersion");
        try {
            fileType = parseFileType(json.getInt("fileType"));
        } catch (Exception e) {
            fileType = FileReleaseType.ALPHA;
            e.printStackTrace();
        }
        fileId = json.getInt("projectFileId");
    }

    public String getFileName() {
        return fileName;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    @Override
    public FileReleaseType getFileType() {
        return fileType;
    }

    public int getFileId() {
        return fileId;
    }
}
