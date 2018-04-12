package com.github.franckyi.cmpdl.task.cursemeta;

import com.github.franckyi.cmpdl.CurseMetaAPI;
import com.github.franckyi.cmpdl.model.ProjectFilesList;
import com.github.franckyi.cmpdl.task.TaskBase;

public class GetProjectFilesTask extends TaskBase<ProjectFilesList> {

    private final int projectId;

    public GetProjectFilesTask(int projectId) {
        this.projectId = projectId;
    }

    @Override
    protected ProjectFilesList call0() {
        updateTitle(String.format("Getting project files for project %d", projectId));
        return CurseMetaAPI.getProjectFiles(projectId);
    }
}
