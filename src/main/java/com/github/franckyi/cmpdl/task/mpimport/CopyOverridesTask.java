package com.github.franckyi.cmpdl.task.mpimport;

import com.github.franckyi.cmpdl.task.TaskBase;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class CopyOverridesTask extends TaskBase<Void> {

    private final File srcFolder, dstFolder;

    public CopyOverridesTask(File srcFolder, File dstFolder) {
        this.srcFolder = srcFolder;
        this.dstFolder = dstFolder;
    }


    @Override
    protected Void call0() throws IOException {
        updateTitle("Copying overrides");
        copyFolder(srcFolder, dstFolder, StandardCopyOption.REPLACE_EXISTING);
        return null;
    }

    private static void copyFolder(File source, File dest, CopyOption... options) throws IOException {
        if (!dest.exists())
            dest.mkdirs();
        File[] contents = source.listFiles();
        if (contents != null) {
            for (File f : contents) {
                File newFile = new File(dest.getAbsolutePath() + File.separator + f.getName());
                if (f.isDirectory())
                    copyFolder(f, newFile, options);
                else
                    copyFile(f, newFile, options);
            }
        }
    }

    private static void copyFile(File source, File dest, CopyOption... options) throws IOException {
        Files.copy(source.toPath(), dest.toPath(), options);
    }

}
