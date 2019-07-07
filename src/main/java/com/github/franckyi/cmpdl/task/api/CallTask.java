package com.github.franckyi.cmpdl.task.api;

import com.github.franckyi.cmpdl.task.TaskBase;
import retrofit2.Call;

import java.io.IOException;

public class CallTask<T> extends TaskBase<T> {

    private final String title;
    private final Call<T> call;

    public CallTask(String title, Call<T> call) {
        this.title = title;
        this.call = call;
    }

    @Override
    protected T call0() throws IOException {
        updateTitle(title);
        return call.execute().body();
    }
}
