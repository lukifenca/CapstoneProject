package com.lukitor.myapplicationC

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lukitor.myapplicationC.databinding.ActivityGetStartedHomeBinding
import com.lukitor.myapplicationC.data.room.database.UserDatabase
import com.lukitor.myapplicationC.data.room.entity.Users
import com.lukitor.myapplicationC.viewmodel.UserViewModel
import com.lukitor.myapplicationC.viewmodel.ViewModelFactory
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

class GetStartedHomeActivity : AppCompatActivity() {

    lateinit var sliderView: SliderView
    lateinit var adapter: SliderAdapter
    lateinit var binding: ActivityGetStartedHomeBinding

    private lateinit var userDatabase: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        val mainViewModel = obtainViewModel(this@GetStartedHomeActivity)
        mainViewModel.getUser().observe(this, usersObserver)


        sliderView = binding.imageSlider
        adapter=SliderAdapter(this)
        sliderView.setSliderAdapter(adapter)

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.indicatorSelectedColor = Color.WHITE
        sliderView.indicatorUnselectedColor = Color.GRAY
        sliderView.scrollTimeInSec = 3
        sliderView.isAutoCycle = true
        sliderView.startAutoCycle()
        addNewItem()

//        binding.btnGetStarted.setOnClickListener{
//            val intentt= Intent(this,ActivityFormInput::class.java)
//            startActivity(intentt)
//            finish()
//        }
    }

    fun addNewItem() {
        val sliderItem = SliderItem()
        sliderItem.description = "Bergabunglah Bersama Kami"
        sliderItem.image= R.drawable.gambar1m_1080x1920
        adapter.addItem(sliderItem)
        val sliderItem2 = SliderItem()
        sliderItem2.description = "Kami Hadir Untuk Membantu Mengontrol Makanan Anda"
        sliderItem2.image= R.drawable.gambar3m_1080x1920
        adapter.addItem(sliderItem2)
        val sliderItem3 = SliderItem()
        sliderItem3.description = ""
        sliderItem3.image= R.drawable.gambar3m_1080x1920
        adapter.addItem(sliderItem3)
        val sliderItem4 = SliderItem()
        sliderItem4.description = "Daftarkan Dirimu Sekarang !"
        sliderItem4.image= R.drawable.gambar1m_1080x1920
        adapter.addItem(sliderItem4)
    }


    private fun obtainViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserViewModel::class.java)
    }

    private val usersObserver = Observer<Users> { users ->
        if (users != null) {
            var intentt= Intent(this,UserActivity::class.java)
            startActivity(intentt)
        }
    }
}