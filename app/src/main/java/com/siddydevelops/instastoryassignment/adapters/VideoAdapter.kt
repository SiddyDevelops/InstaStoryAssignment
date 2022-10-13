package com.siddydevelops.instastoryassignment.adapters

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.siddydevelops.instastoryassignment.R
import com.siddydevelops.instastoryassignment.database.entities.ReelsItem
import com.siddydevelops.instastoryassignment.databinding.VideoItemLayoutBinding
import com.siddydevelops.instastoryassignment.reels.ReelsViewModel

class VideoAdapter(private val reelLikedListener: ReelLikedListener, private var viewModel: ReelsViewModel) :
    RecyclerView.Adapter<VideoAdapter.VideoHolder>() {

    private val allReels = ArrayList<ReelsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val binding =
            VideoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        Log.d("Items",allReels.toString())
        holder.bindTo(allReels[position])
    }

    override fun getItemCount(): Int {
        return allReels.size
    }

    fun updateList(newList: List<ReelsItem>) {
        allReels.clear()
        allReels.addAll(newList)
        notifyDataSetChanged()
    }

    inner class VideoHolder(private val binding: VideoItemLayoutBinding) :
        ViewHolder(binding.root) {
        fun bindTo(reelItem: ReelsItem) {
            val player = ExoPlayer.Builder(binding.root.context).build()
            binding.videoView.player = player
            binding.progressBar.visibility = View.VISIBLE

            if(reelItem.isLiked) {
                binding.likeBtn.text = "Liked!"
            }

            val mediaItem = MediaItem.fromUri(Uri.parse(reelItem.video_uri))
            player.addMediaItem(mediaItem)
            player.prepare()
            player.play()
            player.setAudioAttributes(AudioAttributes.DEFAULT, true)
            player.playWhenReady = true
            player.addListener(object : Player.Listener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {
                        ExoPlayer.STATE_READY -> {
                            binding.progressBar.visibility = View.GONE
                        }
                        ExoPlayer.STATE_ENDED -> {
                            //player.seekTo(0)
                            stopPlayer(player)
                        }
                    }
                }
            })

            binding.likeBtn.setOnClickListener {
                //reelLikedListener.onReelLikedListener(reelItem)
                viewModel.update(ReelsItem(reelItem.video_uri,true))
                Toast.makeText(binding.videoView.context,"Added liked to this item.", Toast.LENGTH_SHORT).show()
            }
        }

        private fun stopPlayer(player: ExoPlayer) {
            player.playWhenReady = false
            player.pause()
            player.stop()
            player.release()
        }
    }
}

interface ReelLikedListener {
    fun onReelLikedListener(item: ReelsItem)
}