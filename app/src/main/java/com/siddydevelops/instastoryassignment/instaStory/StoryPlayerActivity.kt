package com.siddydevelops.instastoryassignment.instaStory

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.siddydevelops.instastoryassignment.databinding.ActivityStoryPlayerBinding
import com.siddydevelops.instastoryassignment.user.UserData
import jp.shts.android.storiesprogressview.StoriesProgressView


class StoryPlayerActivity : AppCompatActivity(), StoriesProgressView.StoriesListener {

    private lateinit var binding: ActivityStoryPlayerBinding
    private var imageList: ArrayList<UserData> = arrayListOf()

    private var pressTime = 0L
    private var limit = SEC_10
    private var counter = 0

    private lateinit var player: ExoPlayer

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener =
        OnTouchListener { _, motionEvent ->
//            when (motionEvent.action) {
//                MotionEvent.ACTION_DOWN -> {
//
//                    // on action down when we press our screen
//                    // the story will pause for specific time.
//                    pressTime = System.currentTimeMillis()
//
//                    // on below line we are pausing our indicator.
//                    binding.stories.pause()
//                    return@OnTouchListener false
//                }
//                MotionEvent.ACTION_UP -> {
//
//                    /*
//                    in action up case when user do not touches
//                    screen this method will skip to next image.
//                    */
//                    val now = System.currentTimeMillis()
//
//                    // on below line we are resuming our progress bar for status.
//                    //binding.stories.resume()
//
//                    // on below line we are returning if the limit < now - presstime
//                    return@OnTouchListener limit < now - pressTime
//                }
//            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryPlayerBinding.inflate(layoutInflater)
        val root = binding.root
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(root)

        // Get Stories From Intent
        imageList = intent.getParcelableArrayListExtra<UserData>("IMAGEURLS")!!

        binding.stories.setStoriesCount(imageList.size)
        binding.stories.setStoriesListener(this)
        binding.stories.startStories(counter)

        player = ExoPlayer.Builder(this).build()
        binding.video.player = player

        if (isImageFile(imageList[counter])) {
            glideImage(
                imageList[counter].image
            )
        } else {
            previewVideo(imageList[counter].image)
        }

        binding.reverse.setOnClickListener {
            binding.stories.reverse()
        }

        binding.likeBtn.setOnClickListener {
            Toast.makeText(this,"Added like to the story.",Toast.LENGTH_SHORT).show()
        }

        binding.reverse.setOnTouchListener(onTouchListener)

        binding.skip.setOnClickListener {
            binding.stories.skip()
        }

        binding.skip.setOnTouchListener(onTouchListener)
    }

    override fun onNext() {
        player.pause()
        ++counter
        if (isImageFile(imageList[counter])) {
            limit = SEC_10
            glideImage(
                imageList[counter].image
            )
        } else {
            previewVideo(imageList[counter].image)
        }
    }

    override fun onPrev() {
        player.pause()
        if (counter - 1 < 0) return
        --counter
        if(isImageFile(imageList[counter])) {
            limit = SEC_10
            glideImage(
                imageList[counter].image
            )
        } else {
            previewVideo(imageList[counter].image)
        }
    }

    override fun onComplete() {
        finish()
    }

    override fun onDestroy() {
        binding.stories.destroy()
        super.onDestroy()
    }

    private fun glideImage(
        image: String
    ) {
        binding.image.visibility = View.VISIBLE
        binding.video.visibility = View.GONE

        Glide.with(this).load(image).into(binding.image)
        binding.stories.setStoryDuration(SEC_10)
        binding.stories.startStories(counter)
        binding.stories.pause()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun previewVideo(videoUri: String) {
        binding.image.visibility = View.GONE
        binding.video.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.stories.pause()

        val mediaItem = MediaItem.fromUri(videoUri)
        player.addMediaItem(mediaItem)
        player.prepare()
        player.play()
        player.setAudioAttributes(AudioAttributes.DEFAULT,  /* handleAudioFocus= */true)
        player.playWhenReady = true
        player.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == ExoPlayer.STATE_READY) {
                    binding.stories.setStoryDuration(player.getDuration())
                    binding.progressBar.visibility = View.GONE
                    //binding.stories.resume()
                }
            }
        })
    }

    private fun isImageFile(data: UserData): Boolean {
        return (data.type == "image")
    }

    companion object {
        private const val SEC_10 = 10000L
        private const val SEC_30 = 30000L
    }
}