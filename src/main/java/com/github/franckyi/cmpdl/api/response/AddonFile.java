package com.github.franckyi.cmpdl.api.response;

import com.github.franckyi.cmpdl.api.IBean;

import java.time.Instant;
import java.util.List;

public class AddonFile implements IBean {

    private int id;
    private String displayName;
    private String fileName;
    private Instant fileDate;
    private ReleaseType releaseType;
    private String downloadUrl;
    private List<String> gameVersion;

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFileName() {
        return fileName;
    }

    public Instant getFileDate() {
        return fileDate;
    }

    public ReleaseType getReleaseType() {
        return releaseType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public List<String> getGameVersion() {
        return gameVersion;
    }

}
