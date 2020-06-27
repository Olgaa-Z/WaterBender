package com.lauwba.waterbender.towerlamp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.lauwba.waterbender.Config
import com.lauwba.waterbender.R
import com.lauwba.waterbender.RequestHandler
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class TampilTl : AppCompatActivity() , View.OnClickListener{

    private var editid: EditText? = null
    private var edittl: EditText? = null
    private var editshift: EditText? = null
    private var editstatus: EditText? = null
    private var edithm: EditText? = null
    private var editfuel: EditText? = null

    private var buttonUpdate: Button? = null
    private var buttonDelete: Button? = null

    private var id: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tampil_tl)

        val intent = intent

        id = intent.getStringExtra(Config.EMP_ID)

        editid = findViewById(R.id.editid) as EditText
        edittl = findViewById(R.id.edittl) as EditText
        editshift = findViewById(R.id.editshift) as EditText
        editstatus = findViewById(R.id.editstatus) as EditText
        edithm = findViewById(R.id.edithm) as EditText
        editfuel = findViewById(R.id.editfuel) as EditText

        buttonUpdate = findViewById(R.id.buttonUpdate) as Button
        buttonDelete = findViewById(R.id.buttonDelete) as Button

        buttonUpdate!!.setOnClickListener(this)
        buttonDelete!!.setOnClickListener(this)

        editid!!.setText(id)

//        getEmployee()
    }

    private fun getEmployee() {
        @Suppress("DEPRECATION")
        class GetEmployee : AsyncTask<Void, Void, String>() {
            lateinit var loading: ProgressDialog
            override fun onPreExecute() {
                super.onPreExecute()
                loading =
                    ProgressDialog.show(this@TampilTl, "Fetching...", "Wait...", false, false)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                loading.dismiss()
                showEmployee(s)
            }

            override fun doInBackground(vararg params: Void): String {
                val rh = RequestHandler()
                return rh.sendGetRequestParam(Config.URL_GET_EMP , id)
            }
        }

        val ge = GetEmployee()
        ge.execute()
    }

    private fun showEmployee(json: String) {
        try {
            val jsonObject = JSONObject(json)
            val result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY)
            val c = result.getJSONObject(0)
            val towerlamp = c.getString(Config.TAG_TL)
            val shift = c.getString(Config.TAG_SHIFT)
            val status = c.getString(Config.TAG_STATUS)
            val hm = c.getString(Config.TAG_HM)
            val fuel = c.getString(Config.TAG_FUEL)

            edittl?.setText(towerlamp)
            editshift?.setText(shift)
            editstatus?.setText(status)
            edithm?.setText(hm)
            editfuel?.setText(fuel)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    private fun updateEmployee() {
        val tl = edittl?.getText().toString().trim { it <= ' ' }
        val shift = editshift?.getText().toString().trim { it <= ' ' }
        val status = editstatus?.getText().toString().trim { it <= ' ' }
        val hm = edithm?.getText().toString().trim { it <= ' ' }
        val fuel = editfuel?.getText().toString().trim { it <= ' ' }

        class UpdateEmployee : AsyncTask<Void, Void, String>() {
            lateinit var loading: ProgressDialog
            override fun onPreExecute() {
                super.onPreExecute()
                loading =
                    ProgressDialog.show(this@TampilTl, "Updating...", "Wait...", false, false)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                loading.dismiss()
                Toast.makeText(this@TampilTl, s, Toast.LENGTH_LONG).show()
            }

//            override fun doInBackground(vararg params: Void): String {
//                val hashMap = HashMap<String, String>()
//                hashMap[konfigurasi.KEY_EMP_ID] = id
//                hashMap[konfigurasi.KEY_EMP_NAMA] = name
//                hashMap[konfigurasi.KEY_EMP_POSISI] = desg
//                hashMap[konfigurasi.KEY_EMP_GAJIH] = salary
//
//                val rh = RequestHandler()
//
//                return rh.sendPostRequest(Config.URL_UPDATE_EMP, hashMap)
//            }

            @SuppressLint("WrongThread")
            override fun doInBackground(vararg v: Void): String {
//                val params = HashMap<String, String>()
                val params = HashMap<String, String?>()
                params[Config.KEY_EMP_ID] = id
                params[Config.KEY_EMP_TL] = tl
                params[Config.KEY_EMP_SHIFT] = shift
                params[Config.KEY_EMP_STATUS] = status
                params[Config.KEY_EMP_HM] = hm
                params[Config.KEY_EMP_FUEL] = fuel

                val rh = RequestHandler()
                return rh.sendPostRequest(Config.URL_UPDATE_EMP, params)

            }
        }

        val ue = UpdateEmployee()
        ue.execute()
    }

    private fun deleteEmployee() {
        class DeleteEmployee : AsyncTask<Void, Void, String>() {
            lateinit var loading: ProgressDialog

            override fun onPreExecute() {
                super.onPreExecute()
                loading = ProgressDialog.show(
                    this@TampilTl,
                    "Updating...",
                    "Tunggu...",
                    false,
                    false
                )
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                loading.dismiss()
                Toast.makeText(this@TampilTl, s, Toast.LENGTH_LONG).show()
            }

            override fun doInBackground(vararg params: Void): String {
                val rh = RequestHandler()
                return rh.sendGetRequestParam(Config.URL_DELETE_EMP, id)
            }
        }

        val de = DeleteEmployee()
        de.execute()
    }

    private fun confirmDeleteEmployee() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Pegawai ini?")

        alertDialogBuilder.setPositiveButton("Ya") { arg0, arg1 ->
            deleteEmployee()
            startActivity(Intent(this@TampilTl, TampilSemuaTl::class.java))
        }

        alertDialogBuilder.setNegativeButton("Tidak",
            object: DialogInterface.OnClickListener {

                override fun onClick(arg0: DialogInterface, arg1:Int) {

                }
            })

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onClick(v: View?) {
        if (v === buttonUpdate) {
            updateEmployee()
        }

        if (v === buttonDelete) {
            confirmDeleteEmployee()
        }
    }
}
