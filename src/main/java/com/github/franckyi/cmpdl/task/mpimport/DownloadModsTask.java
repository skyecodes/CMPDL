package com.github.franckyi.cmpdl.task.mpimport;

import com.github.franckyi.cmpdl.CMPDL;
import com.github.franckyi.cmpdl.api.response.AddonFile;
import com.github.franckyi.cmpdl.model.ModpackManifest;
import com.github.franckyi.cmpdl.task.TaskBase;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

import java.io.*;
import java.util.List;

public class DownloadModsTask extends TaskBase<Void> {

    private final File modsFolder, progressFile;
    private final List<ModpackManifest.ModpackManifestMod> mods;

    private final ObjectProperty<Task<?>> task = new SimpleObjectProperty<>();

    public DownloadModsTask(File modsFolder, File progressFile, List<ModpackManifest.ModpackManifestMod> mods) {
        this.modsFolder = modsFolder;
        this.progressFile = progressFile;
        this.mods = mods;
    }

    @Override
    protected Void call0() throws IOException {
        int max = mods.size();
        int start = 0;
        if (progressFile.exists() && progressFile.isFile()) {
            BufferedReader reader = new BufferedReader(new FileReader(progressFile));
            String line;
            while ((line = reader.readLine()) != null && !isCancelled()) {
                ModpackManifest.ModpackManifestMod mod = new ModpackManifest.ModpackManifestMod(line);
                mods.remove(mod);
                CMPDL.progressPane.getController().log("File %d:%d already downloaded - skipping", mod.getProjectId(), mod.getFileId());
                start++;
            }
            reader.close();
        } else {
            progressFile.createNewFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(progressFile, true))) {
            for (int i = start; i < max; i++) {
                updateProgress(i, max);
                if (isCancelled()) {
                    writer.close();
                    return null;
                }
                updateTitle(String.format("Downloading mods (%d/%d)", i + 1, max));
                ModpackManifest.ModpackManifestMod mod = mods.get(i - start);
                CMPDL.progressPane.getController().log("Resolving file %d:%d", mod.getProjectId(), mod.getFileId());
                AddonFile file = CMPDL.getAPI().getFile(mod.getProjectId(), mod.getFileId()).execute().body();
                if (file != null) {
                    DownloadFileTask task = new DownloadFileTask(file.getDownloadUrl(), new File(modsFolder, file.getFileName()));
                    setTask(task);
                    CMPDL.progressPane.getController().log("Downloading file %s", file.getFileName().replaceAll("%", ""));
                    task.setOnSucceeded(e -> {
                        try {
                            writer.write(String.format("%d:%d\n", mod.getProjectId(), mod.getFileId()));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    });
                    task.run();
                } else {
                    CMPDL.progressPane.getController().log("!!! Unknown file %d:%d - skipping !!!", mod.getProjectId(), mod.getFileId());
                }
            }
        }
        return null;
    }


    private void setTask(Task<?> task) {
        Platform.runLater(() -> this.task.setValue(task));
    }

    public ReadOnlyObjectProperty<Task<?>> taskProperty() {
        return task;
    }
}
