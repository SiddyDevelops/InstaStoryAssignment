package com.siddydevelops.instastoryassignment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.siddydevelops.instastoryassignment.MainActivity
import com.siddydevelops.instastoryassignment.R
import com.siddydevelops.instastoryassignment.instaStory.InstaStoryViewer

class StoryViewAdapter(private var usernameList: Array<String>,
                       private var profileImageList: Array<String>
) : RecyclerView.Adapter<StoryViewAdapter.StoryViewHolder>() {
    private val ImageURls0 = arrayOf(
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/11.png",
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/41.png",
        "https://images.pexels.com/photos/799443/pexels-photo-799443.jpeg"
    )
    private val username0 = "Siddharth Singh"
    private val userProfile0 =
        "https://raw.githubusercontent.com/SiddyDevelops/Blogaro/main/Assets/Profile-Images/me_photo.jpg"
    private val storyTimes0 = arrayOf("15hr Ago", "8hr Ago", "9hr Ago")
    private val likeCounts0 = arrayOf("22K", "257", "6.8K")
    private val storyText0 = arrayOf(
        "New Pokemon now live!",
        "Gather tonight for the latest event by AC/DC",
        "People around the world are crazy!"
    )

    private val ImageURls1 = arrayOf(
        "https://www.kolpaper.com/wp-content/uploads/2020/11/Aesthetic-Mobile-Wallpaper-2.jpg",
        "https://www.enjpg.com/img/2020/4k-mobile-7.jpg"
    )
    private val username1 = "Christina"
    private val userProfile1 =
        "https://raw.githubusercontent.com/SiddyDevelops/Blogaro/main/Assets/Profile-Images/christina.jpg"
    private val storyTimes1 = arrayOf("2hr Ago", "15hr Ago")
    private val likeCounts1 = arrayOf("59.6K", "2.7K")
    private val storyText1 =
        arrayOf("Crypto Reaching Heights!", "RED Moon appeared in Northern America.")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.story_item_layout, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.username.text = usernameList!![position]
        Glide.with(holder.itemView.context).load(profileImageList!![position])
            .into(holder.profileImageStory)
        holder.frameLayout.setOnClickListener { view ->
            if (holder.adapterPosition == 0) {
                val instaStoryViewer = InstaStoryViewer(
                    view.context,
                    MainActivity::class.java,
                    ImageURls0,
                    username0,
                    userProfile0,
                    storyTimes0,
                    likeCounts0,
                    storyText0
                )
                instaStoryViewer.showStory()
            }
            if (holder.adapterPosition == 1) {
                val instaStoryViewer = InstaStoryViewer(
                    view.context,
                    MainActivity::class.java,
                    ImageURls1,
                    username1,
                    userProfile1,
                    storyTimes1,
                    likeCounts1,
                    storyText1
                )
                instaStoryViewer.showStory()
            }
        }
    }

    override fun getItemCount(): Int {
        return usernameList!!.size
    }

    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username: TextView
        var frameLayout: FrameLayout
        var profileImageStory: ImageView

        init {
            username = itemView.findViewById(R.id.username)
            frameLayout = itemView.findViewById(R.id.frameLayout)
            profileImageStory = itemView.findViewById(R.id.profileImageStory)
        }
    }
}