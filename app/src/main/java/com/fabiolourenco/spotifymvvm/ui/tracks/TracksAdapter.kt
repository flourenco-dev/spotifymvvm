package com.fabiolourenco.spotifymvvm.ui.tracks

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_track.view.*
import com.fabiolourenco.spotifymvvm.R
import com.fabiolourenco.spotifymvvm.data.model.Track

class TracksAdapter constructor(private val tracks: List<Track>, private val listener: TrackListener)
    : RecyclerView.Adapter<TracksAdapter.ViewHolder>(), Filterable {

    private var filteredTracks = tracks

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = filteredTracks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(filteredTracks[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                filteredTracks = if (charString.isEmpty()) {
                    tracks

                } else {
                    val filteredList: MutableList<Track> = mutableListOf()
                    for (row in tracks) {

                        // name match condition
                        if (row.name?.toLowerCase()?.contains(charString.toLowerCase()) == true) {
                            filteredList.add(row)
                        }
                    }

                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredTracks
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredTracks = filterResults.values as? List<Track> ?: listOf()
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(item: Track) {
            itemView.trackName.text = item.name

            var artist = ""
            item.artists?.forEach {
                if (!TextUtils.isEmpty(it.name)) {
                    artist += it.name
                }
            }
            itemView.trackArtist.text = artist

            item.album?.let {
                itemView.trackAlbum.text = it.name

                if (it.images?.isNotEmpty() == true) {
                    Glide.with(itemView.context)
                        .load(it.images[0].url)
                        .placeholder(R.drawable.ic_album)
                        .into(itemView.trackImage)
                } else {
                    itemView.trackImage.setImageResource(R.drawable.ic_album)
                }
            }

            itemView.setOnClickListener {
                listener.onTrackClick(item)
            }
        }
    }

    interface TrackListener {
        fun onTrackClick(track: Track)
    }
}
