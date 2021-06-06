package com.lukitor.myapplicationC

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.lukitor.myapplicationC.data.room.entity.Nutrients
import com.lukitor.myapplicationC.databinding.ActivityTakePhotoBinding
import com.lukitor.myapplicationC.retrofit.ApiConfig
import com.lukitor.myapplicationC.retrofit.NutrientResponses
import com.lukitor.myapplicationC.viewmodel.UserViewModel
import com.lukitor.myapplicationC.viewmodel.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt


class ActivityTakePhoto : AppCompatActivity() {
    var maxGaram: Int = 5000; var maxGula: Int = 50; var maxLemak: Int = 67; var maxKalori: Int = 0;
    var dailyGaram: Int = 0; var dailyGula: Int = 0; var dailyLemak: Int = 0; var dailyKalori: Int = 0;
    var tempGaram: Int = 0; var tempGula: Int = 0; var tempLemak: Int = 0; var tempKalori: Int = 0;
    private lateinit var fromsmall: Animation
    private lateinit var fromnothing: Animation
    private lateinit var foricon: Animation
    private lateinit var togo: Animation

    //buat hitung perbandingan
    var measurement:Int=0; var perbandingan:Int=0;

    //berat makanan inputan user
    var userInputGram:Int=500

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
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "Eunonia channel"
    }

    private fun initClassifier() {
        classifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
    }

    fun anim(){
        fromsmall = AnimationUtils.loadAnimation(this,R.anim.fromsmall)
        fromnothing = AnimationUtils.loadAnimation(this,R.anim.fromnothing)
        foricon = AnimationUtils.loadAnimation(this,R.anim.foricon)
        togo = AnimationUtils.loadAnimation(this,R.anim.togo)
        binding.mykonten.alpha = 0f
        binding.overbox.alpha = 0f
        binding.imagetemp.visibility = View.GONE
    }
    fun modalAnim(){
        binding.imagetemp.visibility = View.VISIBLE
        binding.imagetemp.startAnimation(foricon)
        binding.overbox.alpha = 1f
        binding.overbox.startAnimation(fromnothing)
        binding.mykonten.alpha = 1f
        binding.mykonten.startAnimation(fromsmall)
    }
    fun removeModalAnim(){
        binding.overbox.startAnimation(togo)
        binding.imagetemp.startAnimation(togo)
        binding.mykonten.startAnimation(togo)
        binding.imagetemp.visibility = View.GONE
        ViewCompat.animate(binding.mykonten).setStartDelay(1000).alpha(0f).start()
        ViewCompat.animate(binding.overbox).setStartDelay(1000).alpha(0f).start()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClassifier()
        anim()
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
            modalAnim()
        }
        binding.btnConfirm.setOnClickListener{
            removeModalAnim()
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
                var namamakanan = result[0].title
                if(namamakanan=="bakso")namamakanan="meatballs"
                else if(namamakanan=="donuts")namamakanan="donut gula"
                else if(namamakanan=="pancakes")namamakanan="pancakes gula"
                else if(namamakanan=="waffles")namamakanan="waffle"
                else if(namamakanan=="onion rings")namamakanan="onion ring carl"
                userInputGram = binding.et1.text.toString().toInt()
                callApi(namamakanan)
                binding.layoutHasil.visibility = View.VISIBLE
                binding.layoutHasil.startAnimation(anim)
                binding.txtHasilFoodName.text=result[0].title
            }
            binding.et1.setText("")
        }
        binding.btnConfFood.setOnClickListener{
            AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Apakah Deteksi Makanan Sudah Benar ?")
                .setCancelable(false)
                .setPositiveButton("Yes") {
                        dialog: DialogInterface, _: Int ->
                    UpdateData()
                    finish()
                    overridePendingTransition(R.transition.nothing,R.transition.bottom_down)
                }
                .setNegativeButton("No") {
                        dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
                .show()
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


    private fun callApi(foodName:String){
        val api = ApiConfig.provideApiService()
        val upload :Call<NutrientResponses> = api.getNutrients(foodName)
        upload.enqueue(object : Callback<NutrientResponses> {
            override fun onResponse(
                call: Call<NutrientResponses>,
                response: Response<NutrientResponses>
            ) {
                if (response.body()!=null) {
//                    binding.txtNutritionKalori.text=response.body()!!.calories
                    tempKalori=response.body()!!.calories!!.toFloat().roundToInt()
//                    binding.txtNutritionGula.text=response.body()!!.sugar
                    tempGula=response.body()!!.sugar!!.toFloat().roundToInt()
//                    binding.txtNutritionLemak.text=response.body()!!.fat
                    tempLemak=response.body()!!.fat!!.toFloat().roundToInt()
//                    binding.txtNutritionSodium.text=response.body()!!.sodium
                    tempGaram=response.body()!!.sodium!!.toFloat().roundToInt()

                    measurement=response.body()!!.metricServingAmount!!.toFloat().roundToInt()
                    hitungperbandingan()
                    var namatemp=binding.txtHasilFoodName.text
                    binding.txtHasilFoodName.text = "$namatemp (${userInputGram.toString()}g)"

                    binding.txtNutritionKalori.text=tempKalori.toString()
                    binding.txtNutritionGula.text=tempGula.toString()
                    binding.txtNutritionLemak.text=tempLemak.toString()
                    binding.txtNutritionSodium.text=tempGaram.toString()

                    ChangeData()
//                    Toast.makeText(
//                        this@ActivityTakePhoto,
//                        "Berhasil Response",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }

            override fun onFailure(call: Call<NutrientResponses>, t: Throwable) {
                Log.d("RETRO", "ON FAILURE : " + t.message)
            }
        })
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
        if(countKalori>maxKalori)sendNotification("Kalori",1)

        var countGula :Int  = dailyGula+tempGula
        Log.d("Notif","Daily Gula :$countGula - Max Gula : {$maxGula}")
        if(countGula > maxGula)sendNotification("Gula",2)

        var countGaram :Int  = dailyGaram+tempGaram
        if(countGaram>maxGaram)sendNotification("Sodium",3)

        var countLemak :Int  = dailyLemak+tempLemak
        if(countLemak>maxLemak)sendNotification("Lemak",4)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)
        val dataNutrients = Nutrients(1,formatted,countKalori,countGaram,countGula,countLemak)
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        viewModel.updateNutrient(dataNutrients)
    }

    fun sendNotification(Jenis:String, jenisID:Int) {
        Log.d("Notif","Masuk $Jenis")
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_alert)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.notification_alert))
            .setContentTitle("Nutrisi Harian Melebihi Rekomendasi Harian")
            .setContentText("$Jenis anda melebih batas harian !")
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_NAME
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()
        mNotificationManager.notify(jenisID, notification)
    }

    //buat hitung perbandingan
    fun hitungperbandingan(){
        perbandingan=(userInputGram/measurement).toFloat().roundToInt()
//        Log.d("hasil",perbandingan.toString())
        updateNutrientsBanding()
    }


    //buat update hasil data temp setelah dapet perbandingan
    fun updateNutrientsBanding(){
        tempKalori*=perbandingan
        tempGula*=perbandingan
        tempLemak*=perbandingan
        tempGaram*=perbandingan
    }
}