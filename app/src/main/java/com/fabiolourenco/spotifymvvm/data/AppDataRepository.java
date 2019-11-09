package com.fabiolourenco.spotifymvvm.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.fabiolourenco.spotifymvvm.data.api.ApiHelper;
import com.fabiolourenco.spotifymvvm.data.api.ApiHelperFactory;
import com.fabiolourenco.spotifymvvm.data.model.*;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;

class AppDataRepository implements DataRepository {

    private MutableLiveData<List<Playlist>> playlists;
    private MutableLiveData<String> playlistsMessage;

    private ApiHelper apiHelper;

    AppDataRepository() {
        playlists = new MutableLiveData<>();
        playlistsMessage = new MutableLiveData<>();
        apiHelper = ApiHelperFactory.getApiHelper();
    }

    @Override
    public void getFeaturedPlaylists() {
        apiHelper.getFeaturedPlaylists().enqueue(new Callback<FeaturedPlaylists>() {
            @Override
            public void onResponse(@NotNull Call<FeaturedPlaylists> call,
                                   @NotNull Response<FeaturedPlaylists> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PagingPlaylist paging = response.body().getPlaylists();
                    playlistsMessage.setValue(response.body().getMessage());

                    if (paging != null) {
                        List<Playlist> items = paging.getItems();

                        if (items != null && !items.isEmpty()) {
                            playlists.setValue(items);

                        } else {
                            Timber.w("Not playlist returned");
                            playlists.setValue(null);
                        }

                    } else {
                        Timber.w("Playlists wrapper was null");
                        playlists.setValue(null);
                    }

                } else {
                    Timber.w("Request was not successful or the body was empty");
                    playlists.setValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull Call<FeaturedPlaylists> call, @NotNull Throwable t) {
                Timber.w(t);
                playlists.setValue(null);
            }
        });
    }

    @NotNull
    @Override
    public LiveData<List<Track>> getPlaylistTracks(@NotNull String playlistId) {
        MutableLiveData<List<Track>> tracks = new MutableLiveData<>();
        apiHelper.getPlaylistTracks(playlistId).enqueue(new Callback<PagingTrack>() {
            @Override
            public void onResponse(@NotNull Call<PagingTrack> call,
                                   @NotNull Response<PagingTrack> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ItemTrack> items = response.body().getItems();

                    if (items != null && !items.isEmpty()) {
                        List<Track> trackList = new ArrayList<>();
                        for (ItemTrack item :
                                items) {
                            if (item.getTrack() != null) {
                                trackList.add(item.getTrack());
                            }
                        }
                        tracks.setValue(trackList);

                    } else {
                        Timber.w("Not tracks returned");
                        tracks.setValue(null);
                    }

                } else {
                    Timber.w("Request was not successful or the body was empty");
                    tracks.setValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull Call<PagingTrack> call, @NotNull Throwable t) {
                Timber.w(t);
                tracks.setValue(null);
            }
        });

        return tracks;
    }

//    @Override
//    public void getPlaylistTracks(@NotNull String playlistId, int limit, int offset) {
//
//    }

    @NotNull
    @Override
    public LiveData<List<Playlist>> getPlaylistsObservable() {
        return playlists;
    }

    @NotNull
    @Override
    public LiveData<String> getPlaylistsMessageObservable() {
        return playlistsMessage;
    }

    @Override
    public void setToken(@NotNull String token, int expiresIn) {
        apiHelper.setToken(token, expiresIn);
    }
}
