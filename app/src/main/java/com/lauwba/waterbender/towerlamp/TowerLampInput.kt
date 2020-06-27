package com.lauwba.waterbender.towerlamp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.lauwba.waterbender.Config
import com.lauwba.waterbender.R
import com.lauwba.waterbender.RequestHandler

class TowerLampInput : AppCompatActivity(), View.OnClickListener {


    //Dibawah ini merupakan perintah untuk mendefinikan View
    private var idtl: EditText? = null
    private var shift: EditText? = null
    private var status: EditText? = null
    private var hm: EditText? = null
    private var fuel: EditText? = null

    private var buttoninput: Button? = null
    private var buttonview: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tower_lamp_input)

        //Inisialisasi dari View
        idtl = findViewById(R.id.idtl) as EditText
        shift = findViewById(R.id.shift) as EditText
        status = findViewById(R.id.status) as EditText
        hm = findViewById(R.id.hm) as EditText
        fuel = findViewById(R.id.fuel) as EditText

        buttoninput = findViewById(R.id.buttoninput) as Button
        buttonview = findViewById(R.id.buttonview) as Button

        //Setting listeners to button
        buttoninput!!.setOnClickListener(this)
        buttonview!!.setOnClickListener(this)
    }

    //Dibawah ini merupakan perintah untuk Menambahkan Pegawai (CREATE)
    private fun addEmployee() {

        val idtl = idtl?.getText().toString().trim { it <= ' ' }
        val shift = shift?.getText().toString().trim { it <= ' ' }
        val status = status?.getText().toString().trim { it <= ' ' }
        val hm = hm?.getText().toString().trim { it <= ' ' }
        val fuel = fuel?.getText().toString().trim { it <= ' ' }


        class AddEmployee : AsyncTask<Void, Void, String>() {

            lateinit var loading: ProgressDialog

            override fun onPreExecute() {
                super.onPreExecute()
                loading = ProgressDialog.show(
                    this@TowerLampInput,
                    "Menambahkan...",
                    "Tunggu...",
                    false,
                    false
                )
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                loading.dismiss()
                Toast.makeText(this@TowerLampInput, s, Toast.LENGTH_LONG).show()
            }

            @SuppressLint("WrongThread")
            override fun doInBackground(vararg v: Void): String {
                val params = hashMapOf<String, String?>()
                params[Config.KEY_EMP_TL] = idtl
                params[Config.KEY_EMP_SHIFT] = shift
                params[Config.KEY_EMP_STATUS] = status
                params[Config.KEY_EMP_HM] = hm
                params[Config.KEY_EMP_FUEL] = fuel

                val rh = RequestHandler()
                return rh.run { sendPostRequest(Config.URL_ADD, params) }

            }
        }

        val ae = AddEmployee()
        ae.execute()
    }

    override fun onClick(v: View) {
        if (v === buttoninput) {
            addEmployee()
        }

        if (v === buttonview) {
            startActivity(Intent(this, TampilSemuaTl::class.java))
        }
    }
}
