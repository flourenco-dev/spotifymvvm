package com.fabiolourenco.spotifymvvm.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.fabiolourenco.spotifymvvm.R
import com.fabiolourenco.spotifymvvm.ui.playlists.PlaylistsActivity
import com.fabiolourenco.spotifymvvm.utils.AppConstants
import com.google.android.material.snackbar.Snackbar
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

        val request = AuthenticationRequest.Builder(
                AppConstants.SPOTIFY_CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                AppConstants.getSpotifyRedirectUri(this).toString())
            .setScopes(arrayOf("streaming"))
            .setShowDialog(true)
            .build()

        AuthenticationClient.openLoginActivity(this, AppConstants.SPOTIFY_LOGIN_REQUEST_CODE, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == AppConstants.SPOTIFY_LOGIN_REQUEST_CODE) {
            AuthenticationClient.getResponse(resultCode, data).also {
                when (it.type) {
                    AuthenticationResponse.Type.TOKEN -> {
                        splashViewModel.updateToken(it.accessToken, it.expiresIn)
                        startActivity(Intent(this, PlaylistsActivity::class.java))
                        finish()
                    }

                    AuthenticationResponse.Type.ERROR-> {
                        Snackbar.make(splashView, "ERROR: ${it.error}", Snackbar.LENGTH_SHORT).show()
                        splashViewModel.updateToken("", 0)

                        splashView.postDelayed({
                            finish()
                        }, 500)
                    }

                    else -> {
                        Snackbar.make(splashView, "else", Snackbar.LENGTH_SHORT).show()
                        splashViewModel.updateToken("", 0)

                        splashView.postDelayed({
                            finish()
                        }, 500)
                    }
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
