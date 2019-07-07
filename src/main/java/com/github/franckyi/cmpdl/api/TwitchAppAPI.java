package com.github.franckyi.cmpdl.api;

import com.github.franckyi.cmpdl.api.response.Addon;
import com.github.franckyi.cmpdl.api.response.AddonFile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface TwitchAppAPI {

    @GET("addon/{addonId}")
    Call<Addon> getAddon(@Path("addonId") int addonId);

    @GET("addon/{addonId}/file/{fileId}")
    Call<AddonFile> getFile(@Path("addonId") int addonId, @Path("fileId") int fileId);

    @GET("addon/{addonId}/files")
    Call<List<AddonFile>> getAddonFiles(@Path("addonId") int addonId);

}
