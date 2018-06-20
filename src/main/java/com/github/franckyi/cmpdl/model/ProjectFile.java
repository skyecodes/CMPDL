package com.github.franckyi.cmpdl.model;

import org.json.JSONObject;

public class ProjectFile implements IProjectFile {

    private final String fileName;
    private final String fileNameOnDisk;
    private final String gameVersion;
    //private final String fileType;
    private final int fileId;
    private final String downloadUrl;

    public ProjectFile(JSONObject json) {
        if (json.has("error") && json.getBoolean("error"))
            throw new IllegalArgumentException("Error " + json.getInt("status"));
        fileName = json.getString("FileName");
        fileNameOnDisk = json.getString("FileNameOnDisk");
        //gameVersion = json.getJSONArray("GameVersion").getString(0);
        gameVersion = json.getJSONArray("gameVersion").getString(0);
        //fileType = json.getString("ReleaseType");
        fileId = json.getInt("Id");
        downloadUrl = json.getString("DownloadURL");
    }

    @Override
    public int getFileId() {
        return fileId;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    public String getFileNameOnDisk() {
        return fileNameOnDisk;
    }

    @Override
    public String getGameVersion() {
        return gameVersion;
    }

    @Override
    public String getFileType() {
        //return fileType;
        return "";
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
