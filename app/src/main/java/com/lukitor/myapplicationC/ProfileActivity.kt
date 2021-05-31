package com.lukitor.myapplicationC

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.lukitor.myapplicationC.data.room.entity.Users
import com.lukitor.myapplicationC.databinding.ActivityProfileBinding
import com.lukitor.myapplicationC.viewmodel.UserViewModel
import com.lukitor.myapplicationC.viewmodel.ViewModelFactory

class ProfileActivity : AppCompatActivity() {
    // Variable
    private lateinit var binding: ActivityProfileBinding
    private lateinit var fromsmall: Animation
    private lateinit var fromnothing: Animation
    private lateinit var foricon: Animation
    private lateinit var togo: Animation
    private lateinit var data: Users
    private lateinit var viewModel: UserViewModel
    private lateinit var tipe: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationIcon(R.drawable.iconback)
        setSupportActionBar(binding.toolbar)

        // Getdata
        LoadData()
        // Animation
        Animation()
        // Edit
        editData()
    }

    fun LoadData(){
        // Get Data
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        viewModel.getUser().observe(this, { courses ->
            binding.txtProfileNama.text = courses.nama
            binding.txtProfileUmur.text = courses.umur + " Thn"
            binding.txtProfileBerat.text = courses.berat.toString() + " kg"
            binding.txtProfileTinggi.text = courses.tinggi.toString() + " cm"

            binding.txtguladarah.text = courses.Gula.toString() + " mg/dL"
            if(courses.Gula < 108 ){
                binding.txtguladarah2.text = "*"
                binding.txtguladarah2.setTextColor("#00FF0A".toColor())
            }
            else if(courses.Gula in 108..124){
                binding.txtguladarah2.text = "**"
                binding.txtguladarah2.setTextColor("#FF9801".toColor())
            }
            else if(courses.Gula >= 125){
                binding.txtguladarah2.text = "***"
                binding.txtguladarah2.setTextColor("#FD0000".toColor())
            }

            binding.txttensi.text = courses.sistolik.toString() + "/" + courses.diastolik.toString() + " mmHg"
            if(courses.sistolik < 139 && courses.diastolik <= 89){
                binding.txttensi2.text = "*"
                binding.txttensi2.setTextColor("#00FF0A".toColor())
            }
            else if((courses.sistolik in 140..159) && (courses.diastolik in 90..99)){
                binding.txttensi2.text = "**"
                binding.txttensi2.setTextColor("#FF9801".toColor())
            }
            else if(courses.sistolik > 159 && courses.diastolik > 99){
                binding.txttensi2.text = "***"
                binding.txttensi2.setTextColor("#FD0000".toColor())
            }

            binding.txtkolestrol.text = courses.LDL.toString() + "/" + courses.HDL.toString() + "/" + courses.trigliserida.toString() + " mg/dL"
            val kolestrolTotal: Double = courses.LDL.toDouble() + courses.HDL.toDouble() + (courses.trigliserida.toDouble() * 0.2)
            if(kolestrolTotal < 200){
                binding.txtkolestrol2.text = "*"
                binding.txtkolestrol2.setTextColor("#00FF0A".toColor())
            }
            else if(kolestrolTotal >= 200 && kolestrolTotal < 240){
                binding.txtkolestrol2.text = "**"
                binding.txtkolestrol2.setTextColor("#FF9801".toColor())
            }
            else if(kolestrolTotal >= 240){
                binding.txtkolestrol2.text = "***"
                binding.txtkolestrol2.setTextColor("#FD0000".toColor())
            }

            binding.txtBerat.text = courses.berat.toString() + " kg"
            val bbIdeal: Double =  courses.berat.toDouble() / ((courses.tinggi.toDouble()/100)*1.7)
            if(bbIdeal <= 25){
                binding.txtBerat2.text = "*"
                binding.txtBerat2.setTextColor("#00FF0A".toColor())
            }
            else if(bbIdeal > 25 && bbIdeal < 40){
                binding.txtBerat2.text = "**"
                binding.txtBerat2.setTextColor("#FF9801".toColor())
            }
            else if(bbIdeal > 40){
                binding.txtBerat2.text = "***"
                binding.txtBerat2.setTextColor("#FD0000".toColor())
            }
            binding.txtTinggi.text = courses.tinggi.toString() + " cm"
            binding.txtUsia.text = courses.umur + " Tahun"
            data = Users(courses.nama,courses.umur,courses.email,courses.tinggi,courses.berat,courses.sistolik,courses.diastolik,courses.LDL,courses.HDL,courses.trigliserida,courses.Gula)
        })
    }

    fun Animation(){
        binding.toolbar.setNavigationOnClickListener{view -> finish()
            overridePendingTransition(R.transition.nothing,R.transition.bottom_down)}
        binding.collapsingToolbar.title = "Your Profile"
        binding.collapsingToolbar.setCollapsedTitleTextColor(
            ContextCompat.getColor(this, R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(Color.argb(0, 255, 0, 0))
        fromsmall = AnimationUtils.loadAnimation(this,R.anim.fromsmall)
        fromnothing = AnimationUtils.loadAnimation(this,R.anim.fromnothing)
        foricon = AnimationUtils.loadAnimation(this,R.anim.foricon)
        togo = AnimationUtils.loadAnimation(this,R.anim.togo)
        binding.mykonten.alpha = 0f
        binding.overbox.alpha = 0f
        binding.imagetemp.visibility = View.GONE
    }

    fun editData(){
        binding.imgsettinggula.setOnClickListener { view ->
            binding.txtEdit.text = "Edit Data Gula Darah Anda"
            binding.et1.hint = "Gula Darah Puasa 200 mg/dL"
            tipe = "Gula"
            modalAnim()
        }

        binding.imgsettingberat.setOnClickListener { view ->
            binding.txtEdit.text = "Edit Data Berat Badan Anda"
            binding.et1.hint = "Berat Badan 90 kg"
            tipe = "Berat"
            modalAnim()
        }

        binding.imgsettingtinggi.setOnClickListener { view ->
            binding.txtEdit.text = "Edit Data Tinggi Badan Anda"
            binding.et1.hint = "Tinggi Badan 180 cm"
            tipe = "Tinggi"
            modalAnim()
        }

        binding.imgsettingUsia.setOnClickListener { view ->
            binding.txtEdit.text = "Edit Data Usia Anda"
            binding.et1.hint = "Usia 19 Tahun"
            tipe = "Usia"
            modalAnim()
        }

        binding.imgsettingdarahtinggi.setOnClickListener { view ->
            binding.txtEdit.text = "Edit Data Tensi Darah Anda"
            binding.et1.hint = "Sistole 120 mg/dL"
            binding.et2.hint = "Diastole 90 mmHg"
            tipe = "Tensi"
            modalAnim()
            binding.et2.visibility = View.VISIBLE
        }

        binding.imgsettingkolestrol.setOnClickListener { view ->
            binding.txtEdit.text = "Edit Data Kolestrol Anda"
            binding.et1.hint = "LDL 150 mg/dL"
            binding.et2.hint = "HDL 111 mg/dL"
            binding.et3.hint = "Trigliserida 1000 mg/dL"
            tipe = "Kolestrol"
            modalAnim()
            binding.et2.visibility = View.VISIBLE
            binding.et3.visibility = View.VISIBLE
        }

        binding.imgClose.setOnClickListener { view ->
            binding.overbox.startAnimation(togo)
            binding.imagetemp.startAnimation(togo)
            binding.mykonten.startAnimation(togo)
            binding.imagetemp.visibility = View.GONE
            ViewCompat.animate(binding.mykonten).setStartDelay(1000).alpha(0f).start()
            ViewCompat.animate(binding.overbox).setStartDelay(1000).alpha(0f).start()
            clearET()
        }

        binding.btnConfirm.setOnClickListener { view ->
            binding.overbox.startAnimation(togo)
            binding.imagetemp.startAnimation(togo)
            binding.mykonten.startAnimation(togo)
            binding.imagetemp.visibility = View.GONE
            ViewCompat.animate(binding.mykonten).setStartDelay(1000).alpha(0f).start()
            ViewCompat.animate(binding.overbox).setStartDelay(1000).alpha(0f).start()
            if (tipe == "Gula"){
                data.Gula = binding.et1.text.toString().toInt()
            }
            else if (tipe == "Tensi"){
                data.sistolik = binding.et1.text.toString().toInt()
                data.diastolik = binding.et2.text.toString().toInt()
            }
            else if (tipe == "Berat"){
                data.berat = binding.et1.text.toString().toInt()
            }
            else if (tipe == "Tinggi"){
                data.tinggi = binding.et1.text.toString().toInt()
            }
            else if (tipe == "Usia"){
                data.umur = binding.et1.text.toString()
            }
            else if (tipe == "Kolestrol"){
                data.LDL = binding.et1.text.toString().toInt()
                data.HDL = binding.et2.text.toString().toInt()
                data.trigliserida = binding.et3.text.toString().toInt()
            }
            viewModel.update(data)
            LoadData()
            clearET()
        }
    }
    fun modalAnim(){
        binding.et2.visibility = View.INVISIBLE
        binding.et3.visibility = View.INVISIBLE
        binding.imagetemp.visibility = View.VISIBLE
        binding.imagetemp.startAnimation(foricon)
        binding.overbox.alpha = 1f
        binding.overbox.startAnimation(fromnothing)
        binding.mykonten.alpha = 1f
        binding.mykonten.startAnimation(fromsmall)
    }
    fun clearET(){
        binding.et1.setText("")
        binding.et2.setText("")
        binding.et3.setText("")
    }
    fun String.toColor(): Int = Color.parseColor(this)
}