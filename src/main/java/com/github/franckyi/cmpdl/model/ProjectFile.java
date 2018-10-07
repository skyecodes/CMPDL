package com.github.franckyi.cmpdl.model;

import org.json.JSONObject;

public class ProjectFile implements IProjectFile {

    private final String fileName;
    private final String fileNameOnDisk;
    private final String gameVersion;
    private FileReleaseType fileType;
    private final int fileId;
    private final String downloadUrl;

    public ProjectFile(JSONObject json) {
        fileName = json.getString("fileName");
        fileNameOnDisk = json.getString("fileNameOnDisk");
        gameVersion = json.getJSONArray("gameVersion").getString(0);
        try {
            fileType = parseFileType(json.getInt("releaseType"));
        } catch (Exception e) {
            fileType = FileReleaseType.ALPHA;
            e.printStackTrace();
        }
        fileId = json.getInt("id");
        downloadUrl = json.getString("downloadUrl");
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
    public FileReleaseType getFileType() {
        return fileType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
