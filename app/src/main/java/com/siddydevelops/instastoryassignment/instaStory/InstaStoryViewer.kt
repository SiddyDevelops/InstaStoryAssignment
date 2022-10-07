package com.siddydevelops.instastoryassignment.instaStory

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import com.siddydevelops.instastoryassignment.user.User


class InstaStoryViewer(
    private val context: Context?,
    private val cls: Class<*>?,
    private val user: User,
    private val userProfile: String?
) {
    fun showStory() {
        val intent = Intent(context, StoryPlayerActivity::class.java)
        intent.putExtra("ClassName", cls)
        intent.putStringArrayListExtra("IMAGEURLS", user.imageList)
        intent.putExtra("USERNAME", user.userName)
        intent.putExtra("USERPROFILE", userProfile)
        context?.startActivity(intent)
    }
}