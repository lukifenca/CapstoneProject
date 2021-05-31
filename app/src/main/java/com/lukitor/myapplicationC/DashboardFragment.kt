package com.lukitor.myapplicationC

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.lukitor.myapplicationC.data.room.entity.Nutrients
import com.lukitor.myapplicationC.data.room.entity.Users
import com.lukitor.myapplicationC.databinding.FragmentDashboardBinding
import com.lukitor.myapplicationC.viewmodel.UserViewModel
import com.lukitor.myapplicationC.viewmodel.ViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DashboardFragment : Fragment() {

    lateinit var binding:FragmentDashboardBinding
    var maxGaram: Int = 5
    var maxGula: Int = 50
    var maxLemak: Int = 67
    var maxKalori: Double = 0.0
    private lateinit var data: Users
    private lateinit var dataNutrients: Nutrients
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding= FragmentDashboardBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(activity!=null){
            LoadData()
            binding.buttonToCamera.setOnClickListener{
                val intent= Intent(activity,ActivityTakePhoto::class.java)
                startActivity(intent)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DashboardFragment().apply {arguments = Bundle().apply {}}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun LoadData() {
        // Get Data
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        viewModel.getUser().observe(viewLifecycleOwner, { courses ->
            data = Users(courses.nama,courses.umur,courses.email,courses.tinggi,courses.berat,courses.sistolik,courses.diastolik,courses.LDL,courses.HDL,courses.trigliserida, courses.Gula)
            maxKalori = (88.4 + 13.4 * courses.berat.toDouble()) + (4.8 * courses.tinggi.toDouble()) - (5.68 * courses.umur.toDouble())
        })
        viewModel.getNutrient().observe(viewLifecycleOwner, { courses ->
            dataNutrients = Nutrients(courses.tanggal,courses.kalori,courses.garam,courses.gula,courses.lemak)
            Starter()
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Starter(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)
        if (dataNutrients.tanggal == formatted){
            ChangeData()
        }
        else{
            dataNutrients = Nutrients(formatted,0,0,0,0)
            viewModel.updateNutrient(dataNutrients)
            ChangeData()
        }
    }

    fun ChangeData(){
        binding.txtDashboardKalori.text = dataNutrients.kalori.toString() + "/" + maxKalori.toString()
        binding.txtDashboardGula.text = dataNutrients.gula.toString() + "/" + maxGula.toString()
        binding.txtDashboardGaram.text = dataNutrients.garam.toString() + "/" + maxGaram.toString()
        binding.txtDashboardLemak.text = dataNutrients.lemak.toString() + "/" + maxLemak.toString()
        if(dataNutrients.kalori>maxKalori){
            Glide.with(this).load(R.drawable.arrowup).into(binding.imgDashboardKalori)
            val Temp: Double = ((dataNutrients.kalori.toDouble()-maxKalori)/maxKalori) * 100
            binding.txtPersenKalori.text = Temp.toInt().toString() +"% Lebih Tinggi"
        }
        else{
            Glide.with(this).load(R.drawable.arrowdown).into(binding.imgDashboardKalori)
            val Temp = ((maxKalori-dataNutrients.kalori.toDouble())/maxKalori) * 100
            binding.txtPersenKalori.text = Temp.toInt().toString() +"% Lebih Rendah"
        }
        if(dataNutrients.gula>maxGula){
            Glide.with(this).load(R.drawable.arrowup).into(binding.imgDashboardGula)
            val Temp = ((dataNutrients.gula.toDouble()-maxGula)/maxGula) * 100
            binding.txtPersenGula.text = Temp.toInt().toString() +"% Lebih Tinggi"
        }
        else{
            Glide.with(this).load(R.drawable.arrowdown).into(binding.imgDashboardGula)
            val Temp = ((maxGula-dataNutrients.gula.toDouble())/maxGula)*100
            binding.txtPersenGula.text = Temp.toInt().toString() +"% Lebih Rendah"
        }
        if(dataNutrients.garam>maxGaram){
            Glide.with(this).load(R.drawable.arrowup).into(binding.imgDashboardGaram)
            val Temp = ((dataNutrients.garam.toDouble()-maxGaram)/maxGaram)*100
            binding.txtPersenGaram.text = Temp.toInt().toString() +"% Lebih Tinggi"
        }
        else{
            Glide.with(this).load(R.drawable.arrowdown).into(binding.imgDashboardGaram)
            val Temp = ((maxGaram-dataNutrients.garam.toDouble())/maxGaram)*100
            binding.txtPersenGaram.text = Temp.toInt().toString() +"% Lebih Rendah"
        }
        if(dataNutrients.lemak>maxLemak){
            Glide.with(this).load(R.drawable.arrowup).into(binding.imgDashboardLemak)
            val Temp = ((dataNutrients.lemak.toDouble()-maxLemak)/maxLemak)*100
            binding.txtPersenLemak.text = Temp.toInt().toString() +"% Lebih Tinggi"
        }
        else{
            Glide.with(this).load(R.drawable.arrowdown).into(binding.imgDashboardLemak)
            val Temp = ((maxLemak-dataNutrients.lemak.toDouble())/maxLemak)*100
            binding.txtPersenLemak.text = Temp.toInt().toString() +"% Lebih Rendah"
        }
    }
}