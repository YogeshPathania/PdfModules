package com.example.smartindiahackathon.login.emailLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartindiahackathon.DashBoardActivity
import com.example.smartindiahackathon.Homepage
import com.example.smartindiahackathon.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.smartindiahackathon.localData.UserManager
import com.example.smartindiahackathon.model.UserDataModel
import com.example.smartindiahackathon.utils.Utils
import com.google.firebase.database.*
import com.example.smartindiahackathon.login.LoginActivity
import kotlinx.android.synthetic.main.register_layout.*
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {
    var Register: Button? = null
    var button2: Button? = null
    var name: EditText? = null
    var txtLastName: EditText? = null
    var CountryName: EditText? = null
    var gmail: EditText? = null
    var number: EditText? = null
    var PasswordNumber: EditText? = null

    private lateinit var auth: FirebaseAuth
    var database = FirebaseDatabase.getInstance()
    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        auth = Firebase.auth
        mDatabase = FirebaseDatabase.getInstance().reference
        Register = findViewById(R.id.Register)
        button2 = findViewById(R.id.button2)
        name = findViewById(R.id.name)
        txtLastName = findViewById(R.id.txtLastName)
        CountryName = findViewById(R.id.CountryName)
        gmail = findViewById(R.id.gmail)
        number = findViewById(R.id.number)
        PasswordNumber = findViewById(R.id.PasswordNumber)
        setClick()
    }

    private fun setClick() {
        Register!!.setOnClickListener { v: View? ->
            if (setValidation()) {
                progressBar24.visibility=View.VISIBLE
                firebaseNewUserAuthenticate(gmail!!.text.toString(),PasswordNumber!!.text.toString())
            }
        }

        button2!!.setOnClickListener { v: View? ->
            val intent = Intent(this, LoginWithEmailActivity::class.java)
            startActivity (intent)
        }
        imgBack.setOnClickListener { v: View? ->
         finish()
        }
    }

    private fun setValidation(): Boolean {
        if (name!!.text.toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show()
            return false
        } else if (txtLastName!!.text.toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_SHORT).show()
            return false
        } else if (CountryName!!.text.toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Country Name", Toast.LENGTH_SHORT).show()
            return false
        } else if (gmail!!.text.toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Email Address", Toast.LENGTH_SHORT).show()
            return false
        } else if (!isValidEmailAddress(gmail!!.text.toString())) {
            Toast.makeText(this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show()
            return false
        } else if (number!!.text.toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Phone No", Toast.LENGTH_SHORT).show()
            return false
        } else if (PasswordNumber!!.text.toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show()
            return false
        } else if (PasswordNumber!!.text.toString().length < 6) {
            Toast.makeText(this, "Password should be greater than 6", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun isValidEmailAddress(email: String?): Boolean {
        val ePattern =
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = Pattern.compile(ePattern)
        val m = p.matcher(email)
        return m.matches()
    }

    fun firebaseNewUserAuthenticate(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                    progressBar24.visibility=View.GONE
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d( "createUserWithEmail", task.exception.toString())
                    progressBar24.visibility=View.GONE
                    Toast.makeText(
                        baseContext, task.exception.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }
    //********************* get firebase data****************//

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val uidRef = mDatabase?.child("userdata")?.child("users")?.child(uid)
            val eventListener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        val profileData = UserManager.getQuestionariesData();
                        if (profileData == null) {
                            progressBar24.visibility = View.GONE
                            val model = UserDataModel()
                            model.name = name!!.text.toString()
                            model.lastName = txtLastName!!.text.toString()
                            model.email = gmail!!.text.toString()
                            model.phoneNo = number!!.text.toString()
                            model.countryName = CountryName!!.text.toString()
                            UserManager.saveQuestionaries(model)
                            profileData?.let { UserManager.storeFireBaseData(it) }
                            getFirebasedata()
                        }
                    } else {
                        getFirebasedata()
                        Toast.makeText(this@RegisterActivity, "Welcome Back", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            }
            uidRef?.addListenerForSingleValueEvent(eventListener)

        } else {
            Toast.makeText(this@RegisterActivity, "UI not updated login issue", Toast.LENGTH_SHORT)
                .show()

        }


    }


    private fun getFirebasedata() {

        // calling add value event listener method
        // for getting the values from database.

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        //  mDatabase?.child("userdata")?.child("users")?.child(uid)
        mDatabase?.child("userdata")?.child("users")?.child(uid)?.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                val value = snapshot.getValue(UserDataModel::class.java)
                checkUserFullyRegisterOrNot(value)
                // after getting the value we are setting
                // our value to our text view in below line.
                //retrieveTV.setText(value)
            }

            override fun onCancelled(error: DatabaseError) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(this@RegisterActivity, "Fail to get data.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun checkUserFullyRegisterOrNot(model: UserDataModel?) {
        if (Utils.isNetworkConnected(this)) {
            UserManager.saveQuestionaries(model)// save the firebase data in local
            //********************get the pref data*************//
            val profileData = UserManager.getQuestionariesData()
            if (profileData == null) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else{
                val intent = Intent(this@RegisterActivity, DashBoardActivity::class.java)
                startActivity(intent)
                finish()
            }
            progressBar24.visibility = View.GONE
        }
    }
}