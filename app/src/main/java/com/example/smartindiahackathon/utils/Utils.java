package com.example.smartindiahackathon.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.IOException;
import java.util.ArrayList;


public class Utils {

    static Context context;

    public Utils(Context context) {
        this.context = context;
    }


    public static int REQUEST_WRITE_STORAGE_REQUEST_CODE = 111;

    public static boolean checkPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }

        if (hasReadPermissions(context) && hasWritePermissions(context)) {
            return true;
        } else {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, REQUEST_WRITE_STORAGE_REQUEST_CODE);
            return false;
        }
    }

    private static boolean hasReadPermissions(Context context) {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private static boolean hasWritePermissions(Context context) {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }



    public static void setPref(Context context, String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value);
        editor.apply();
    }
    public static String getPref(Context context, String key, String value) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, value);
    }


    public static void setPref(Context context, String key, Integer value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value);
        editor.apply();
    }

    public static Integer getPref(Context context, String key, Integer value) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, value);
    }



//    public static boolean isNetworkConnected() {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
//    }
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    static Dialog adsDialog;


    public static void dismissDialog(){
        if (adsDialog != null && adsDialog.isShowing()) {
            adsDialog.dismiss();
        }
    }


    @SuppressLint("CommitPrefEdits")
    public static void setString(Context context,String pref,String value){
         SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit().putString(pref,value);
    }

    public static String getString(Context context,String pref,String value){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(pref,value);
    }

    public static ArrayList<String> getAssetItems(Context mContext, String categoryName) {
        ArrayList<String> arrayList = new ArrayList<>();
        String[] imgPath;
        AssetManager assetManager = mContext.getAssets();
        try {
            imgPath = assetManager.list(categoryName);
            if (imgPath != null) {
                for (String anImgPath : imgPath) {
                    arrayList.add("///android_asset/" + categoryName + "/" + anImgPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static String ReplaceSpacialCharacters(String string) {
        return string.replace(" ", "").replace("&", "").replace("-", "").
                replace("'", "").replace("(","").replace(")","")
                .replace("/","");
    }
}