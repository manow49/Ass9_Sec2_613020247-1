package com.example.lab9mysqlupdatedelete

import Student
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_edit_delete.*
import retrofit2.Response

class EditDeleteActivity : AppCompatActivity() {
    val createClient = StudentAPI.create()
    var mID :String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_delete)

        mID = intent.getStringExtra("mId")
        val mName = intent.getStringExtra("mName")
        val mAge = intent.getStringExtra("mAge")

        edt_id.setText(mID)
        edt_id.isEnabled = false
        edt_name.setText(mName)
        edt_age.setText(mAge)
    }

    fun saveStudent(v: View) {
        createClient.updateStudent(
            edt_id.text.toString(),
            edt_name.text.toString(),
            edt_age.text.toString().toInt()
        )
            .enqueue(object : retrofit2.Callback<Student> {
                override fun onResponse(
                    call: retrofit2.Call<Student>,
                    response: Response<Student>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext, "Successfully Updated", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Update Failure", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: retrofit2.Call<Student>, t: Throwable) = t.printStackTrace()
            })
    }

    fun deleteStudent(v:View){
        val warndialog = AlertDialog.Builder(this)
        warndialog.apply {
            setTitle("Warning")
            setMessage("Do you want to delete the student?")
            setNegativeButton("Yes"){dialog, which->
                createClient.deleteStudent(mID.toString())
                    .enqueue(object :retrofit2.Callback<Student>{
                        override fun onResponse(call: retrofit2.Call<Student>, response: Response<Student>) {
                            if(response.isSuccessful){
                                Toast.makeText(applicationContext,"Successfully Deleted",Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<Student>, t: Throwable) {
                            Toast.makeText(applicationContext,"Delete Failure",Toast.LENGTH_LONG).show()
                        }
                    })
                finish()
            }
            setPositiveButton("No"){dialog, which ->dialog.cancel() }
            show()
        }
    }
}