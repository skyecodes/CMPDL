package com.github.franckyi.cmpdl;

import java.util.List;

@SuppressWarnings("unused")
public class ManifestJson {

    public MinecraftJson minecraft;

    public String getForgeVersion() {
        for (MinecraftJson.ModloaderJson loader : minecraft.modLoaders) {
            if (loader.id.startsWith("forge"))
                return loader.id.substring("forge-".length());
        }
        return "N/A";
    }

    @SuppressWarnings("unused")
    public class MinecraftJson {

        public String version;
        public List<ModloaderJson> modLoaders;

        @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public class FileJson {

        public int projectID;
        public int fileID;
        public boolean required;

    }

    public String overrides;

}
