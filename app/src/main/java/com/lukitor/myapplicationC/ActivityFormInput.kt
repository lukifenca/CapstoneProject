package com.lukitor.myapplicationC

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lukitor.myapplicationC.databinding.ActivityFormInputBinding


class ActivityFormInput : AppCompatActivity() {

    lateinit var binding: ActivityFormInputBinding
    var dataDiri = HashMap<String, String>()
    var dataMedis = HashMap<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFormInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("medis")){
            dataMedis=intent.getSerializableExtra("medis") as HashMap<String, String>
        }

        if(intent.hasExtra("data")){
            dataDiri=intent.getSerializableExtra("data") as HashMap<String, String>
            binding.etNama.setText(dataDiri["nama"])
            binding.etUmur.setText(dataDiri["umur"])
            binding.etEmail.setText(dataDiri["email"])
            binding.etTinggiBadan.setText(dataDiri["tinggi"])
            binding.etBeratBadan.setText(dataDiri["berat"])
        }
        binding.pgFormIdentity.progress=50


        binding.etNama.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count>0){
                    binding.txtFieldNama.error = null
                    binding.txtFieldNama.helperText = ""
                }
                if(count==0){
                    binding.txtFieldNama.helperText = "*Required"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.etUmur.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count>0){
                    binding.txtUmur.error = null
                    binding.txtUmur.helperText = ""
                }
                if(count==0){
                    binding.txtUmur.helperText = "*Required"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count>0){
                    binding.txtEmail.error = null
                    binding.txtEmail.helperText = ""
                }
                if(count==0){
                    binding.txtEmail.helperText = "*Required"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.etBeratBadan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count>0){
                    binding.txtInputBeratBadan.error = null
                    binding.txtInputBeratBadan.helperText = ""
                }
                if(count==0){
                    binding.txtInputBeratBadan.helperText = "*Required"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.etTinggiBadan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count>0){
                    binding.txtInputTinggiBadan.error = null
                    binding.txtInputTinggiBadan.helperText = ""
                }
                if(count==0){
                    binding.txtInputTinggiBadan.helperText = "*Required"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.fabNextToBM.setOnClickListener(View.OnClickListener {

            var status = true;
            if (binding.etNama.text!!.isEmpty()) {
                status = false
                binding.txtFieldNama.isErrorEnabled = true
                binding.txtFieldNama.error = "Field Ini Harus Diisi !"
            }
            if (binding.etUmur.text!!.isEmpty()) {
                status = false
                binding.txtUmur.isErrorEnabled = true
                binding.txtUmur.error = "Field Ini Harus Diisi !"
            }
            if (binding.etEmail.text!!.isEmpty()){
                status = false
                binding.txtEmail.isErrorEnabled = true
                binding.txtEmail.error = "Field Ini Harus Diisi !"
            }
            if (binding.etBeratBadan.text!!.isEmpty()) {
                status = false
                binding.txtInputBeratBadan.isErrorEnabled = true
                binding.txtInputBeratBadan.error = "Field Ini Harus Diisi !"
            }
            if (binding.etTinggiBadan.text!!.isEmpty())  {
                status = false
                binding.txtInputTinggiBadan.isErrorEnabled = true
                binding.txtInputTinggiBadan.error = "Field Ini Harus Diisi !"
            }

            if (status) {
                dataDiri["nama"] = binding.etNama.text.toString()
                dataDiri["umur"] = binding.etUmur.text.toString()
                dataDiri["email"] = binding.etEmail.text.toString()
                dataDiri["berat"] = binding.etBeratBadan.text.toString()
                dataDiri["tinggi"] = binding.etTinggiBadan.text.toString()
                var intentKirim = Intent(this, ActivityFormMedis::class.java)
                intentKirim.putExtra("data", dataDiri)
                intentKirim.putExtra("medis", dataMedis)
                startActivity(intentKirim)
            }
        })
        supportActionBar?.hide()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}