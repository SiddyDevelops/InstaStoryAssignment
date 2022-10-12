package com.siddydevelops.instastoryassignment.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.siddydevelops.instastoryassignment.databinding.VideoItemLayoutBinding

class VideoAdapter(private val videoList: ArrayList<String>) : RecyclerView.Adapter<VideoAdapter.VideoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val binding = VideoItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VideoHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.bindTo(videoList[position])
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    open class VideoHolder(private val binding: VideoItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(videoUrl: String) {
            val player = ExoPlayer.Builder(binding.root.context).build()
            binding.videoView.player = player
            binding.progressBar.visibility = View.VISIBLE

            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            player.addMediaItem(mediaItem)
            player.prepare()
            player.play()
            player.setAudioAttributes(AudioAttributes.DEFAULT,true)
            player.playWhenReady = true
            player.addListener(object : Player.Listener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {
                        ExoPlayer.STATE_READY -> {
                            binding.progressBar.visibility = View.GONE
                        }
                        ExoPlayer.STATE_ENDED -> {
                            player.seekTo(0)
                        }
                        Player.STATE_BUFFERING -> {
                            TODO()
                        }
                        Player.STATE_IDLE -> {
                            TODO()
                        }
                    }
                }
            })
        }
    }
}