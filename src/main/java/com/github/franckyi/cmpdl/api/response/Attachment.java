package com.github.franckyi.cmpdl.api.response;

import com.github.franckyi.cmpdl.api.IBean;

public class Attachment implements IBean {

    private boolean isDefault;
    private String thumbnailUrl;

    public boolean isDefault() {
        return isDefault;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

}
