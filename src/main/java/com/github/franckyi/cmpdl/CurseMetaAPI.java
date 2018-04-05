package com.github.franckyi.cmpdl;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

public class CurseMetaAPI {

    private static final String URL = "http://cursemeta.dries007.net/api/v2/direct";

    public static JSONObject getProject(int projectId) throws UnirestException {
        return get("/GetAddOn", projectId).getObject();
    }

    public static JSONArray getProjectFiles(int projectId) throws UnirestException {
        return get("/GetAllFilesForAddOn", projectId).getArray();
    }

    public static JSONObject getProjectFile(int projectId, int fileId) throws UnirestException {
        return get("/GetAddOnFile", projectId, fileId).getObject();
    }

    private static JsonNode get(String path, Object... args) throws UnirestException {
        StringBuilder sb = new StringBuilder(URL + path);
        for (Object o : args) {
            sb.append("/").append(o);
        }
        return Unirest.get(sb.toString()).asJson().getBody();
    }

}
