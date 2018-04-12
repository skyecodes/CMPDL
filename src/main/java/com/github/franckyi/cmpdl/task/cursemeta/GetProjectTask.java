package com.github.franckyi.cmpdl.task.cursemeta;

import com.github.franckyi.cmpdl.CurseMetaAPI;
import com.github.franckyi.cmpdl.model.Project;
import com.github.franckyi.cmpdl.task.TaskBase;

public class GetProjectTask extends TaskBase<Project> {

    private final int projectId;

    public GetProjectTask(int projectId) {
        this.projectId = projectId;
    }

    @Override
    protected Project call0() {
        updateTitle(String.format("Getting project data for project %d", projectId));
        return CurseMetaAPI.getProject(projectId);
    }
}
