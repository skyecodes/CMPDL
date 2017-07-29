package com.github.franckyi.cmpdl.task;

import com.github.franckyi.cmpdl.CMPDL;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadModpackTask extends CustomTask<Void> {

    private URL url;
    private File tmp;

    public DownloadModpackTask(String url) throws IOException {
        this.url = new URL(url);
        new File(CMPDL.getTempDirectory()).mkdirs();
        this.tmp = new File(CMPDL.getZipFile());
        this.tmp.createNewFile();
    }

    @Override
    protected Void call0() throws Exception {
        log("> Downloading modpack at " + url);
        this.updateProgress(0, 1);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        long completeFileSize = httpConnection.getContentLength();
        BufferedInputStream bis = new BufferedInputStream(httpConnection.getInputStream());
        FileOutputStream fis = new FileOutputStream(tmp);
        byte[] buffer = new byte[1024];
        long dl = 0;
        int count;
        while ((count = bis.read(buffer, 0, 1024)) != -1 && !isCancelled()) {
            fis.write(buffer, 0, count);
            dl += count;
            this.updateProgress(dl, completeFileSize);
        }
        fis.close();
        bis.close();
        return null;
    }

    @Override
    protected void succeeded() {
        UnzipModpackTask task = new UnzipModpackTask(tmp);
        getController().setSecondaryProgress(task, "Unzipping modpack");
        new Thread(task).start();
        log("> Download succeeded !");
    }

}
