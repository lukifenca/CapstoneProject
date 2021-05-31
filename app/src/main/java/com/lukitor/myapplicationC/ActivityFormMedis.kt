package com.lukitor.myapplicationC

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lukitor.myapplicationC.data.room.database.UserDatabase
import com.lukitor.myapplicationC.data.room.entity.Nutrients
import com.lukitor.myapplicationC.data.room.entity.Users
import com.lukitor.myapplicationC.databinding.ActivityFormMedisBinding
import com.lukitor.myapplicationC.viewmodel.UserViewModel
import com.lukitor.myapplicationC.viewmodel.ViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.HashMap

class ActivityFormMedis : AppCompatActivity() {

    lateinit var binding:ActivityFormMedisBinding
    private lateinit var userViewModel: UserViewModel
    var dataDiri = HashMap<String, String>()
    var dataMedis = HashMap<String, String>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFormMedisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userViewModel=obtainViewModel(this@ActivityFormMedis)
        binding.pgFormIdentity.progress=100
        if(intent.hasExtra("data"))dataDiri=intent.getSerializableExtra("data") as HashMap<String, String>
        if(intent.hasExtra("medis")){
            dataMedis=intent.getSerializableExtra("medis") as HashMap<String, String>
            binding.etTekananDarah.setText(dataMedis["Darah"])
            binding.etKolesterolLDL.setText(dataMedis["LDL"])
            binding.etKolesterolHDL.setText(dataMedis["HDL"])
            binding.etKolesterolTrigli.setText(dataMedis["Trigli"])
            binding.etGulaDarah.setText(dataMedis["Gula"])
        }

        binding.etTekananDarah.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count>0){
                    binding.txtTekananDarah.error = null
                    binding.txtTekananDarah.helperText = ""
                }
                if(count==0){
                    binding.txtTekananDarah.helperText = "*Required"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.etKolesterolLDL.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count>0){
                    binding.txtInputKolesterolLDL.error = null
                    binding.txtInputKolesterolLDL.helperText = ""
                }
                if(count==0){
                    binding.txtInputKolesterolLDL.helperText = "*Required"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.etKolesterolHDL.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count>0){
                    binding.txtInputKolesterolHDL.error = null
                    binding.txtInputKolesterolHDL.helperText = ""
                }
                if(count==0){
                    binding.txtInputKolesterolHDL.helperText = "*Required"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.etKolesterolTrigli.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count>0){
                    binding.txtInputKolesterolTrigli.error = null
                    binding.txtInputKolesterolTrigli.helperText = ""
                }
                if(count==0){
                    binding.txtInputKolesterolTrigli.helperText = "*Required"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.etGulaDarah.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count>0){
                    binding.txtInputGulaDarah.error = null
                    binding.txtInputGulaDarah.helperText = ""
                }
                if(count==0){
                    binding.txtInputGulaDarah.helperText = "*Required"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        binding.fabBack.setOnClickListener{
            dataMedis["Darah"] = binding.etTekananDarah.text.toString()
            dataMedis["LDL"] = binding.etKolesterolLDL.text.toString()
            dataMedis["HDL"] = binding.etKolesterolHDL.text.toString()
            dataMedis["Trigli"] = binding.etKolesterolTrigli.text.toString()
            dataMedis["Gula"] = binding.etGulaDarah.text.toString()
            var intentKirim= Intent(this, ActivityFormInput::class.java)
            intentKirim.putExtra("data", dataDiri)
            intentKirim.putExtra("medis", dataMedis)

            startActivity(intentKirim)
            finish()
        }

        binding.fabConfirm.setOnClickListener{
            var status = true;
            if (binding.etTekananDarah.text!!.isEmpty()) {
                status = false
                binding.txtTekananDarah.isErrorEnabled = true
                binding.txtTekananDarah.error = "Field Ini Harus Diisi !"
            }
            if (!binding.etTekananDarah.text!!.isEmpty()) {
                var tekananDarah:List<String> = binding.etTekananDarah.text.toString().split("/")
                if(tekananDarah.size<2){
                    status = false
                    binding.txtTekananDarah.isErrorEnabled = true
                    binding.txtTekananDarah.error = "Format pengisian Salah"
                }
            }
            if (binding.etKolesterolHDL.text!!.isEmpty()) {
                status = false
                binding.txtInputKolesterolHDL.isErrorEnabled = true
                binding.txtInputKolesterolHDL.error = "Field Ini Harus Diisi !"
            }
            if (binding.etKolesterolLDL.text!!.isEmpty()){
                status = false
                binding.txtInputKolesterolLDL.isErrorEnabled = true
                binding.txtInputKolesterolLDL.error = "Field Ini Harus Diisi !"
            }
            if (binding.etKolesterolTrigli.text!!.isEmpty()) {
                status = false
                binding.txtInputKolesterolTrigli.isErrorEnabled = true
                binding.txtInputKolesterolTrigli.error = "Field Ini Harus Diisi !"
            }
            if (binding.etGulaDarah.text!!.isEmpty())  {
                status = false
                binding.txtInputGulaDarah.isErrorEnabled = true
                binding.txtInputGulaDarah.error = "Field Ini Harus Diisi !"
            }

            if(status){
                AlertDialog.Builder(this)
                    .setTitle("Confirm")
                    .setMessage("Apakah Data-Data Anda Sudah Benar ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") {
                            dialog: DialogInterface, _: Int ->


                        var tekananDarah:List<String> = binding.etTekananDarah.text.toString().split("/")
                        val users = Users(dataDiri["nama"]!!,
                            dataDiri["umur"]!!,dataDiri["email"]!!,
                        dataDiri["tinggi"]!!.toInt(),dataDiri["berat"]!!.toInt(),
                            tekananDarah[0].toInt(),tekananDarah[1].toInt(),
                            binding.etKolesterolLDL.text.toString().toInt(),
                            binding.etKolesterolHDL.text.toString().toInt(),
                            binding.etKolesterolTrigli.text.toString().toInt(),
                            binding.etGulaDarah.text.toString().toInt()
                        )
                        val current = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val formatted = current.format(formatter)
                        val nutrients = Nutrients(formatted,0,0,0,0)
                        Executors.newSingleThreadExecutor().execute(Runnable {
                            UserDatabase.getInstance(this@ActivityFormMedis).userDao().insertUsers(users) //replace with your code
                            UserDatabase.getInstance(this@ActivityFormMedis).userDao().insertNutrient(nutrients) //replace with your code
                        })
                        var intentt=Intent(this,UserActivity::class.java)
                        startActivity(intentt)
                        finish()
                    }
                    .setNegativeButton("No") {
                            dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
        supportActionBar?.hide()
    }

    override fun onBackPressed() {
        var intentKirim= Intent(this, ActivityFormInput::class.java)
        intentKirim.putExtra("data", dataDiri)
        startActivity(intentKirim)
        finish()
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserViewModel::class.java)
    }
}