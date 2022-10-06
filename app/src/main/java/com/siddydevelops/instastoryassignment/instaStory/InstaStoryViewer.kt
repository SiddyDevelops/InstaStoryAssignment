package com.siddydevelops.instastoryassignment.instaStory

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle


class InstaStoryViewer(
    private val context: Context?,
    private val cls: Class<*>?,
    private val imageList: ArrayList<Bitmap>,
    private val usernames: String?,
    private val userProfile: String?
) {
    fun showStory() {
        val intent = Intent(context, StoryPlayerActivity::class.java)
        intent.putExtra("ClassName", cls)

        intent.putExtra("IMAGEURLS", imageList)

        intent.putExtra("USERNAME", usernames)
        intent.putExtra("USERPROFILE", userProfile)
        context?.startActivity(intent)
    }
}