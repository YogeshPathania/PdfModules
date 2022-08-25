package com.example.smartindiahackathon;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.example.smartindiahackathon.localData.PrefsManager;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressLint("CustomSplashScreen")
public class SPLASHSCREEN extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        PrefsManager.Companion.initialize(this);//initilize the sharedpref
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SPLASHSCREEN.this, DashBoardActivity.class);
            startActivity(intent);
            finish();

        }, 1500);

    }
    public void printHashKey() {
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("HASHkEYS", hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            //  Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            //Log.e(TAG, "printHashKey()", e);
        }
    }
}