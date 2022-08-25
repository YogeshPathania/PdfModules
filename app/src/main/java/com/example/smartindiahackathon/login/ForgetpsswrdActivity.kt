package com.example.smartindiahackathon.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.app.ProgressDialog
import android.widget.Button
import android.widget.EditText
import com.example.smartindiahackathon.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.forgetpsswrd.*
import java.util.regex.Pattern


class ForgetpsswrdActivity : AppCompatActivity() {

    lateinit var button3: Button
    lateinit var gmail: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgetpsswrd)
        button3 = findViewById(R.id.button3)
        gmail = findViewById(R.id.editTextTextEmailAddress)


        setClick()
    }

    private fun setClick() {
        button3.setOnClickListener {
            if (gmail.text.toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Email Address", Toast.LENGTH_SHORT).show()

            } else if (!isValidEmailAddress(gmail.text.toString())) {
                Toast.makeText(this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show()

            } else {
                // progressBar25.visibility= View.VISIBLE
                resetUserPassword(gmail.text.toString())
            }

        }
        imgBack.setOnClickListener {
            finish()
        }
    }

    fun isValidEmailAddress(email: String?): Boolean {
        val ePattern =
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = Pattern.compile(ePattern)
        val m = p.matcher(email)
        return m.matches()
    }

    fun resetUserPassword(email: String?) {
        val mAuth = FirebaseAuth.getInstance()
        val progressDialog = ProgressDialog(this@ForgetpsswrdActivity)
        progressDialog.setMessage("verifying..")
        progressDialog.show()
        mAuth.sendPasswordResetEmail(email!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Reset password instructions has sent to your email",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Email don't exist", Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener { e ->
                progressDialog.dismiss()
            }
    }
}