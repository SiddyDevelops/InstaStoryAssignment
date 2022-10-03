package com.siddydevelops.instastoryassignment.instaStory

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.siddydevelops.instastoryassignment.databinding.ActivityStoryPlayerBinding
import jp.shts.android.storiesprogressview.StoriesProgressView

class StoryPlayerActivity : AppCompatActivity(), StoriesProgressView.StoriesListener {

    private lateinit var binding: ActivityStoryPlayerBinding

    private var ImageURls: Array<String> = arrayOf()
    private var username: String? = null
    private var userProfile: String? = null
    private var storyTimes: Array<String> = arrayOf()
    private var likeCounts: Array<String> = arrayOf()
    private var storyText: Array<String> = arrayOf()

    var pressTime = 0L
    var limit = 500L

    var cls: Class<*>? = null

    private var counter = 0

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
        // inside in create method below line is use to make a full screen.

        //Initializing Variables through intent
        // inside in create method below line is use to make a full screen.

        //Initializing Variables through intent
        Initializing()

        // on below line we are initializing our variables.

        // on below line we are initializing our variables.

        // on below line we are setting the total count for our stories.

        // on below line we are setting the total count for our stories.
        binding.stories.setStoriesCount(ImageURls.size)

        // on below line we are setting story duration for each story.

        // on below line we are setting story duration for each story.
        binding.stories.setStoryDuration(3000L)

        // on below line we are calling a method for set
        // on story listener and passing context to it.

        // on below line we are calling a method for set
        // on story listener and passing context to it.
        binding.stories.setStoriesListener(this)
        // below line is use to start stories progress bar.

        // below line is use to start stories progress bar.
        binding.stories.startStories(counter)
        binding.stories.pause()
        glideImage(
            ImageURls[counter],
            username!!, storyTimes[counter], likeCounts[counter], storyText[counter]
        )

        // below is the view for going to the previous story.
        // initializing our previous view.

        // below is the view for going to the previous story.
        // initializing our previous view.

        // adding on click listener for our reverse view.

        // adding on click listener for our reverse view.
        binding.reverse.setOnClickListener { // inside on click we are
            // reversing our progress view.
            binding.stories.reverse()
        }

        // on below line we are calling a set on touch
        // listener method to move towards previous image.

        // on below line we are calling a set on touch
        // listener method to move towards previous image.
        binding.reverse.setOnTouchListener(onTouchListener)

        // on below line we are initializing
        // view to skip a specific story.

        // on below line we are initializing
        // view to skip a specific story.
        binding.skip.setOnClickListener { // inside on click we are
            // skipping the story progress view.
            binding.stories.skip()
        }
        // on below line we are calling a set on touch
        // listener method to move to next story.
        // on below line we are calling a set on touch
        // listener method to move to next story.
        binding.skip.setOnTouchListener(onTouchListener)
    }

    fun Initializing() {
        cls = intent.getSerializableExtra("ClassName") as Class<*>?
        ImageURls = intent.getStringArrayExtra("IMAGEURLS")!!
        username = intent.getStringExtra("USERNAME")
        userProfile = intent.getStringExtra("USERPROFILE")
        storyTimes = intent.getStringArrayExtra("STORYTIMES")!!
        likeCounts = intent.getStringArrayExtra("LIEKCOUNT")!!
        storyText = intent.getStringArrayExtra("STORYTEXT")!!
    }

    override fun onNext() {
        // this method is called when we move
        // to next progress view of story.

        // this method is called when we move
        // to next progress view of story.
        glideImage(
            ImageURls[++counter],
            username!!, storyTimes[counter], likeCounts[counter], storyText[counter]
        )
    }

    override fun onPrev() {

        // this method id called when we move to previous story.
        // on below line we are decreasing our counter
        if (counter - 1 < 0) return
        glideImage(
            ImageURls[--counter],
            username!!, storyTimes[counter], likeCounts[counter], storyText[counter]
        )

        // on below line we are setting image to image view
    }

    override fun onComplete() {
        // when the stories are completed this method is called.
        // in this method we are moving back to initial main activity.
        val i = Intent(this@StoryPlayerActivity, cls)
        startActivity(i)
        finish()
        Log.d("Complete","Complete")
    }

    override fun onDestroy() {
        // in on destroy method we are destroying
        // our stories progress view.
        binding.stories.destroy()
        super.onDestroy()
    }

    private fun glideImage(
        URL: String,
        username: String,
        time: String,
        like: String,
        storyText: String
    ) {
        Glide.with(this)
            .load(URL)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(
                        this@StoryPlayerActivity,
                        "Failed to load image.",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.stories.pause()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.stories.resume()
                    return false
                }
            })
            .into(binding.image)
        binding.usernameTV.text = username
        binding.storyTimeTV.text = time
        binding.likeCountTV.text = like
        binding.storyTTV.text = storyText
    }
}