package com.github.franckyi.cmpdl.api.response;

import com.github.franckyi.cmpdl.api.IEnum;
import javafx.scene.paint.Color;

public enum ReleaseType implements IEnum {
    RELEASE(1, 20, 184, 102),
    BETA(2, 14, 115, 216),
    ALPHA(3, 126, 121, 139);

    private final int id;
    private final Color color;

    ReleaseType(int id, int r, int g, int b) {
        this.id = id;
        this.color = Color.rgb(r, g, b);
    }

    @Override
    public int toJson() {
        return id;
    }

    public Color getColor() {
        return color;
    }
}
