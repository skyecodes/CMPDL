package com.github.franckyi.cmpdl.api.response;

import com.github.franckyi.cmpdl.api.IBean;

public class Category implements IBean {

    private int categoryId;
    private String name;
    private String avatarUrl;

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

}
