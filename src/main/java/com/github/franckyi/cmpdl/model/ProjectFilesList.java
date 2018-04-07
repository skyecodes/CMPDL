package com.github.franckyi.cmpdl.model;

import org.json.JSONArray;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectFilesList extends AbstractList<ProjectFile> {

    private final List<ProjectFile> list;

    public ProjectFilesList(JSONArray json) {
        list = new ArrayList<>(json.length());
        for (int i = 0; i < json.length(); i++) {
            list.add(new ProjectFile(json.getJSONObject(i)));
        }
        Collections.sort(list);
    }

    @Override
    public ProjectFile get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }
}
