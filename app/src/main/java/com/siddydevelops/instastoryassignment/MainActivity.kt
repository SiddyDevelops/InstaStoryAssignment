package com.siddydevelops.instastoryassignment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.siddydevelops.instastoryassignment.adapters.StoryViewAdapter
import com.siddydevelops.instastoryassignment.databinding.ActivityMainBinding
import com.siddydevelops.instastoryassignment.user.User
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.collections.ArrayList


open class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private var imageList: ArrayList<String> = arrayListOf()
    private var userStories: ArrayList<User> = arrayListOf()
    private var count = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val root = activityMainBinding.root
        setContentView(root)

        activityMainBinding.storyViewRV.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        activityMainBinding.selectFromGallery.setOnClickListener {
            selectImageInAlbum()
        }

        activityMainBinding.selectCamera.setOnClickListener {
            takePhoto()
        }

        activityMainBinding.addStoryBtn.setOnClickListener {
            if(activityMainBinding.userNameET.text.isEmpty()) {
                Toast.makeText(this,"Please enter the username!",Toast.LENGTH_LONG).show()
            } else {
                userStories.add(User(activityMainBinding.userNameET.text.toString(),imageList))
            }
            activityMainBinding.storyViewRV.adapter = StoryViewAdapter(userStories)
        }
    }

    private fun selectImageInAlbum() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        launchGallery.launch(i)
    }

    private fun takePhoto() {
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (i.resolveActivity(packageManager) != null) {
            startActivityForResult(i, REQUEST_TAKE_PHOTO)
        }
    }

    private var launchGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode
            == RESULT_OK
        ) {
            val data = result.data
            // do your operation from here....
            if (data != null
                && data.data != null
            ) {
                val selectedImageUri: Uri? = data.data
                var selectedImageBitmap: Bitmap = Bitmap.createBitmap(AppCompatResources.getDrawable(this,R.drawable.ic_image)!!.toBitmap())
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        selectedImageUri
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                imageList.add(getImageUri(this,selectedImageBitmap).toString())
                count++
                activityMainBinding.imageCounter.text = "ImageCount: ${count}"
            }
        }
    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }
}