package com.siddydevelops.instastoryassignment.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.siddydevelops.instastoryassignment.adapters.VideoAdapter
import com.siddydevelops.instastoryassignment.database.entities.ReelsItem
import com.siddydevelops.instastoryassignment.databinding.ActivityReelsBinding
import com.siddydevelops.instastoryassignment.models.ExoPlayerItem
import com.siddydevelops.instastoryassignment.viewModels.ReelsViewModel

class ReelsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityReelsBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: ReelsViewModel
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    private var updateReelsItems = ArrayList<ReelsItem>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        val videoList: ArrayList<String> = arrayListOf()
//        videoList.add("https://sample-videos.com/video123/mp4/480/big_buck_bunny_480p_5mb.mp4")
//        videoList.add("http://techslides.com/demos/sample-videos/small.mp4")

        //val videoList: ArrayList<String> = intent.getStringArrayListExtra("VideoList") as ArrayList<String>

        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application))[ReelsViewModel::class.java]

        val videoPreparedListener = object : VideoAdapter.OnVideoPreparedListener{
            override fun onVideoPreparedListener(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)
            }
        }

        val reelStatusListener = object : VideoAdapter.ChangeLikeReelStatus{
            override fun onToggleReelLike(reelItem: ReelsItem) {
                updateReelsItems.add(reelItem)
            }
        }

        val adapter = VideoAdapter(this,videoPreparedListener,reelStatusListener)
        binding.viewPager.adapter = adapter
        //PagerSnapHelper().attachToRecyclerView(binding.recyclerView)

        viewModel.allReels.observe(this) { list ->
            list?.let {
                adapter.updateList(it)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }
                if(previousIndex != -1) {
                    val player = exoPlayerItems[previousIndex].exoPlayer
                    player.pause()
                    player.playWhenReady = false
                }
                val newIndex = exoPlayerItems.indexOfFirst { it.position == position }
                if(newIndex != -1) {
                    val player = exoPlayerItems[newIndex].exoPlayer
                    player.playWhenReady = true
                    player.play()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewPager.currentItem }
        if(index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()
        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewPager.currentItem }
        if(index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(exoPlayerItems.isNotEmpty()) {
            for(item in exoPlayerItems) {
                val player = item.exoPlayer
                player.stop()
                player.clearMediaItems()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        for(item in updateReelsItems) {
            viewModel.updateLike(item)
        }
    }
}