package com.example.smartindiahackathon.localData

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.smartindiahackathon.model.UserDataModel


object UserManager {


    fun saveQuestionaries(userProfile: UserDataModel?) {
        PrefsManager.get()
            .save(PrefsManager.PREF_USER_DATA, userProfile ?: UserDataModel())
        try {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            if (uid!=null){
                storeFireBaseData(userProfile!!)
            }
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }


    }

    fun deleteQuestionaries() {
        PrefsManager.get()
            .removeAll()
    }
    fun getQuestionariesData(): UserDataModel? {
        return PrefsManager.get()
            .getObject(PrefsManager.PREF_USER_DATA, UserDataModel::class.java)
    }

    fun  storeFireBaseData(model: UserDataModel){
        try {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            var mDatabase: DatabaseReference? = null
            mDatabase = FirebaseDatabase.getInstance().reference
            mDatabase.child("userdata").child("users").child(uid).setValue(model)
        }catch (e:Exception){
            e.printStackTrace()

        }


    }
}