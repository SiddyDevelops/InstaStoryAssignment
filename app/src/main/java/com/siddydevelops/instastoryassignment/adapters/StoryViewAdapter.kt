package com.siddydevelops.instastoryassignment.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.siddydevelops.instastoryassignment.databinding.StoryItemLayoutBinding
import com.siddydevelops.instastoryassignment.activities.StoryPlayerActivity
import com.siddydevelops.instastoryassignment.models.User

class StoryViewAdapter(private val userStories: ArrayList<User>) : RecyclerView.Adapter<StoryViewAdapter.StoryViewHolder>() {

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
            binding.frameLayout.setOnClickListener { view ->
                val intent = Intent(view.context, StoryPlayerActivity::class.java)
                intent.putParcelableArrayListExtra("IMAGEURLS", user.data)
                view.context.startActivity(intent)
            }
        }
    }
}