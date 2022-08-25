package com.example.smartindiahackathon.login.emailLogin

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartindiahackathon.DashBoardActivity
import com.example.smartindiahackathon.Homepage
import com.example.smartindiahackathon.R
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.smartindiahackathon.localData.UserManager
import com.example.smartindiahackathon.login.ForgetpsswrdActivity
import com.example.smartindiahackathon.model.UserDataModel
import com.example.smartindiahackathon.utils.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.emailactivity.*

class LoginWithEmailActivity : AppCompatActivity() {

    lateinit var login: Button
    lateinit var forget: TextView
    lateinit var email: EditText
    lateinit var password: EditText
    private var mDatabase: DatabaseReference? = null

    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emailactivity)


        auth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        forget = findViewById(R.id.textView19)
        email = findViewById(R.id.emailId)
        password = findViewById(R.id.edtPassword)
        login = findViewById(R.id.Login)

        forget.setOnClickListener {
            val intent = Intent(this@LoginWithEmailActivity, ForgetpsswrdActivity::class.java)
            startActivity(intent)
        }
        imgBack.setOnClickListener {
            finish()
        }

        login.setOnClickListener {
            val emailId = email.text.toString()
            val pass = password.text.toString()

            if (emailId.isEmpty()) {
                email.error = "Enter email";
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                password.error = "Enter password";
                return@setOnClickListener
            }
            progressBar22.visibility = View.VISIBLE
            loginUsingEmail(emailId, pass)
        }

    }

    private fun loginUsingEmail(email: String, password: String) {
        print("Tested");
        auth!!.signInWithEmailAndPassword(email, password).addOnSuccessListener { result ->
            print("ON Login $result")
            val user = auth!!.currentUser
            saveUser(user)
        }.addOnCompleteListener {
            print("TASK: ${it.exception}")
            progressBar22.visibility = View.GONE
            // showAlert()
        }.addOnFailureListener {
            print("Login failed : ${it.localizedMessage}")
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT)
                .show()
            progressBar22.visibility = View.GONE
        }
    }

    private fun saveUser(user: FirebaseUser?) {
        if (user != null) {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val uidRef = mDatabase?.child("userdata")?.child("users")?.child(uid)
            val eventListener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        val profileData = UserManager.getQuestionariesData();
                        if (profileData == null) {
                            progressBar22.visibility = View.GONE
                            showAlert()
                        }


                    } else {
                        progressBar22.visibility = View.GONE
                        getFirebasedata()
                        Toast.makeText(
                            this@LoginWithEmailActivity,
                            "Welcome Back",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            }
            uidRef?.addListenerForSingleValueEvent(eventListener)

        } else {
            Toast.makeText(this, "Unable to login", Toast.LENGTH_SHORT)
                .show()

        }


    }

    //********************* get firebase data****************//
    private fun getFirebasedata() {

        // calling add value event listener method
        // for getting the values from database.

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        //  mDatabase?.child("userdata")?.child("users")?.child(uid)
        mDatabase?.child("userdata")?.child("users")?.child(uid)
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(UserDataModel::class.java)
                    checkUserFullyRegisterOrNot(value)

                }

                override fun onCancelled(error: DatabaseError) {
                    // calling on cancelled method when we receive
                    // any error or we are not able to get the data.
                    progressBar22.visibility = View.GONE
                    Toast.makeText(
                        this@LoginWithEmailActivity,
                        "Fail to get data.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun showAlert() {
        AlertDialog.Builder(this)
            .setTitle("Alert")
            .setMessage("User doesn't exist?")
            .setCancelable(false)// Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(android.R.string.yes) { dialog: DialogInterface?, which: Int ->
                // Continue with delete operation
                FirebaseAuth.getInstance().signOut()
                LoginManager.getInstance().logOut()
                UserManager.deleteQuestionaries()
                dialog!!.dismiss()
//
            } // A null listener allows the button to dismiss the dialog and take no further action.
            //.setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }


    private fun checkUserFullyRegisterOrNot(model: UserDataModel?) {
        if (Utils.isNetworkConnected(this)) {
            UserManager.saveQuestionaries(model)// save the firebase data in local

            //********************get the pref data*************//
            val profileData = UserManager.getQuestionariesData();
            if (profileData == null) {
                val intent = Intent(this@LoginWithEmailActivity, LoginWithEmailActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@LoginWithEmailActivity, DashBoardActivity::class.java)
                startActivity(intent)
                finish()
            }
            progressBar22.visibility = View.GONE
        }
    }

}