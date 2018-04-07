package com.github.franckyi.cmpdl.task.mpimport;

import com.github.franckyi.cmpdl.CMPDL;
import com.github.franckyi.cmpdl.CurseMetaAPI;
import com.github.franckyi.cmpdl.model.ModpackManifest;
import com.github.franckyi.cmpdl.model.ProjectFile;
import com.github.franckyi.cmpdl.task.TaskBase;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

import java.io.File;
import java.util.List;

public class DownloadModsTask extends TaskBase<Void> {

    private final File modsFolder;
    private final List<ModpackManifest.ModpackManifestMod> mods;

    private ObjectProperty<Task<?>> task = new SimpleObjectProperty<>();

    public DownloadModsTask(File modsFolder, List<ModpackManifest.ModpackManifestMod> mods) {
        this.modsFolder = modsFolder;
        this.mods = mods;
    }

    @Override
    protected Void call0() {
        updateProgress(0, 1);
        for (int i = 0; i < mods.size(); i++) {
            if (isCancelled()) return null;
            updateTitle(String.format("Downloading mods (%d/%d)", i + 1, mods.size()));
            ModpackManifest.ModpackManifestMod mod = mods.get(i);
            CMPDL.progressPane.getController().log("Resolving file %d:%d", mod.getProjectId(), mod.getFileId());
            ProjectFile file = CurseMetaAPI.getProjectFile(mod.getProjectId(), mod.getFileId());
            if (file != null) {
                DownloadFileTask task = new DownloadFileTask(file.getDownloadUrl(), new File(modsFolder, file.getFileNameOnDisk()));
                setTask(task);
                CMPDL.progressPane.getController().log("Downloading file %s", file.getFileName().replaceAll("%", ""));
                task.run();
            } else {
                CMPDL.progressPane.getController().log("Unknown file %d:%d - skipping", mod.getProjectId(), mod.getFileId());
            }
            updateProgress(i, mods.size());
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
