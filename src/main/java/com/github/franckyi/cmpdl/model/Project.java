package com.github.franckyi.cmpdl.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Project {

    private final int projectId;
    private final String name;
    private final String author;
    private final String summary;
    private final String logoUrl;
    private final String url;
    private final String categoryName;
    private final String categoryLogoUrl;
    private final boolean modpack;
    private final List<ProjectFileMinimal> files;

    public Project(JSONObject json) {
        projectId = json.getInt("Id");
        name = json.getString("Name");
        author = json.getString("PrimaryAuthorName");
        summary = json.getString("Summary");
        logoUrl = json.getJSONArray("Attachments").getJSONObject(0).getString("Url");
        url = json.getString("WebSiteURL");
        categoryName = json.getString("PrimaryCategoryName");
        categoryLogoUrl = json.getString("PrimaryCategoryAvatarUrl");
        modpack = "ModPack".equals(json.getString("PackageType"));
        JSONArray array = json.getJSONArray("GameVersionLatestFiles");
        files = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            files.add(new ProjectFileMinimal(array.getJSONObject(i)));
        }
        Collections.sort(files);
    }

    public int getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getSummary() {
        return summary;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryLogoUrl() {
        return categoryLogoUrl;
    }

    public boolean isModpack() {
        return modpack;
    }

    public List<ProjectFileMinimal> getFiles() {
        return files;
    }
}
