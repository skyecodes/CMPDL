package com.github.franckyi.cmpdl.task.cursemeta;

import com.github.franckyi.cmpdl.CurseMetaAPI;
import com.github.franckyi.cmpdl.model.ProjectFile;
import com.github.franckyi.cmpdl.task.TaskBase;

public class GetProjectFileTask extends TaskBase<ProjectFile> {

    private final int projectId, fileId;

    public GetProjectFileTask(int projectId, int fileId) {
        this.projectId = projectId;
        this.fileId = fileId;
    }

    @Override
    protected ProjectFile call0() {
        updateTitle(String.format("Getting project file %d:%d", projectId, fileId));
        return CurseMetaAPI.getProjectFile(projectId, fileId);
    }
}
