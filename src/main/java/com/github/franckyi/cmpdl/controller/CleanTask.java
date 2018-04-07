package com.github.franckyi.cmpdl.controller;

import com.github.franckyi.cmpdl.task.TaskBase;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class CleanTask extends TaskBase<Void> {

    private final File temp;

    public CleanTask(File temp) {
        this.temp = temp;
    }

    @Override
    protected Void call0() throws Throwable {
        updateTitle("Cleaning");
        Files.walkFileTree(temp.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
        return null;
    }
}
