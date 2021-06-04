package com.lukitor.myapplicationC

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.lukitor.myapplicationC.data.room.entity.Nutrients
import com.lukitor.myapplicationC.databinding.ActivityTakePhotoBinding
import com.lukitor.myapplicationC.retrofit.ApiConfig
import com.lukitor.myapplicationC.retrofit.ResponseApiModel
import com.lukitor.myapplicationC.viewmodel.UserViewModel
import com.lukitor.myapplicationC.viewmodel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ActivityTakePhoto : AppCompatActivity() {
    var maxGaram: Int = 5; var maxGula: Int = 50; var maxLemak: Int = 67; var maxKalori: Int = 0;
    var dailyGaram: Int = 0; var dailyGula: Int = 0; var dailyLemak: Int = 0; var dailyKalori: Int = 0;
    var tempGaram: Int = 0; var tempGula: Int = 0; var tempLemak: Int = 0; var tempKalori: Int = 0;
    private lateinit var viewModel: UserViewModel
    lateinit var binding:ActivityTakePhotoBinding
    lateinit var bitmap: Bitmap
    var tempUri :Uri? = null
    var mUri :Uri? = null
    var picturePath:String? = null
    var filegambarcamera:File? = null

    private val mInputSize = 299
    private val mModelPath = "model.tflite"
    private val mLabelPath = "label.txt"
    private lateinit var classifier: Classifier

    companion object{
        private const val MY_CAMERA_PERMISSION_CODE = 111
        private const val CAMERA_REQUEST = 1888
        private const val GALLERY_REQUEST = 1010
    }

    private fun initClassifier() {
        classifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClassifier()
        binding.layoutHasil.visibility = View.GONE
        tempUri=null
        val anim:Animation = AnimationUtils.loadAnimation(this,R.anim.fromsmall)
        val animout:Animation = AnimationUtils.loadAnimation(this,R.anim.tosmall)
        if (intent.extras != null){
            maxKalori = intent.extras!!.getInt("maxKalori")
            dailyGaram = intent.extras!!.getInt("dailyGaram")
            dailyLemak = intent.extras!!.getInt("dailyLemak")
            dailyGula = intent.extras!!.getInt("dailyGula")
            dailyKalori = intent.extras!!.getInt("dailyKalori")
        }
        binding.btnBack.setOnClickListener{view -> finish()
            overridePendingTransition(R.transition.nothing,R.transition.bottom_down)}
//        val filename = "label.txt"
//        val labels = application.assets.open(filename).bufferedReader().use { it.readText() }.split("\n")

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
        binding.btnPredict.setOnClickListener{
            var bitmapss = drawableToBitmap(binding.gambarHasil.drawable)
            val result=classifier.recognizeImage(bitmapss!!)
            if(result.isEmpty()){
                Log.d("Classifier","Unknown Food")
                binding.txtApakahMakanan.visibility=View.VISIBLE
                if(binding.layoutHasil.visibility==View.VISIBLE){
                    binding.layoutHasil.startAnimation(animout)
                    binding.layoutHasil.visibility=View.GONE
                }
            }
            else{
                Log.d("Classifier",result[0].title)
                binding.txtApakahMakanan.visibility=View.GONE
                ChangeData()
                binding.layoutHasil.visibility = View.VISIBLE
                binding.layoutHasil.startAnimation(anim)
                binding.txtHasilFoodName.text=result[0].title
            }





////            sendPhoto(tempUri,filegambarcamera)
////            filegambarcamera=null
//            var bitmap = drawableToBitmap(binding.gambarHasil.drawable)
////            var resized=Bitmap.createScaledBitmap(bitmap!!,299,299,true)
//            var resized=Bitmap.createScaledBitmap(bitmap!!,299,299,false)
//
//            var classifier=Classifier(assets,"model.tflie","label.txt",299)
//            val result=classifier.recognizeImage(bitmap)
//
//
//            val model = Model.newInstance(this)
//
////            Log.d("Model",resized.width.toString())
//
//            // Creates inputs for reference.
//
//            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 299, 299, 3), DataType.FLOAT32)
//            var tensorImage= TensorImage(DataType.FLOAT32)
//            tensorImage.load(resized)
//            var byteBuffer1 = tensorImage.buffer
//
//
//
//            inputFeature0.loadBuffer(byteBuffer1)
//
//            // Runs model inference and gets result.
//            val outputs = model.process(inputFeature0)
//            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//
//            var max=getmax(outputFeature0.floatArray)
//
//            binding.txtHasilPredict.text=labels[max]
//
//            // Releases model resources if no longer used.
//            model.close()
//            ChangeData()
//            binding.layoutHasil.visibility = View.VISIBLE
//            binding.layoutHasil.startAnimation(anim)
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
            if(data!=null){
                binding.gambarHasil.setImageURI(data?.data)
            }

//            var uri: Uri? = data?.data
//            if (uri != null) {
//                bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                    val src: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, uri!!)
//                    ImageDecoder.decodeBitmap(src)
//
//                } else {
//                    @Suppress("DEPRECATION")
//                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
//                }
//                tempUri = getImageUri(this@ActivityTakePhoto,bitmap)!!
//            }
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

    fun getmax(arr:FloatArray):Int {
        var ind =0
        var min = 0.0f
        for (i in 0..19)
        {
            println(i.toString() +". " + arr[i])
            if(arr[i]>min){
                ind=i
                min=arr[i]
            }
        }
        return ind

    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1
        val bitmap = Bitmap.createBitmap(
            width, height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }

    // ChangeData function buat ganti data Nutrisi harian
    fun ChangeData(){
        var countKalori :Int  = dailyKalori+tempKalori
        binding.txtDashboardKalori.text = countKalori.toString() +  "/" + maxKalori.toString()
        var countGula :Int  = dailyGula+tempGula
        binding.txtDashboardGula.text = countGula.toString() + "/" + maxGula.toString()
        var countGaram :Int  = dailyGaram+tempGaram
        binding.txtDashboardGaram.text = countGaram.toString() + "/" + maxGaram.toString()
        var countLemak :Int  = dailyLemak+tempLemak
        binding.txtDashboardLemak.text = countLemak.toString() + "/" + maxLemak.toString()
        if(countKalori>maxKalori){
            Glide.with(this).load(R.drawable.arrowup).into(binding.imgDashboardKalori)
            val Temp: Double = ((countKalori.toDouble()-maxKalori.toDouble())/maxKalori.toDouble()) * 100
            binding.txtPersenKalori.text = Temp.toInt().toString() +"% Lebih Tinggi"
        }
        else{
            Glide.with(this).load(R.drawable.arrowdown).into(binding.imgDashboardKalori)
            val Temp: Double = ((maxKalori.toDouble()-countKalori.toDouble())/maxKalori.toDouble()) * 100
            binding.txtPersenKalori.text = Temp.toInt().toString() +"% Lebih Rendah"
        }
        if(countGula>maxGula){
            Glide.with(this).load(R.drawable.arrowup).into(binding.imgDashboardGula)
            val Temp: Double = ((countGula.toDouble()-maxGula.toDouble())/maxGula.toDouble()) * 100
            binding.txtPersenGula.text = Temp.toInt().toString() +"% Lebih Tinggi"
        }
        else{
            Glide.with(this).load(R.drawable.arrowdown).into(binding.imgDashboardGula)
            val Temp: Double = ((maxGula.toDouble()-countGula.toDouble())/maxGula.toDouble())*100
            binding.txtPersenGula.text = Temp.toInt().toString() +"% Lebih Rendah"
        }
        if(countGaram>maxGaram){
            Glide.with(this).load(R.drawable.arrowup).into(binding.imgDashboardGaram)
            val Temp: Double = ((countGaram.toDouble()-maxGaram.toDouble())/maxGaram.toDouble())*100
            binding.txtPersenGaram.text = Temp.toInt().toString() +"% Lebih Tinggi"
        }
        else{
            Glide.with(this).load(R.drawable.arrowdown).into(binding.imgDashboardGaram)
            val Temp: Double = ((maxGaram.toDouble()-countGaram.toDouble())/maxGaram.toDouble())*100
            binding.txtPersenGaram.text = Temp.toInt().toString() +"% Lebih Rendah"
        }
        if(countLemak>maxLemak){
            Glide.with(this).load(R.drawable.arrowup).into(binding.imgDashboardLemak)
            val Temp: Double = ((countLemak.toDouble()-maxLemak.toDouble())/maxLemak.toDouble())*100
            binding.txtPersenLemak.text = Temp.toInt().toString() +"% Lebih Tinggi"
        }
        else{
            Glide.with(this).load(R.drawable.arrowdown).into(binding.imgDashboardLemak)
            val Temp: Double = ((maxLemak.toDouble()-countLemak.toDouble())/maxLemak.toDouble())*100
            binding.txtPersenLemak.text = Temp.toInt().toString() +"% Lebih Rendah"
        }
    }

    // UpdateData function buat update database Nutrients (kalo user setuju)
    @RequiresApi(Build.VERSION_CODES.O)
    fun UpdateData(){
        var countKalori :Int  = dailyKalori+tempKalori
        var countGula :Int  = dailyGula+tempGula
        var countGaram :Int  = dailyGaram+tempGaram
        var countLemak :Int  = dailyLemak+tempLemak
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)
        val dataNutrients = Nutrients(formatted,countKalori,countGaram,countGula,countLemak)
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        viewModel.updateNutrient(dataNutrients)
    }
}