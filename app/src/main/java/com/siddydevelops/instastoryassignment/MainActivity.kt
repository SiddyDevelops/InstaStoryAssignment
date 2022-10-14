package com.siddydevelops.instastoryassignment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.siddydevelops.instastoryassignment.adapters.StoryViewAdapter
import com.siddydevelops.instastoryassignment.database.entities.ReelsItem
import com.siddydevelops.instastoryassignment.databinding.ActivityMainBinding
import com.siddydevelops.instastoryassignment.reels.ReelsActivity
import com.siddydevelops.instastoryassignment.reels.ReelsViewModel
import com.siddydevelops.instastoryassignment.user.User
import com.siddydevelops.instastoryassignment.user.UserData
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


open class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private var imageList: ArrayList<UserData> = arrayListOf()
    //private var videoList: ArrayList<String> = arrayListOf()
    private lateinit var uri: Uri
    private var currentPhotoPath: String = ""
    private var imgPath: String? = null
    private var count = 0

    private lateinit var viewModel: ReelsViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val root = activityMainBinding.root
        setContentView(root)

        if (!checkCameraPermission()) {
            getCameraPermission()
        }

        imageList.add(UserData("https://picsum.photos/id/237/200/300","image"))  // Image
        imageList.add(UserData("https://picsum.photos/seed/picsum/200/300","image")) // Image
        imageList.add(UserData("https://sample-videos.com/video123/mp4/480/big_buck_bunny_480p_5mb.mp4","video")) // Video
        imageList.add(UserData("http://techslides.com/demos/sample-videos/small.mp4","video")) // Video
        imageList.add(UserData("https://picsum.photos/id/237/200/300","image")) // Image
        imageList.add(UserData("https://picsum.photos/seed/picsum/200/300","image")) // Image

        val data : ArrayList<User> = arrayListOf()
        data.add(User(imageList))

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ReelsViewModel::class.java]

        activityMainBinding.storyViewRV.adapter = StoryViewAdapter(data)

        activityMainBinding.reelsBtn.setOnClickListener {
            val intent = Intent(this, ReelsActivity::class.java)
            //intent.putStringArrayListExtra("VideoList",videoList)
            startActivity(intent)
        }

        activityMainBinding.chooseVideoFromGallery.setOnClickListener {
            selectVideoInAlbum()
        }

        activityMainBinding.selectCamera.setOnClickListener {
            recordVideo()
        }
    }

    private fun selectImageInAlbum() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        launchGallery.launch(i)
    }

    private fun selectVideoInAlbum() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            Intent.ACTION_GET_CONTENT
            Intent.ACTION_OPEN_DOCUMENT
            Intent.ACTION_OPEN_DOCUMENT_TREE
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.action= Intent.ACTION_OPEN_DOCUMENT
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST)
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager).also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }

                photoFile?.also {
                    uri = FileProvider.getUriForFile(
                        this,
                        "com.siddydevelops.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    private fun recordVideo() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takePictureIntent ->    //ACTION_IMAGE_CAPTURE
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager).also {
                takePictureIntent.putExtra("android.intent.extra.durationLimit", 30)
                takePictureIntent.putExtra("EXTRA_VIDEO_QUALITY", 0)
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createVideoFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }

                photoFile?.also {
                    uri = FileProvider.getUriForFile(
                        this,
                        "com.siddydevelops.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    @Throws(IOException::class)
    private fun createVideoFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "MP4_${timeStamp}_", /* prefix */
            ".mp4", /* suffix */  //.jpg
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = uri
                    imgPath = resultUri.path
                    count++
                    //videoList.add(uri.toString())
                    viewModel.insert(ReelsItem(uri.toString(),false))
                }
            }

            PICK_VIDEO_REQUEST -> {
                val selectedImageUri: Uri = data?.data!!
                selectedImageUri.path
                val mediaMetadataRetriever = MediaMetadataRetriever()
                mediaMetadataRetriever.setDataSource(this, selectedImageUri)
                val videoTime =
                    mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                count++
                viewModel.insert(ReelsItem(selectedImageUri.toString(),false))
                //videoList.add(selectedImageUri.toString())
                activityMainBinding.imageCounter.text = "VideoCount: ${count}"
            }
        }
    }

    private var launchGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode
            == RESULT_OK
        ) {
            val data = result.data
            if (data != null
                && data.data != null
            ) {
                val selectedImageUri: Uri? = data.data
                var selectedImageBitmap: Bitmap = Bitmap.createBitmap(
                    AppCompatResources.getDrawable(this, R.drawable.ic_image)!!.toBitmap()
                )
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        selectedImageUri
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                viewModel.insert(ReelsItem(getImageUri(this, selectedImageBitmap).toString(),false))
                //videoList.add(getImageUri(this, selectedImageBitmap).toString())
                count++
                activityMainBinding.imageCounter.text = "VideoCount: ${count}"
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

    private fun getCameraPermission() {
        if (checkExternalStoragePermission() && checkCameraPermission()) {
            Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                CAMERA_GALLERY_REQUEST_CODE
            )
        }
    }

    private fun checkCameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            )
            == PackageManager.PERMISSION_GRANTED
        )
            return true
        return false
    }

    private fun checkExternalStoragePermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        )
            return true
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_GALLERY_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show()
                } else
                    Toast.makeText(this, "Permission refused!", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val REQUEST_TAKE_PHOTO = 0
        private const val CAMERA_GALLERY_REQUEST_CODE = 100
        private const val PICK_VIDEO_REQUEST = 110
        private const val REQUEST_ACTION_OPEN = 120
    }
}