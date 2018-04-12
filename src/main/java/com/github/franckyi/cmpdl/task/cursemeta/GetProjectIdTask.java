package com.github.franckyi.cmpdl.task.cursemeta;

import com.github.franckyi.cmpdl.task.TaskBase;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.json.JSONObject;
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
                return new JSONObject(doc.select("a.button--twitch").get(0).attr("data-action-value")).getInt("ProjectID");
            } else {
                return Integer.parseInt(doc.select("ul.cf-details.project-details").get(0).child(0).child(1).html());
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
