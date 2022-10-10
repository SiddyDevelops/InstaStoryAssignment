package com.siddydevelops.instastoryassignment.instaStory

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.siddydevelops.instastoryassignment.databinding.ActivityStoryPlayerBinding
import jp.shts.android.storiesprogressview.StoriesProgressView


class StoryPlayerActivity : AppCompatActivity(), StoriesProgressView.StoriesListener {

    private lateinit var binding: ActivityStoryPlayerBinding
    private var imageList: ArrayList<String> = arrayListOf()
    private var durationList: ArrayList<String> = arrayListOf()
    private var username: String? = null
    private var userProfile: String? = null

    private var pressTime = 0L
    private var limit = SEC_10

    private var counter = 0
    private val retriever = MediaMetadataRetriever()

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener =
        OnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    // on action down when we press our screen
                    // the story will pause for specific time.
                    pressTime = System.currentTimeMillis()

                    // on below line we are pausing our indicator.
                    binding.stories.pause()
                    return@OnTouchListener false
                }
                MotionEvent.ACTION_UP -> {

                    /*
                    in action up case when user do not touches
                    screen this method will skip to next image.
                    */
                    val now = System.currentTimeMillis()

                    // on below line we are resuming our progress bar for status.
                    binding.stories.resume()

                    // on below line we are returning if the limit < now - presstime
                    return@OnTouchListener limit < now - pressTime
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryPlayerBinding.inflate(layoutInflater)
        val root = binding.root
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(root)

        init()
        val durations = LongArray(durationList.size)
        for(i in 0 until durationList.size) {
            durations[i] = durationList[i].toLong()
        }
        binding.stories.setStoriesCountWithDurations(durations)
        binding.stories.setStoriesListener(this)
        binding.stories.startStories(counter)
        binding.stories.pause()

        if (isImageFile(imageList[counter])) {
            glideImage(
                imageList[counter],
                username!!
            )
        } else if (isVideoFile(imageList[counter])) {
            previewVideo(imageList[counter])
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

    private fun init() {
        imageList = intent.extras?.getStringArrayList("IMAGEURLS")!!
        durationList = intent.extras?.getStringArrayList("DURATIONLIST")!!
        username = intent.getStringExtra("USERNAME")
        userProfile = intent.getStringExtra("USERPROFILE")
        Glide.with(this).load(userProfile).into(binding.profileImage)
    }

    override fun onNext() {
        // this method is called when we move
        // to next progress view of story.

        // this method is called when we move
        // to next progress view of story.
        ++counter
        if (isImageFile(imageList[counter])) {
            limit = SEC_10
            glideImage(
                imageList[counter],
                username!!
            )
        } else if (isVideoFile(imageList[counter])) {
            retriever.setDataSource(this, Uri.parse(imageList[counter]))
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            limit = time!!.toLong()
            previewVideo(imageList[counter])
        }
    }

    override fun onPrev() {
        if (counter - 1 < 0) return
        --counter
        if(isImageFile(imageList[counter])) {
            limit = SEC_10
            glideImage(
                imageList[counter],
                username!!
            )
        } else if(isVideoFile(imageList[counter])) {
            retriever.setDataSource(this, Uri.parse(imageList[counter]))
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            limit = time!!.toLong()
            previewVideo(imageList[counter])
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
        image: String,
        username: String
    ) {
        binding.image.visibility = View.VISIBLE
        binding.video.visibility = View.GONE
        binding.image.setImageURI(Uri.parse(image))
        binding.usernameTV.text = username
    }

    private fun previewVideo(videoUri: String) {
        binding.image.visibility = View.GONE
        binding.video.visibility = View.VISIBLE
        binding.video.setVideoURI(Uri.parse(videoUri))
        binding.video.start()
    }

    private fun isImageFile(path: String?): Boolean {
        val cR: ContentResolver = contentResolver
        val type = cR.getType(Uri.parse(path))
        return type!!.startsWith("image")
    }

    private fun isVideoFile(path: String?): Boolean {
        val cR: ContentResolver = contentResolver
        val type = cR.getType(Uri.parse(path))
        return type!!.startsWith("video")
    }

    companion object {
        private const val SEC_10 = 10000L
        private const val SEC_30 = 30000L
    }
}