package com.siddydevelops.instastoryassignment.reels

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Video
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.siddydevelops.instastoryassignment.R
import com.siddydevelops.instastoryassignment.adapters.VideoAdapter
import com.siddydevelops.instastoryassignment.databinding.ActivityReelsBinding
import com.siddydevelops.instastoryassignment.databinding.VideoItemLayoutBinding

class ReelsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityReelsBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        val videoList: ArrayList<String> = arrayListOf()
//        videoList.add("https://sample-videos.com/video123/mp4/480/big_buck_bunny_480p_5mb.mp4")
//        videoList.add("http://techslides.com/demos/sample-videos/small.mp4")

        val videoList: ArrayList<String> = intent.getStringArrayListExtra("VideoList") as ArrayList<String>
        binding.recyclerView.adapter = VideoAdapter(videoList)
        PagerSnapHelper().attachToRecyclerView(binding.recyclerView)

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("Position: ","${getCurrentItem()}")
                    //onPageChanged(position)
                    setCurrentItem(getCurrentItem())
                }
            }
        })

    }

    private fun getCurrentItem(): Int {
        return (binding.recyclerView.layoutManager as LinearLayoutManager)
            .findFirstVisibleItemPosition()
    }

    private fun setCurrentItem(position: Int) {
        binding.recyclerView.layoutManager!!.scrollToPosition(position)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}