package com.fabiolourenco.spotifymvvm.ui.tracks

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fabiolourenco.spotifymvvm.App
import com.fabiolourenco.spotifymvvm.R
import com.fabiolourenco.spotifymvvm.data.model.Track
import com.fabiolourenco.spotifymvvm.utils.AppConstants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_tracks.*
import timber.log.Timber


class TracksActivity : AppCompatActivity(), TracksAdapter.TrackListener {

    private lateinit var tracksViewModel: TracksViewModel
    private lateinit var adapter: TracksAdapter

    private val tracks: MutableList<Track> = mutableListOf()
    private var playlistId: String? = null
    private var playlistName: String? = null

    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracks)

        playlistId = intent?.getStringExtra(AppConstants.EXTRA_PLAYLIST_ID)
        playlistName = intent?.getStringExtra(AppConstants.EXTRA_PLAYLIST_NAME)
        if (playlistId == null || playlistName == null) {
            Timber.w("Something went wrong, there's no playlist ID")
            finish()
            return
        }
        supportActionBar?.title = playlistName

        adapter = TracksAdapter(tracks, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        tracksViewModel = ViewModelProviders.of(this).get(TracksViewModel::class.java)
        tracksViewModel.tracks.observe(this, Observer {
            loadingView.isVisible = false
            updateListUi(it ?: listOf())
        })

        loadInitialUi()
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as App).spotifyAppRemote?.playerApi?.pause()
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

    override fun onTrackClick(track: Track) {
        Snackbar.make(recyclerView, track.name ?: "", Snackbar.LENGTH_INDEFINITE)
            .setAction("Pause") {
                (application as App).spotifyAppRemote?.playerApi?.pause()
            }
            .show()
        (application as App).spotifyAppRemote?.playerApi?.play(track.uri)
    }

    private fun loadInitialUi() {
        loadingView.isVisible = true
        recyclerView.isInvisible = true
        emptyView.isVisible = false

        tracksViewModel.getTracks(playlistId!!)
    }

    private fun updateListUi(list: List<Track>) {
        if (list.isEmpty()) {
            emptyView.isVisible = true
            recyclerView.isInvisible = true

        } else {
            emptyView.isVisible = false
            recyclerView.isVisible = true
        }

        tracks.clear()
        tracks.addAll(list)
        adapter.notifyDataSetChanged()
    }
}
