package com.github.franckyi.cmpdl.task;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipModpackTask extends CustomTask<Void> {

    private final File zipfile;
    private final File folder;

    UnzipModpackTask(File file) {
        this.zipfile = file;
        this.folder = file.getParentFile();
    }

    @Override
    protected Void call0() throws Exception {
        log("> Unzipping file at " + folder.getAbsolutePath());
        FileInputStream is = new FileInputStream(zipfile.getCanonicalFile());
        FileChannel channel = is.getChannel();
        ZipEntry ze;
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is))) {
            while ((ze = zis.getNextEntry()) != null && !isCancelled()) {
                File f = new File(folder.getCanonicalPath(), ze.getName());
                if (ze.isDirectory()) {
                    f.mkdirs();
                    continue;
                }
                f.getParentFile().mkdirs();
                try {
                    try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(f))) {
                        final byte[] buf = new byte[1024];
                        int bytesRead;
                        long nread = 0L;
                        long length = zipfile.length();
                        while (-1 != (bytesRead = zis.read(buf)) && !isCancelled()) {
                            fos.write(buf, 0, bytesRead);
                            nread += bytesRead;
                            updateProgress(channel.position(), length);
                        }
                    }
                } catch (final IOException ioe) {
                    f.delete();
                    throw ioe;
                }
            }
        }
        updateProgress(0, 0);
        return null;
    }

    @Override
    protected void succeeded() {
        DownloadModsTask task = new DownloadModsTask(folder);
        getController().setPrimaryProgress(task, "Step 2/3 : Downloading dependencies");
        new Thread(task).start();
        log("> Unzipping succeeded !");
    }
}
