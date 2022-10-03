package com.siddydevelops.instastoryassignment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.siddydevelops.instastoryassignment.adapters.StoryViewAdapter
import com.siddydevelops.instastoryassignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    private var usernameList =
        arrayOf("Siddharth", "Christina", "Luis Villasmil", "Michael Daze", "Usman Yousaf")

    private var profileImageList = arrayOf(
        "https://raw.githubusercontent.com/SiddyDevelops/Blogaro/main/Assets/Profile-Images/me_photo.jpg",
        "https://raw.githubusercontent.com/SiddyDevelops/Blogaro/main/Assets/Profile-Images/christina.jpg",
        "https://raw.githubusercontent.com/SiddyDevelops/Blogaro/main/Assets/Profile-Images/luis_villasmil.jpg",
        "https://raw.githubusercontent.com/SiddyDevelops/Blogaro/main/Assets/Profile-Images/michael_daze.jpg",
        "https://raw.githubusercontent.com/SiddyDevelops/Blogaro/main/Assets/Profile-Images/usman_yousaf.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val root = activityMainBinding.root
        setContentView(root)

        activityMainBinding.storyViewRV.layoutManager =  LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        activityMainBinding.storyViewRV.adapter = StoryViewAdapter(usernameList, profileImageList)
    }
}