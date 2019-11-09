package com.fabiolourenco.spotifymvvm.data.api;

import com.fabiolourenco.spotifymvvm.data.model.FeaturedPlaylists;
import com.fabiolourenco.spotifymvvm.data.model.PagingTrack;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

class AppApiHelper implements ApiHelper {

    private SpotifyApi spotifyApi;
    private Map<String, String> headers;

    private String token;
    private long tokenExpireDate;

    AppApiHelper() {
        Retrofit spotifyRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        spotifyApi = spotifyRetrofit.create(SpotifyApi.class);
        constructHeaders();
    }

    @NotNull
    @Override
    public Call<FeaturedPlaylists> getFeaturedPlaylists() {
        constructHeaders();
        return spotifyApi.getFeaturedPlaylists(headers);
    }

    @NotNull
    @Override
    public Call<PagingTrack> getPlaylistTracks(@NotNull String playlistId) {
        constructHeaders();
        return spotifyApi.getPlaylistTracks(headers, playlistId);
    }

    @NotNull
    @Override
    public Call<PagingTrack> getPlaylistTracks(@NotNull String playlistId, int limit, int offset) {
        if (Calendar.getInstance().getTimeInMillis() <= tokenExpireDate) {
            constructHeaders();
            return spotifyApi.getPlaylistTracks(headers,playlistId, limit, offset);

        } else {
            // TODO: 2019-06-06 - Do Token Refresh - Maybe move this to DataRepository
            return spotifyApi.getPlaylistTracks(headers, playlistId, limit, offset);
        }
    }

    @Override
    public void setToken(@NotNull String token, int expiresIn) {
        this.token = token;
        tokenExpireDate = Calendar.getInstance().getTimeInMillis() + expiresIn * 1000;
    }

    private void constructHeaders() {
        headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + token);
    }
}
