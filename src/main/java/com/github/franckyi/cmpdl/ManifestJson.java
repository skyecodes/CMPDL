package com.github.franckyi.cmpdl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManifestJson {

    public MinecraftJson minecraft;

    public String getForgeVersion() {
        for (MinecraftJson.ModloaderJson loader : minecraft.modLoaders) {
            if (loader.id.startsWith("forge"))
                return loader.id.substring("forge-".length());
        }
        return "N/A";
    }

    public static ManifestJson from(JSONObject root) {
        ManifestJson manifestJson = new ManifestJson();
        MinecraftJson minecraftJson = manifestJson.new MinecraftJson();
        JSONObject minecraft = root.getJSONObject("minecraft");
        minecraftJson.version = minecraft.getString("version");
        minecraftJson.modLoaders = new ArrayList<>();
        JSONArray modLoaders = minecraft.getJSONArray("modLoaders");
        modLoaders.iterator().forEachRemaining(jsonValue -> {
            JSONObject modloader = (JSONObject) jsonValue;
            MinecraftJson.ModloaderJson modloaderJson = minecraftJson.new ModloaderJson();
            modloaderJson.id = modloader.getString("id");
            modloaderJson.primary = modloader.getBoolean("primary");
            minecraftJson.modLoaders.add(modloaderJson);
        });
        manifestJson.minecraft = minecraftJson;
        manifestJson.manifestType = root.getString("manifestType");
        manifestJson.manifestVersion = root.getInt("manifestVersion");
        manifestJson.name = root.getString("name");
        manifestJson.version = root.getString("version");
        manifestJson.author = root.getString("author");
        manifestJson.files = new ArrayList<>();
        JSONArray files = root.getJSONArray("files");
        files.iterator().forEachRemaining(jsonValue -> {
            JSONObject file = (JSONObject) jsonValue;
            FileJson fileJson = manifestJson.new FileJson();
            fileJson.projectID = file.getInt("projectID");
            fileJson.fileID = file.getInt("fileID");
            fileJson.required = file.getBoolean("required");
            manifestJson.files.add(fileJson);
        });
        return manifestJson;
    }

    public class MinecraftJson {

        public String version;
        public List<ModloaderJson> modLoaders;

        public class ModloaderJson {

            public String id;
            public boolean primary;

        }
    }

    public String manifestType;
    public int manifestVersion;
    public String name;
    public String version;
    public String author;
    public List<FileJson> files;

    public class FileJson {

        public int projectID;
        public int fileID;
        public boolean required;

    }

    public String overrides;

}
