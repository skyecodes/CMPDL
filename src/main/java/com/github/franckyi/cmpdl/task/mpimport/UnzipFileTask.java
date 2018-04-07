package com.github.franckyi.cmpdl.task.mpimport;

import com.github.franckyi.cmpdl.task.TaskBase;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipFileTask extends TaskBase<Void> {

    private final File src, dst;

    public UnzipFileTask(File src, File dst) {
        this.src = src;
        this.dst = dst;
    }

    @Override
    protected Void call0() throws Throwable {
        updateTitle(String.format("Unzipping %s", src.getName()));
        FileInputStream is = new FileInputStream(src.getCanonicalFile());
        FileChannel channel = is.getChannel();
        ZipEntry ze;
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is))) {
            while ((ze = zis.getNextEntry()) != null && !isCancelled()) {
                File f = new File(dst.getCanonicalPath(), ze.getName());
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
                        long length = src.length();
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
}
