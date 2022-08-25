package com.example.smartindiahackathon.login


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartindiahackathon.DashBoardActivity
import com.example.smartindiahackathon.Homepage
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.example.smartindiahackathon.login.emailLogin.RegisterActivity
import com.example.smartindiahackathon.R
import com.example.smartindiahackathon.localData.UserManager.getQuestionariesData
import com.example.smartindiahackathon.localData.UserManager.saveQuestionaries
import com.example.smartindiahackathon.localData.UserManager.storeFireBaseData
import com.example.smartindiahackathon.model.UserDataModel
import com.example.smartindiahackathon.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private var callbackManager: CallbackManager? = null

    private lateinit var auth: FirebaseAuth
    var database = FirebaseDatabase.getInstance()
    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
// Initialize Firebase Auth
        auth = Firebase.auth
        mDatabase = FirebaseDatabase.getInstance().reference
        val TextView = findViewById<TextView>(R.id.textView17)
        TextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
                    startActivity (intent)
        }

        initializeGoogle()
        initilizeFb()
        clickListner()

    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    // Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    // Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val uidRef = mDatabase?.child("userdata")?.child("users")?.child(uid)
            val eventListener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        val profileData = getQuestionariesData();
                            progressBar2.visibility=View.GONE
                            profileData?.let { storeFireBaseData(it) }
                            getFirebasedata()

                    } else {
                        getFirebasedata()
                        Toast.makeText(this@LoginActivity, "Welcome Back", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            }
            uidRef?.addListenerForSingleValueEvent(eventListener)

        } else {
            Toast.makeText(this@LoginActivity, "UI not updated login issue", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun clickListner() {
        cvContinueWithGoogle.setOnClickListener(this)
        cvContinueWithFacebook.setOnClickListener(this)
        cvContinueWithEmail.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cvContinueWithGoogle -> {
                startActivityForResult(mGoogleSignInClient.signInIntent, 1653)
                progressBar2.visibility=View.VISIBLE
            }
            R.id.cvContinueWithFacebook -> {
                fb_login_button.performClick()
                progressBar2.visibility=View.VISIBLE
            }

//            R.id.cvContinueWithEmail -> {
//                cvContinueWithEmail.performClick()
//                progressBar2.visibility=View.VISIBLE
//            }
        }
    }

    ///fb listner
    private fun initilizeFb() {
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()

        fb_login_button.setReadPermissions("email", "public_profile")
        fb_login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // Log.d(TAG, "facebook:onSuccess:$loginResult")
                Toast.makeText(this@LoginActivity, "Sucess", Toast.LENGTH_SHORT).show()
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Toast.makeText(this@LoginActivity, "Cancel", Toast.LENGTH_SHORT).show()
                progressBar2.visibility=View.GONE
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_SHORT).show()
                progressBar2.visibility=View.GONE
            }
        })
    }

    //*******************google Account Stuff**********//
    fun initializeGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1653 && data != null) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this@LoginActivity, "Google sign in failed", Toast.LENGTH_SHORT)
                    .show()
                progressBar2.visibility=View.GONE
            }
        }
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    // Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    //  Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    //********************* get firebase data****************//
    private fun getFirebasedata() {
        // calling add value event listener method
        // for getting the values from database.
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
      //  mDatabase?.child("userdata")?.child("users")?.child(uid)
        mDatabase?.child("userdata")?.child("users")?.child(uid)?.addValueEventListener(object : ValueEventListener {
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
                Toast.makeText(this@LoginActivity, "Fail to get data.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkUserFullyRegisterOrNot(model: UserDataModel?) {
        if (Utils.isNetworkConnected(this)) {
            saveQuestionaries(model)// save the firebase data in local

            //********************get the pref data*************//
            val profileData = getQuestionariesData();
            if (profileData == null) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                    val intent = Intent(this@LoginActivity, DashBoardActivity::class.java)
                    startActivity(intent)
                    finish()

            }
            progressBar2.visibility=View.GONE
        }
    }


}


