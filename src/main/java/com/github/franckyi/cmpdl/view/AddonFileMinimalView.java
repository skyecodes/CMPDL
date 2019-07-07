package com.github.franckyi.cmpdl.view;

import com.github.franckyi.cmpdl.api.response.AddonFile;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AddonFileMinimalView extends ListCell<AddonFile> {

    private final VBox root = new VBox(5);
    private final Label fileName = new Label();
    private final Label gameVersion = new Label();
    private final Label fileType = new Label();

    public AddonFileMinimalView() {
        fileName.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 16));
        fileType.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize()));
        root.getChildren().addAll(fileName, gameVersion, fileType);
        VBox.setMargin(fileType, new Insets(0, 0, 0, 2));
    }

    @Override
    protected void updateItem(AddonFile item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            fileName.setText(item.getDisplayName());
            gameVersion.setText("for MC " + String.join(", ", item.getGameVersion()));
            fileType.setText(item.getReleaseType().toString());
            fileType.setBackground(new Background(new BackgroundFill(item.getReleaseType().getColor(), new CornerRadii(5), new Insets(-2, -5, -2, -5))));
            fileType.setTextFill(Color.WHITE);
            setGraphic(root);
        }
    }

}
