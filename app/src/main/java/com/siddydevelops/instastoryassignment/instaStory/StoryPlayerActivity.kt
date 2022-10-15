package com.siddydevelops.instastoryassignment.instaStory

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.siddydevelops.instastoryassignment.databinding.ActivityStoryPlayerBinding
import com.siddydevelops.instastoryassignment.progressBar.MultiProgressBar
import com.siddydevelops.instastoryassignment.user.UserData


class StoryPlayerActivity : AppCompatActivity(), MultiProgressBar.ProgressFinishListener,
    MultiProgressBar.ProgressStepChangeListener {

    private val binding by lazy { ActivityStoryPlayerBinding.inflate(layoutInflater) }
    private var imageList: ArrayList<UserData> = arrayListOf()
    private var counter = 0
    private lateinit var player :ExoPlayer
    private var pressTime = 0L
    private var limit = 500L

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener =
        OnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_BUTTON_PRESS -> {

                    // on action down when we press our screen
                    // the story will pause for specific time.
                    pressTime = System.currentTimeMillis()

                    // on below line we are pausing our indicator.
                    binding.stories.pause()
                    return@OnTouchListener false
                }
                MotionEvent.ACTION_BUTTON_RELEASE -> {

                    // in action up case when user do not touches
                    // screen this method will skip to next image.
                    val now = System.currentTimeMillis()

                    // on below line we are resuming our progress bar for status.
                    binding.stories.start()

                    // on below line we are returning if the limit < now - presstime
                    return@OnTouchListener limit < now - pressTime
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)

       // Get Stories From Intent
        imageList = intent.getParcelableArrayListExtra("IMAGEURLS")!!

        binding.stories.setProgressStepsCount(imageList.size)
        binding.stories.setListener(this)
        binding.stories.start()
        if (isImageFile(imageList[counter])) {
            glideImage(imageList[counter].image)
        } else {
            previewVideo(imageList[counter].image)
        }

        binding.likeBtn.setOnClickListener {
            Toast.makeText(this, "Added like to the story.", Toast.LENGTH_SHORT).show()
        }

        binding.reverse.setOnTouchListener(onTouchListener)
        binding.skip.setOnTouchListener(onTouchListener)

        binding.skip.setOnClickListener {
            if(counter < imageList.size -1) {
                binding.stories.next()
                ++counter
                onNext()
            }else{
                finish()
                stopPlayer()
            }
        }

        binding.reverse.setOnClickListener {
            binding.stories.previous()
            onPrev()
        }
    }

    private fun onNext() {
        stopPlayer()
        if (isImageFile(imageList[counter])) {
            glideImage(
                imageList[counter].image
            )
        } else {
            previewVideo(imageList[counter].image)
        }
    }

    private fun onPrev() {
        stopPlayer()
        if (counter - 1 < 0) return
        --counter
        if (isImageFile(imageList[counter])) {
            glideImage(
                imageList[counter].image
            )
        } else {
            previewVideo(imageList[counter].image)
        }
    }


    private fun glideImage(image: String) {
        binding.stories.pause()
        binding.progressBar.visibility = View.VISIBLE
        binding.image.visibility = View.VISIBLE
        binding.video.visibility = View.GONE
        binding.stories.setSingleDisplayTime(5F)

        Glide.with(this)
            .load(image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(applicationContext,"Image failed to load!",Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    binding.stories.start()
                    return false
                }
            })
            .into(binding.image)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun previewVideo(videoUri: String) {
        binding.image.visibility = View.GONE
        binding.video.visibility = View.VISIBLE
        binding.stories.pause()
        player = ExoPlayer.Builder(this).build()
        binding.video.player = player
        val mediaItem = MediaItem.fromUri(videoUri)
        player.addMediaItem(mediaItem)
        player.prepare()
        player.play()
        player.setAudioAttributes(AudioAttributes.DEFAULT, true)
        player.playWhenReady = true

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == ExoPlayer.STATE_READY) {
                    val duration = player.duration / 1000
                    binding.stories.setSingleDisplayTime(duration.toFloat())
                    binding.stories.start()
                }
            }
        })
    }


    private fun isImageFile(data: UserData): Boolean {
        return (data.type == "image")
    }

    override fun onProgressStepChange(newStep: Int) {
        counter = newStep
        onNext()
    }

    private fun stopPlayer() {
        if(this::player.isInitialized) {
            player.playWhenReady = false
            player.pause()
            player.stop()
            player.release()
        }
    }

    override fun onProgressFinished() {
        finish()
        stopPlayer()
    }

    override fun onStop() {
        super.onStop()
        stopPlayer()
    }
}