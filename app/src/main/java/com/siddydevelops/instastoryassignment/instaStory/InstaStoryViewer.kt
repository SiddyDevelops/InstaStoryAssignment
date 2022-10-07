package com.siddydevelops.instastoryassignment.instaStory

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log


class InstaStoryViewer(
    private val context: Context?,
    private val cls: Class<*>?,
    private val imageList: ArrayList<String>,
    private val usernames: String?,
    private val userProfile: String?
) {
    fun showStory() {
        val intent = Intent(context, StoryPlayerActivity::class.java)
        intent.putExtra("ClassName", cls)

        intent.putStringArrayListExtra("IMAGEURLS", imageList)
        Log.d("ImageList->",imageList.toString())
        intent.putExtra("USERNAME", usernames)
        intent.putExtra("USERPROFILE", userProfile)
        context?.startActivity(intent)
    }
}