package com.github.franckyi.cmpdl;

import com.github.franckyi.cmpdl.model.Project;
import com.github.franckyi.cmpdl.model.ProjectFile;
import com.github.franckyi.cmpdl.model.ProjectFilesList;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CurseMetaAPI {

    private static final OkHttpClient CLIENT = new OkHttpClient();
    // Docs: https://staging_cursemeta.dries007.net/docs
    private static final String URL = "https://staging_cursemeta.dries007.net/api/v3/direct";

    public static Project getProject(int projectId) {
        try {
            return new Project(new JSONObject(get("/addon", projectId)));
        } catch (JSONException e) {
            e.printStackTrace();
            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, String.format("Unknown project (%d)", projectId), ButtonType.OK).show());
            return null;
        }
    }

    public static ProjectFilesList getProjectFiles(int projectId) {
        try {
            return new ProjectFilesList(new JSONArray(get("/addon", projectId, "/files")));
        } catch (JSONException e) {
            e.printStackTrace();
            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, String.format("Unknown project (%d)", projectId), ButtonType.OK).show());
            return null;
        }
    }

    public static ProjectFile getProjectFile(int projectId, int fileId) {
        try {
            return new ProjectFile(new JSONObject(get("/addon", projectId, "/file", fileId)));
        } catch (JSONException e) {
            e.printStackTrace();
            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, String.format("Unknown project file (%d:%d)", projectId, fileId), ButtonType.OK).show());
            return null;
        }
    }

    private static String get(String path, Object... args) {
        StringBuilder sb = new StringBuilder(URL + path);
        for (Object o : args) {
            sb.append("/").append(o);
        }
        Request request = new Request.Builder().header("User-Agent", CMPDL.USER_AGENT).url(sb.toString()).get().build();
        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
