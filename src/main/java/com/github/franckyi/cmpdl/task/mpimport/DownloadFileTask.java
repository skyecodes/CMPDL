package com.github.franckyi.cmpdl.task.mpimport;

import com.github.franckyi.cmpdl.task.TaskBase;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class DownloadFileTask extends TaskBase<Void> {

    private final String src;
    private final File dst;

    public DownloadFileTask(String src, File dst) {
        this.src = src;
        this.dst = dst;
    }

    @Override
    protected Void call0() throws IOException, URISyntaxException {
        updateTitle(String.format("Downloading %s", dst.getName()));
        URL url = new URL(src);
        URI uri = new URI(url.getProtocol(), url.getHost(), url.getFile(), null);
        if (!dst.exists()) dst.createNewFile();
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toASCIIString()).openConnection();
        int size = connection.getContentLength();
        BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
        FileOutputStream fis = new FileOutputStream(dst);
        byte[] buffer = new byte[1024];
        long dl = 0;
        int count;
        while ((count = bis.read(buffer, 0, 1024)) != -1 && !isCancelled()) {
            fis.write(buffer, 0, count);
            dl += count;
            this.updateProgress(dl, size);
        }
        fis.close();
        bis.close();
        return null;
    }
}
