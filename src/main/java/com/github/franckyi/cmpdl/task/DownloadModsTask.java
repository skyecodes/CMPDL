package com.github.franckyi.cmpdl.task;

import com.eclipsesource.json.Json;
import com.github.franckyi.cmpdl.CMPDL;
import com.github.franckyi.cmpdl.ManifestJson;
import javafx.application.Platform;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadModsTask extends CustomTask<Void> {

    private final File folder;
    private ManifestJson manifest;

    DownloadModsTask(File folder) {
        new File(CMPDL.getModsDirectory()).mkdirs();
        this.folder = folder;
    }

    @Override
    protected Void call0() throws Exception {
        updateProgress(0, 1);
        manifest = ManifestJson.from(Json.parse(new FileReader(CMPDL.getManifestFile())).asObject());
        int length = manifest.files.size();
        log("# " + manifest.name + " v" + manifest.version + " by " + manifest.author);
        log("Mods download started : " + length + " files found");
        final int[] i = {0};
        for (ManifestJson.FileJson fileJson : manifest.files) {
            if (!isCancelled()) {
                log("# Resolving " + fileJson.projectID + ":" + fileJson.fileID + " (" + (i[0]++ + 1) + "/" + length + ")");
                DownloadModTask task = new DownloadModTask(fileJson);
                task.setOnSucceeded(event -> updateProgress(i[0] - 1, length));
                new Thread(task).start();
                while (!task.isDone() && !task.isCancelled()) {
                    if (task.isCancelled()) return null;
                }
            } else return null;
        }
        return null;
    }

    @Override
    protected void succeeded() {
        log("> Mods download succeeded !");
        CopyOverridesTask task = new CopyOverridesTask(manifest, folder);
        getController().setPrimaryProgress(task, "Step 3/3 : Copying overrides");
        new Thread(task).start();
    }

    private class DownloadModTask extends CustomTask<Void> {

        private final ManifestJson.FileJson fileJson;

        DownloadModTask(ManifestJson.FileJson fileJson) {
            this.fileJson = fileJson;
        }

        @Override
        protected Void call0() throws Exception {
            updateProgress(0, 1);
            String url0 = crawl(crawlAddHost("http://minecraft.curseforge.com/projects/" + fileJson.projectID) + "/files/" + fileJson.fileID + "/download");
            if (DownloadModsTask.this.isCancelled()) return null;
            URL url = new URL(url0);
            String fileName = new File(url.getFile()).getName().replaceAll("%20", " ");
            log("> Downloading " + fileName);
            Platform.runLater(() -> getController().setSecondaryProgress(this, fileName));
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            long completeFileSize = httpConnection.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(httpConnection.getInputStream());
            File file = new File(CMPDL.getModsDirectory() + File.separator + fileName);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            long dl = 0;
            int count;
            while ((count = bis.read(buffer, 0, 1024)) != -1 && !isCancelled()) {
                fos.write(buffer, 0, count);
                dl += count;
                this.updateProgress(dl, completeFileSize);
            }
            fos.close();
            bis.close();
            log("> Download succeeded !");
            return null;
        }

    }

}
