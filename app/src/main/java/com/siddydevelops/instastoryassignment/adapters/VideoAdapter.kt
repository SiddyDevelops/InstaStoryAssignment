package com.siddydevelops.instastoryassignment.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.siddydevelops.instastoryassignment.database.entities.ReelsItem
import com.siddydevelops.instastoryassignment.databinding.VideoItemLayoutBinding
import com.siddydevelops.instastoryassignment.models.ExoPlayerItem
import com.siddydevelops.instastoryassignment.viewModels.ReelsViewModel

class VideoAdapter(private var context: Context,
                   private var videoPreparedListener: OnVideoPreparedListener,
                   private var reelStatus: ChangeLikeReelStatus) :
    RecyclerView.Adapter<VideoAdapter.VideoHolder>() {

    private val allReels = ArrayList<ReelsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val binding =
            VideoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoHolder(binding,videoPreparedListener)
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.setVideoPath(allReels[position])
    }

    override fun getItemCount(): Int {
        return allReels.size
    }

    fun updateList(newList: List<ReelsItem>) {
        allReels.clear()
        allReels.addAll(newList)
        notifyDataSetChanged()
    }

    inner class VideoHolder(private val binding: VideoItemLayoutBinding,
                            private var videoPreparedListener: OnVideoPreparedListener) :
        ViewHolder(binding.root) {

        private lateinit var exoPlayer: ExoPlayer
        private lateinit var mediaSource: MediaSource

        fun setVideoPath(reelItem: ReelsItem) {
            if(reelItem.isLiked) {
                binding.likeSwitch.isChecked = true
            }

            exoPlayer = ExoPlayer.Builder(context).build()
            exoPlayer.addListener(object : Player.Listener{
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Log.d("Error",error.toString())
                    Toast.makeText(context,"Can't play this video.",Toast.LENGTH_SHORT).show()
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if(playbackState == Player.STATE_BUFFERING) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else if(playbackState == Player.STATE_READY) {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            })

            binding.videoView.player = exoPlayer
            exoPlayer.seekTo(0)
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

            val dataSourceFactory = DefaultDataSource.Factory(context)
            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(reelItem.video_uri)))
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()

            if(absoluteAdapterPosition == 0) {
                exoPlayer.playWhenReady = true
                exoPlayer.play()
            }

            videoPreparedListener.onVideoPreparedListener(ExoPlayerItem(exoPlayer, absoluteAdapterPosition))

            binding.likeSwitch.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) {
                    reelStatus.onToggleReelLike(ReelsItem(reelItem.video_uri,true))
                    Toast.makeText(binding.videoView.context,"Added liked to this item.", Toast.LENGTH_SHORT).show()
                } else {
                    reelStatus.onToggleReelLike(ReelsItem(reelItem.video_uri,false))
                    Toast.makeText(binding.videoView.context,"Added liked to this item.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    interface OnVideoPreparedListener {
        fun onVideoPreparedListener(exoPlayerItem: ExoPlayerItem)
    }

    interface ChangeLikeReelStatus {
        fun onToggleReelLike(reelItem: ReelsItem)
    }
}