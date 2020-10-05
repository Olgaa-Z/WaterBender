package com.lauwba.waterbender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lauwba.waterbender.retrofit.API
import com.lauwba.waterbender.retrofit.APIRespon
import com.lauwba.waterbender.retrofit.RetrofitClient
import com.lauwba.waterbender.towerlamp.TowerLampInput
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    //Pemesanan Variabel
    lateinit var myAPI: API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Inisialisasi variabel
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(API::class.java)


        //Metode tombol register
        btn_daftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        //metode tombol masuk
        btn_masuk.setOnClickListener {
            if (log_nrp.text.toString().isEmpty()) {
                log_nrp.setError("Masukkan NRP")
                log_nrp.requestFocus()
                return@setOnClickListener
            }
            if (log_pass.text.toString().isEmpty()) {
                log_pass.setError("Masukkan Password")
                log_pass.requestFocus()
                return@setOnClickListener
            }
            ceklogin (log_nrp.text.toString(), log_pass.text.toString())
        }
    }

    private fun ceklogin(nrp: String, pass: String) {
        myAPI.loginUser(nrp, pass)
            .enqueue(object :Callback<APIRespon>{
                override fun onFailure(call: Call<APIRespon>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<APIRespon>, response: Response<APIRespon>) {
                    // Toast.makeText(this@LoginActivity, response.body()!!.nama, Toast.LENGTH_SHORT).show()
                    if (response.body()!!.error) {
                        Toast.makeText(this@LoginActivity, response.body()!!.error_pesan, Toast.LENGTH_SHORT).show()
                    } else {
                        intent= Intent(applicationContext, TowerLampInput::class.java)
                        startActivity(intent)
                        Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            })
    }
}
