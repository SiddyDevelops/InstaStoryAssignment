package com.siddydevelops.instastoryassignment.instaStory

import android.content.Context
import android.content.Intent

class InstaStoryViewer(
    private val context: Context?,
    private val cls: Class<*>?,
    private val imageURls: Array<String>,
    private val usernames: String?,
    private val userProfile: String?,
    private val storyTimes: Array<String>,
    private val likeCounts: Array<String>,
    private val storyText: Array<String>
) {
    fun showStory() {
        val intent = Intent(context, StoryPlayerActivity::class.java)
        intent.putExtra("ClassName", cls)
        intent.putExtra("IMAGEURLS", imageURls)
        intent.putExtra("USERNAME", usernames)
        intent.putExtra("USERPROFILE", userProfile)
        intent.putExtra("STORYTIMES", storyTimes)
        intent.putExtra("LIEKCOUNT", likeCounts)
        intent.putExtra("STORYTEXT", storyText)
        context?.startActivity(intent)
    }
}