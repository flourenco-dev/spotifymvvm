package com.fabiolourenco.spotifymvvm.utils;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.fabiolourenco.spotifymvvm.R;

public final class AppConstants {

    public static final String EXTRA_PLAYLIST_ID = "playlistId";
    public static final String EXTRA_PLAYLIST_NAME = "playlistName";

    public static final String SPOTIFY_CLIENT_ID = "e20dbdb6266f40d9bcf8362a47a54df1";
    public static final int SPOTIFY_LOGIN_REQUEST_CODE = 0x1;

    public static Uri getSpotifyRedirectUri(@NonNull Context context) {
        return new Uri.Builder()
                .scheme(context.getString(R.string.spotify_scheme))
                .authority(context.getString(R.string.spotify_host))
                .build();
    }
}
