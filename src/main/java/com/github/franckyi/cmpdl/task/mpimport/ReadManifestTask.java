package com.github.franckyi.cmpdl.task.mpimport;

import com.github.franckyi.cmpdl.model.ModpackManifest;
import com.github.franckyi.cmpdl.task.TaskBase;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ReadManifestTask extends TaskBase<ModpackManifest> {

    private final File manifestFile;

    public ReadManifestTask(File manifestFile) {
        this.manifestFile = manifestFile;
    }

    @Override
    protected ModpackManifest call0() throws IOException {
        return new ModpackManifest(new JSONObject(new String(Files.readAllBytes(manifestFile.toPath()), StandardCharsets.UTF_8)));
    }
}
