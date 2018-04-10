package com.github.franckyi.cmpdl.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModpackManifest {

    private final String name;
    private final String author;
    private final String version;
    private final String mcVersion;
    private final String forge;
    private final List<ModpackManifestMod> mods;
    private final String overrides;

    public ModpackManifest(JSONObject json) {
        name = json.getString("name");
        author = json.getString("author");
        version = json.getString("version");
        mcVersion = json.getJSONObject("minecraft").getString("version");
        forge = json.getJSONObject("minecraft").getJSONArray("modLoaders").getJSONObject(0).getString("id");
        JSONArray array = json.getJSONArray("files");
        mods = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            mods.add(new ModpackManifestMod(array.getJSONObject(i)));
        }
        overrides = json.getString("overrides");
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    public String getMcVersion() {
        return mcVersion;
    }

    public String getForge() {
        return forge;
    }

    public List<ModpackManifestMod> getMods() {
        return mods;
    }

    public String getOverrides() {
        return overrides;
    }

    @Override
    public String toString() {
        return String.format("### %s v%s by %s for MC %s\n### Mod count : %d\n### Forge version required : %s",
                name.replaceAll("%", ""), version.replaceAll("%", ""), author.replaceAll("%", ""), mcVersion, mods.size(), forge);
    }

    public static class ModpackManifestMod {

        private final int projectId, fileId;

        public ModpackManifestMod(JSONObject json) {
            projectId = json.getInt("projectID");
            fileId = json.getInt("fileID");
        }

        public ModpackManifestMod(String line) {
            String[] split = line.split(":");
            projectId = Integer.parseInt(split[0]);
            fileId = Integer.parseInt(split[1]);
        }

        public int getProjectId() {
            return projectId;
        }

        public int getFileId() {
            return fileId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ModpackManifestMod that = (ModpackManifestMod) o;
            return projectId == that.projectId &&
                    fileId == that.fileId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(projectId, fileId);
        }
    }
}
