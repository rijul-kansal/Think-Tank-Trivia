package com.example.thinktanktrivia.FireBase

import android.util.Log
import com.example.thinktanktrivia.Model.User
import com.example.thinktanktrivia.Utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FireStoreClass {

    val db = Firebase.firestore
    fun AddUserToFireBase(user: User)
    {
        db.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("Main","Sucessfull Added To FireStore")
            }
            .addOnFailureListener{
                Log.d("Main","Failed to add on fireStore")
            }
    }

    fun getCurrentUserId():String
    {
        val mAuth=FirebaseAuth.getInstance()
        var userid=""
         val user=mAuth.currentUser
        if(user!=null)
        {
            userid=user.uid
        }
        return userid
    }
}