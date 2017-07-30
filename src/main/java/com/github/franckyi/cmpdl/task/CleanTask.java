package com.github.franckyi.cmpdl.task;

import com.github.franckyi.cmpdl.CMPDL;
import com.github.franckyi.cmpdl.ManifestJson;

import java.io.File;
import java.util.Arrays;

public class CleanTask extends CustomTask<Void> {

    private ManifestJson manifest;

    public CleanTask(ManifestJson manifest) {
        this.manifest = manifest;
    }

    public CleanTask() {
    }

    @Override
    protected Void call0() throws Exception {
        log("Cleaning up...");
        del(new File(CMPDL.getTempDirectory()));
        return null;
    }

    @Override
    protected void succeeded() {
        if (manifest != null) {
            log("!#! RECOMMENDED FORGE VERSION : " + manifest.getForgeVersion());
            log("!#! A newer version should also work.");
            log("!#! You must install it manually if you're using MultiMC !");
        }
        getController().reset();
        log(CMPDL.exceptions.isEmpty() ? "Done !" : "Done with " + CMPDL.exceptions.size() + " error(s). See log for more info.");
        log("----------------");
    }

    private void del(File file) {
        if (file.isDirectory()) Arrays.asList(file.listFiles()).forEach(this::del);
        file.delete();
    }
}
