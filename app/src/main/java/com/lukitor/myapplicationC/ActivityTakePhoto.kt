package com.lukitor.myapplicationC

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.lukitor.myapplicationC.databinding.ActivityTakePhotoBinding
import com.lukitor.myapplicationC.retrofit.ApiConfig
import com.lukitor.myapplicationC.retrofit.ResponseApiModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ActivityTakePhoto : AppCompatActivity() {

    lateinit var binding:ActivityTakePhotoBinding
    lateinit var bitmap: Bitmap
    var tempUri :Uri? = null
    var mUri :Uri? = null
    var picturePath:String? = null
    var filegambarcamera:File? = null

    companion object{
        private const val MY_CAMERA_PERMISSION_CODE = 111
        private const val CAMERA_REQUEST = 1888
        private const val GALLERY_REQUEST = 1010
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tempUri=null
        supportActionBar!!.hide()

        binding.btnGallery.setOnClickListener{
            var intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type= "image/*"
            startActivityForResult(intent, GALLERY_REQUEST)
        }
        binding.btnCamera.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(Array(10){Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    val f = File("${getExternalFilesDir(null)}/imgShot")
                    val photoURI = FileProvider.getUriForFile(this, "${packageName}.fileprovider", f)
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply { putExtra(MediaStore.EXTRA_OUTPUT, photoURI) }
                    if(cameraIntent.resolveActivity(packageManager) != null){
                        var photoFile_: File? = null
                        try {
                            photoFile_ = createImageFile()
                        } catch (ex: IOException) {
                        }
                        if (photoFile_ != null) {
                            picturePath = photoFile_.absolutePath
                        }
                        // Continue only if the File was successfully created
                        // Continue only if the File was successfully created
                        if (photoFile_ != null) {
                            val photoURI_ = FileProvider.getUriForFile(
                                this,
                                "com.lukitor.myapplicationC.fileprovider", photoFile_
                            )
                            tempUri=photoURI
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI_)
                            startActivityForResult(cameraIntent, CAMERA_REQUEST)
                        }
                    }

                }
            }
        }
        binding.btnSendPhoto.setOnClickListener{
            sendPhoto(tempUri,filegambarcamera)
            filegambarcamera=null
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
                val f = File("${getExternalFilesDir(null)}/imgShot")
                val photoURI = FileProvider.getUriForFile(this, "${packageName}.fileprovider", f)
                tempUri=photoURI
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply { putExtra(MediaStore.EXTRA_OUTPUT, photoURI) }
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST) {
            binding.gambarHasil.setImageURI(data?.data)
            var uri: Uri? = data?.data
            if (uri != null) {
                bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val src: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, uri!!)
                    ImageDecoder.decodeBitmap(src)

                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                }
                tempUri = getImageUri(this@ActivityTakePhoto,bitmap)!!
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                val file_ = File(picturePath)
                val uri_ = FileProvider.getUriForFile(
                    this,
                    "com.lukitor.myapplicationC.fileprovider", file_
                )
                binding.gambarHasil.setImageURI(uri_)
                filegambarcamera=file_
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun sendPhoto(tempoUri : Uri?,fileCamera : File?){
        var file:File?
        if(fileCamera==null)file = File(getRealPathFromURI(tempoUri))
        else  file=fileCamera
        val reqBody: RequestBody = file.asRequestBody("image/*".toMediaType())
        val partImage: MultipartBody.Part =
            MultipartBody.Part.createFormData("imageupload", file.name, reqBody)
        val api = ApiConfig.provideApiService()
        val upload :Call<ResponseApiModel> = api.uploadImage(partImage)
        upload.enqueue(object : Callback<ResponseApiModel> {
            override fun onResponse(
                call: Call<ResponseApiModel>,
                response: Response<ResponseApiModel>
            ) {
                if (response.body()!!.kode=="1") {
                    Toast.makeText(
                        this@ActivityTakePhoto,
                        response.body()!!.pesan,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@ActivityTakePhoto,
                        response.body()!!.pesan,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseApiModel>, t: Throwable) {
                Log.d("RETRO", "ON FAILURE : " + t.message)
            }
        })
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }
    fun getRealPathFromURI(uri: Uri?): String {
        var path = ""
        if (this@ActivityTakePhoto.contentResolver != null) {
            val cursor: Cursor? =
                this@ActivityTakePhoto.contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                if(idx!=-1){
                    path = cursor.getString(idx)
                    cursor.close()
                }

            }
        }
        return path
    }


    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp_: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName_ = "JPEG_" + timeStamp_ + "_"
        val storageDir_ = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image_ = File.createTempFile(
            imageFileName_,  /* prefix */
            ".jpg",  /* suffix */
            storageDir_ /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        picturePath = image_.absolutePath
        return image_
    }
}