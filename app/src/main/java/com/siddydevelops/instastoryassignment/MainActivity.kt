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
import androidx.recyclerview.widget.LinearLayoutManager
import com.siddydevelops.instastoryassignment.adapters.StoryViewAdapter
import com.siddydevelops.instastoryassignment.databinding.ActivityMainBinding
import com.siddydevelops.instastoryassignment.user.User
import com.siddydevelops.instastoryassignment.utils.URIPathHelper
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


open class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private var imageList: ArrayList<String> = arrayListOf()
    private var durationList: ArrayList<String> = arrayListOf()
    private var userStories: ArrayList<User> = arrayListOf()
    private var count = 0

    private lateinit var uri: Uri
    private var currentPhotoPath: String = ""
    private var imgPath: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val root = activityMainBinding.root
        setContentView(root)
        if (!checkCameraPermission()) {
            getCameraPermission()
        }

        activityMainBinding.storyViewRV.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        activityMainBinding.selectFromGallery.setOnClickListener {
            selectImageInAlbum()
        }

        activityMainBinding.chooseVideoFromGallery.setOnClickListener {
            selectVideoInAlbum()
        }

        activityMainBinding.selectCamera.setOnClickListener {
            //takePhoto()
            recordVideo()
        }

        activityMainBinding.addStoryBtn.setOnClickListener {
            if (activityMainBinding.userNameET.text.isEmpty() && imageList.isEmpty()) {
                Toast.makeText(this, "Please enter the username and image!", Toast.LENGTH_LONG)
                    .show()
            } else {
                userStories.add(User(activityMainBinding.userNameET.text.toString(), imageList,durationList))
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

    private fun selectVideoInAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
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
                    imageList.add(uri.toString())
                    durationList.add(SEC_10.toString())
                }
            }

            PICK_VIDEO_REQUEST -> {
                val selectedImageUri: Uri = data?.data!!
                selectedImageUri.path
                val mediaMetadataRetriever = MediaMetadataRetriever()
                mediaMetadataRetriever.setDataSource(this, selectedImageUri)
                val videoTime =
                    mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                if (videoTime!!.toLong() > 30000) {
//                    Toast.makeText(
//                        this,
//                        "Please select a video of length less than 30sec.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    count++
                    imageList.add(selectedImageUri.toString())
                    durationList.add(SEC_30.toString())
                    activityMainBinding.imageCounter.text = "ImageCount: ${count}"
                } else {
                    count++
                    imageList.add(selectedImageUri.toString())
                    durationList.add(videoTime.toLong().toString())
                    activityMainBinding.imageCounter.text = "ImageCount: ${count}"
                }
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
                imageList.add(getImageUri(this, selectedImageBitmap).toString())
                durationList.add(SEC_10.toString())
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

    private fun splitVideo(uri: Uri) {
        try {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(this, uri)
            val videoTime =
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

            val file = File(URIPathHelper().getPath(this,uri)!!)//File read from Source folder to Split.
            if (file.exists()) {
                val videoFileName = file.name.substring(
                    0,
                    file.name.lastIndexOf(".")
                ) // Name of the videoFile without extension
                val splitFile = File(Environment.getExternalStorageDirectory().toString() + "/Download/instaassignment/" + videoFileName)
                if (!splitFile.exists()) {
                    splitFile.mkdirs()
                    Log.d("Directory Created -> " , splitFile.absolutePath)
                }
                var i = 1L
                val inputStream: InputStream = FileInputStream(file)
                var videoFile = splitFile.absolutePath + "/" + java.lang.String.format(
                    "%02d",
                    i
                ) + "_" + file.name // Location to save the files which are Split from the original file.

                var outputStream: OutputStream = FileOutputStream(videoFile)
                Log.d("File Created Location:",videoFile)
                val totalPartsToSplit = videoTime!!.toLong()/30000 // Total files to split.
                Log.d("Parts->",totalPartsToSplit.toString())
                val splitSize = inputStream.available() / totalPartsToSplit
                var streamSize = 0L
                var read = 0
                read = inputStream.read()
                while ((read) != -1) {
                    if (splitSize == streamSize) {
                        if (i != totalPartsToSplit) {
                            i++
                            val fileCount = String.format("%02d", i) // output will be 1 is 01, 2 is 02
                            videoFile = splitFile.absolutePath +"/"+ fileCount +"_"+ file.name
                            outputStream = FileOutputStream(videoFile)
                            Log.d("File Created Location:",videoFile)
                            streamSize = 0
                        }
                    }
                    outputStream.write(read)
                    streamSize++
                    read = inputStream.read()
                }

                inputStream.close()
                outputStream.close()
                Log.d("Total files Split ->", totalPartsToSplit.toString());
            } else {
                Log.d("File Not Found.",file.absolutePath)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val REQUEST_TAKE_PHOTO = 0
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
        private const val CAMERA_GALLERY_REQUEST_CODE = 100
        private const val PICK_VIDEO_REQUEST = 110
        private const val SEC_10 = 10000L
        private const val SEC_30 = 30000L
    }
}