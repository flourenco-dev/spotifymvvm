package com.fabiolourenco.spotifymvvm.ui.playlists

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.fabiolourenco.spotifymvvm.App
import com.fabiolourenco.spotifymvvm.R
import com.fabiolourenco.spotifymvvm.data.model.Playlist
import com.fabiolourenco.spotifymvvm.ui.tracks.TracksActivity
import com.fabiolourenco.spotifymvvm.utils.AppConstants
import com.google.android.material.snackbar.Snackbar
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.android.synthetic.main.activity_playlists.*

class PlaylistsActivity : AppCompatActivity(), PlaylistsAdapter.PlaylistListener {

    private lateinit var playlistsViewModel: PlaylistsViewModel
    private lateinit var adapter: PlaylistsAdapter

    private val playlists: MutableList<Playlist> = mutableListOf()

    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlists)

        adapter = PlaylistsAdapter(playlists, this)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        playlistsViewModel = ViewModelProviders.of(this).get(PlaylistsViewModel::class.java)
        playlistsViewModel.playlists.observe(this, Observer {
            loadingView.isVisible = false
            updateListUi(it ?: listOf())
        })
        playlistsViewModel.playlistsName.observe(this, Observer {
            supportActionBar?.title = it
        })

        loadInitialUi()
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as App).spotifyAppRemote?.playerApi?.pause()
        SpotifyAppRemote.disconnect((application as App).spotifyAppRemote)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView?.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchView?.maxWidth = Integer.MAX_VALUE

        // listening to search query text change
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                adapter.filter.filter(query)
                return false
            }
        })
        return true
    }

    override fun onBackPressed() {
        if (searchView?.isIconified == false) {
            searchView?.isIconified = true

        } else {
            super.onBackPressed()
        }
    }

    override fun onPlaylistClick(playlist: Playlist) {
        val intent = Intent(this, TracksActivity::class.java)
        intent.putExtra(AppConstants.EXTRA_PLAYLIST_ID, playlist.id)
        intent.putExtra(AppConstants.EXTRA_PLAYLIST_NAME, playlist.name)
        startActivity(intent)
    }

    override fun onPlayClick(playlist: Playlist) {
        playlist.uri?.let {
            Snackbar.make(recyclerView, playlist.name ?: "", Snackbar.LENGTH_INDEFINITE)
                .setAction("Pause") {
                    (application as App).spotifyAppRemote?.playerApi?.pause()
                }
                .show()
            (application as App).spotifyAppRemote?.playerApi?.play(it)
        }
    }


    private fun loadInitialUi() {
        loadingView.isVisible = true
        recyclerView.isInvisible = true
        emptyView.isVisible = false

        playlistsViewModel.getPlaylists()
    }

    private fun updateListUi(list: List<Playlist>) {
        if (list.isEmpty()) {
            emptyView.isVisible = true
            recyclerView.isInvisible = true

        } else {
            emptyView.isVisible = false
            recyclerView.isVisible = true
        }

        playlists.clear()
        playlists.addAll(list)
        adapter.notifyDataSetChanged()
    }
}
