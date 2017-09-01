package com.github.franckyi.cmpdl.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

public class VerifyProjectTask extends CustomTask<Optional<String>> {

    private String fileID;
    private final String modpackURL;

    public VerifyProjectTask(String fileID, String modpackURL) {
        this.fileID = fileID;
        this.modpackURL = modpackURL;
    }

    @Override
    protected Optional<String> call0() throws Exception {
        log("Verifying " + modpackURL + ":" + fileID);
        String protocol, host, folder;
        boolean protocolValid, hostValid, folderValid, isCurse;
        URL url;
        try {
            url = new URL(modpackURL);
        } catch (MalformedURLException e) {
            log("The URL is malformed. This will not work !");
            trace(e);
            return Optional.empty();
        }
        protocol = url.getProtocol();
        host = url.getHost();
        folder = url.getFile();
        isCurse = host.equals("mods.curse.com");
        protocolValid = Arrays.asList("http", "https").contains(protocol);
        hostValid = Arrays.asList("mods.curse.com", "minecraft.curseforge.com", "www.feed-the-beast.com").contains(host);
        folderValid = isCurse ? folder.startsWith("/modpacks/minecraft/") && folder.split("/").length == 4 : folder.startsWith("/projects/") && folder.split("/").length == 3;
        if (!protocolValid || !hostValid || !folderValid) {
            log("The URL is incorrect !" + (!protocolValid ? "\n- Protocol invalid" : "") + (!hostValid ? "\n- Host invalid" : "") + (!folderValid ? "\n- Folder invalid" : ""));
            return Optional.empty();
        }
        if (fileID.isEmpty()) {
            fileID = "latest";
        }
        if (host.equals("www.feed-the-beast.com") && fileID.equals("latest")) {
            log("You must specify a file ID other than 'latest' for host www.feed-the-beast.com.");
            return Optional.empty();
        }
        try {
            return Optional.of(crawl(isCurse ? modpackURL + "/" + (fileID.equals("latest") ? "download" : fileID) : modpackURL + "/files/" + fileID + (fileID.equals("latest") ? "" : "/download")));
        } catch (IOException e) {
            log("The file doesn't exist or website is unreachable !");
            trace(e);
            return Optional.empty();
        }
    }
}
