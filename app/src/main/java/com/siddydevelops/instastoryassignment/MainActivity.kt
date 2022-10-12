package com.siddydevelops.instastoryassignment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.siddydevelops.instastoryassignment.adapters.StoryViewAdapter
import com.siddydevelops.instastoryassignment.databinding.ActivityMainBinding
import com.siddydevelops.instastoryassignment.reels.ReelsActivity
import com.siddydevelops.instastoryassignment.user.User
import com.siddydevelops.instastoryassignment.user.UserData
import kotlin.collections.ArrayList


open class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private var imageList: ArrayList<UserData> = arrayListOf()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val root = activityMainBinding.root
        setContentView(root)

        imageList.add(UserData("https://picsum.photos/id/237/200/300","image"))  // Image
        imageList.add(UserData("https://picsum.photos/seed/picsum/200/300","image")) // Image
        imageList.add(UserData("https://sample-videos.com/video123/mp4/480/big_buck_bunny_480p_5mb.mp4","video")) // Video
        imageList.add(UserData("http://techslides.com/demos/sample-videos/small.mp4","video")) // Video
        imageList.add(UserData("https://picsum.photos/id/237/200/300","image")) // Image
        imageList.add(UserData("https://picsum.photos/seed/picsum/200/300","image")) // Image

        val data : ArrayList<User> = arrayListOf()
        data.add(User(imageList))

        activityMainBinding.storyViewRV.adapter = StoryViewAdapter(data)

        activityMainBinding.reelsBtn.setOnClickListener {
            val intent = Intent(this,ReelsActivity::class.java)
            startActivity(intent)
        }
    }
}