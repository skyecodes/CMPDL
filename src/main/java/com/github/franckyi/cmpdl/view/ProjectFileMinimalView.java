package com.github.franckyi.cmpdl.view;

import com.github.franckyi.cmpdl.model.IProjectFile;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ProjectFileMinimalView extends ListCell<IProjectFile> {

    private final VBox root = new VBox(5);
    private final Label fileName = new Label();
    private final Label gameVersion = new Label();
    private final Label fileType = new Label();

    public ProjectFileMinimalView() {
        fileName.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 16));
        fileType.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize()));
        root.getChildren().addAll(fileName, gameVersion, fileType);
    }

    @Override
    protected void updateItem(IProjectFile item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            fileName.setText(item.getFileName());
            gameVersion.setText("for MC " + item.getGameVersion());
            fileType.setText(item.getFileType());
            fileType.setTextFill(item.getColor());
            setGraphic(root);
        }
    }

}
