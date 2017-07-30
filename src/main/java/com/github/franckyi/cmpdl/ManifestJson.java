package com.github.franckyi.cmpdl;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

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

    public static ManifestJson from(JsonObject root) {
        ManifestJson manifestJson = new ManifestJson();
        MinecraftJson minecraftJson = manifestJson.new MinecraftJson();
        JsonObject minecraft = root.get("minecraft").asObject();
        minecraftJson.version = minecraft.getString("version", "N/A");
        minecraftJson.modLoaders = new ArrayList<>();
        JsonArray modLoaders = minecraft.get("modLoaders").asArray();
        modLoaders.iterator().forEachRemaining(jsonValue -> {
            JsonObject modloader = jsonValue.asObject();
            MinecraftJson.ModloaderJson modloaderJson = minecraftJson.new ModloaderJson();
            modloaderJson.id = modloader.getString("id", "N/A");
            modloaderJson.primary = modloader.getBoolean("primary", false);
            minecraftJson.modLoaders.add(modloaderJson);
        });
        manifestJson.minecraft = minecraftJson;
        manifestJson.manifestType = root.getString("manifestType", "N/A");
        manifestJson.manifestVersion = root.getInt("manifestVersion", 0);
        manifestJson.name = root.getString("name", "N/A");
        manifestJson.version = root.getString("version", "N/A");
        manifestJson.author = root.getString("author", "N/A");
        manifestJson.files = new ArrayList<>();
        JsonArray files = root.get("files").asArray();
        files.iterator().forEachRemaining(jsonValue -> {
            JsonObject file = jsonValue.asObject();
            FileJson fileJson = manifestJson.new FileJson();
            fileJson.projectID = file.getInt("projectID", 0);
            fileJson.fileID = file.getInt("fileID", 0);
            fileJson.required = file.getBoolean("required", false);
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
