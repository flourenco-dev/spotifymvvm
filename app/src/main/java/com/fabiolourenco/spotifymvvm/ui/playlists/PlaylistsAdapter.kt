package com.fabiolourenco.spotifymvvm.ui.playlists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_playlist.view.*
import com.fabiolourenco.spotifymvvm.R
import com.fabiolourenco.spotifymvvm.data.model.Playlist

class PlaylistsAdapter constructor(private val playlists: List<Playlist>, private val listener: PlaylistListener)
    : RecyclerView.Adapter<PlaylistsAdapter.ViewHolder>(), Filterable {

    private var filteredPlaylists = playlists

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = filteredPlaylists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(filteredPlaylists[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                filteredPlaylists = if (charString.isEmpty()) {
                    playlists

                } else {
                    val filteredList: MutableList<Playlist> = mutableListOf()
                    for (row in playlists) {

                        // name match condition
                        if (row.name?.toLowerCase()?.contains(charString.toLowerCase()) == true) {
                            filteredList.add(row)
                        }
                    }

                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredPlaylists
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredPlaylists = filterResults.values as? List<Playlist> ?: listOf()
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(item: Playlist) {
            if (item.images?.isNotEmpty() == true) {
                Glide.with(itemView.context)
                    .load(item.images[0].url)
                    .placeholder(R.drawable.ic_list)
                    .into(itemView.playlistImage)
            } else {
                itemView.playlistImage.setImageResource(R.drawable.ic_list)
            }

            itemView.playlistName.text = item.name

            itemView.setOnClickListener {
                listener.onPlaylistClick(item)
            }

            itemView.playlistPlayButton.setOnClickListener {
                listener.onPlayClick(item)
            }
        }
    }

    interface PlaylistListener {
        fun onPlaylistClick(playlist: Playlist)
        fun onPlayClick(playlist: Playlist)
    }
}