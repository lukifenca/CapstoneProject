package com.lukitor.myapplicationC

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lukitor.myapplicationC.databinding.ActivityFormMedisBinding

class ActivityFormMedis : AppCompatActivity() {

    lateinit var binding:ActivityFormMedisBinding
    var dataDiri = HashMap<String, String>()
    var dataMedis = HashMap<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFormMedisBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
}