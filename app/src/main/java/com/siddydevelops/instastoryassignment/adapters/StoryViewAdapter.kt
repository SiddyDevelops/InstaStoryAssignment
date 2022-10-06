package com.siddydevelops.instastoryassignment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siddydevelops.instastoryassignment.MainActivity
import com.siddydevelops.instastoryassignment.R
import com.siddydevelops.instastoryassignment.databinding.StoryItemLayoutBinding
import com.siddydevelops.instastoryassignment.instaStory.InstaStoryViewer
import com.siddydevelops.instastoryassignment.user.User

class StoryViewAdapter(private val userStories: ArrayList<User>) : RecyclerView.Adapter<StoryViewAdapter.StoryViewHolder>() {

    private val userProfile = "https://raw.githubusercontent.com/SiddyDevelops/Blogaro/main/Assets/Profile-Images/me_photo.jpg"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {

        with(holder){
            with(userStories[position]) {
                binding.username.text = this.userName
                binding.frameLayout.setOnClickListener { view ->
                    val instaStoryViewer = InstaStoryViewer(
                        view.context,
                        MainActivity::class.java,
                        this.imageList,
                        this.userName,
                        userProfile
                    )
                    instaStoryViewer.showStory()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return userStories.size
    }

    inner class StoryViewHolder(val binding: StoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}