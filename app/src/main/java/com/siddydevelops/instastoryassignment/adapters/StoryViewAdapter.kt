package com.siddydevelops.instastoryassignment.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.siddydevelops.instastoryassignment.MainActivity
import com.siddydevelops.instastoryassignment.databinding.StoryItemLayoutBinding
import com.siddydevelops.instastoryassignment.instaStory.StoryPlayerActivity
import com.siddydevelops.instastoryassignment.user.User

class StoryViewAdapter(private val userStories: ArrayList<User>) : RecyclerView.Adapter<StoryViewAdapter.StoryViewHolder>() {

    private val userProfile = "https://raw.githubusercontent.com/SiddyDevelops/Blogaro/main/Assets/Profile-Images/me_photo.jpg"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bindTo(userStories[position])
    }

    override fun getItemCount(): Int {
        return userStories.size
    }

    inner class StoryViewHolder(private val binding: StoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(user: User) {
            binding.username.text = user.userName
            binding.frameLayout.setOnClickListener { view ->
                val intent = Intent(view.context, StoryPlayerActivity::class.java)
                intent.putStringArrayListExtra("IMAGEURLS", user.imageList)
                intent.putStringArrayListExtra("DURATIONLIST", user.durationList)
                intent.putExtra("USERNAME", user.userName)
                intent.putExtra("USERPROFILE", userProfile)
                view.context.startActivity(intent)
            }
        }
    }
}