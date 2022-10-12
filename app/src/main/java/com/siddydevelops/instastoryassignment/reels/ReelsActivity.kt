package com.siddydevelops.instastoryassignment.reels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.ExoPlayer
import com.siddydevelops.instastoryassignment.R
import com.siddydevelops.instastoryassignment.adapters.VideoAdapter
import com.siddydevelops.instastoryassignment.databinding.ActivityReelsBinding
import com.siddydevelops.instastoryassignment.user.UserData

class ReelsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityReelsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        val videoList: ArrayList<String> = arrayListOf()
//        videoList.add("https://sample-videos.com/video123/mp4/480/big_buck_bunny_480p_5mb.mp4")
//        videoList.add("http://techslides.com/demos/sample-videos/small.mp4")

        val videoList: ArrayList<String> = intent.getStringArrayListExtra("VideoList") as ArrayList<String>

        binding.viewPager.adapter = VideoAdapter(videoList)
    }
}