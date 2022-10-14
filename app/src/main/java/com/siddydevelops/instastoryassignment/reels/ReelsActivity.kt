package com.siddydevelops.instastoryassignment.reels

import android.app.Activity
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.siddydevelops.instastoryassignment.MainActivity
import com.siddydevelops.instastoryassignment.adapters.VideoAdapter
import com.siddydevelops.instastoryassignment.database.entities.ReelsItem
import com.siddydevelops.instastoryassignment.databinding.ActivityReelsBinding

class ReelsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityReelsBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: ReelsViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        val videoList: ArrayList<String> = arrayListOf()
//        videoList.add("https://sample-videos.com/video123/mp4/480/big_buck_bunny_480p_5mb.mp4")
//        videoList.add("http://techslides.com/demos/sample-videos/small.mp4")

        //val videoList: ArrayList<String> = intent.getStringArrayListExtra("VideoList") as ArrayList<String>

        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application))[ReelsViewModel::class.java]

        val adapter = VideoAdapter(viewModel)
        binding.recyclerView.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(binding.recyclerView)

        viewModel.allReels.observe(this) { list ->
            list?.let {
                adapter.updateList(it)
            }
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("Position: ","${getCurrentItem()}")
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