package com.github.franckyi.cmpdl.task.api;

import com.github.franckyi.cmpdl.task.TaskBase;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GetProjectIdTask extends TaskBase<Integer> {

    private final String projectUrl;

    public GetProjectIdTask(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    @Override
    protected Integer call0() {
        updateTitle(String.format("Getting project ID for %s", projectUrl));
        try {
            URL url = new URL(projectUrl);
            Document doc = Jsoup.parse(url, 10000);
            if (url.getHost().equals("www.curseforge.com")) {
                return Integer.parseInt(doc.select("div.mb-3:nth-child(2) > div:nth-child(1) > span:nth-child(2)").html());
            } else {
                return Integer.parseInt(doc.select(".fa-icon-twitch").attr("data-id"));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Malformed URL", ButtonType.OK).show());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Connection error", ButtonType.OK).show());
            return null;
        }
    }

}
