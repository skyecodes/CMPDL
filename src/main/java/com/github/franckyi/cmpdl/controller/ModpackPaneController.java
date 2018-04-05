package com.github.franckyi.cmpdl.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModpackPaneController implements Initializable {

    @FXML
    private TextField urlField;

    @FXML
    private TextField idField;

    @FXML
    private RadioButton urlButton;

    @FXML
    private RadioButton idButton;

    @FXML
    private ToggleGroup modpack;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        urlField.disableProperty().bind(urlButton.selectedProperty().not());
        idField.disableProperty().bind(idButton.selectedProperty().not());
    }

    public int getProjectID() {
        int projectId;
        if (modpack.getSelectedToggle() == urlButton) {
            try {
                URL url = new URL(urlField.getText());
                Document doc = Jsoup.parse(url, 10000);
                if (url.getHost().equals("www.curseforge.com")) {
                    projectId = new JSONObject(doc.select("a.download-button").get(0).attr("data-action-value")).getInt("ProjectID");
                } else {
                    projectId = Integer.parseInt(doc.select("ul.project-details").get(0).child(1).html());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "The URL is malformed", ButtonType.OK).show();
                return -1;
            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Connection error", ButtonType.OK).show();
                return -1;
            }
        } else {
            try {
                projectId = Integer.parseInt(idButton.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "The project ID must be an integer", ButtonType.OK).show();
                return -1;
            }
        }
        return projectId;
    }
}
