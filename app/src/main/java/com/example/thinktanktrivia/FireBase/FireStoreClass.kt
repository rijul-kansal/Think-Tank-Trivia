package com.example.thinktanktrivia.FireBase

import android.app.Activity
import android.util.Log
import com.example.thinktanktrivia.Activity.FinishActivity
import com.example.thinktanktrivia.Activity.MainActivity
import com.example.thinktanktrivia.Activity.UpdateProfileActivity
import com.example.thinktanktrivia.Model.ResultsModel
import com.example.thinktanktrivia.Model.User
import com.example.thinktanktrivia.Utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FireStoreClass {
   // instance
    val db = Firebase.firestore
    // this fn will add data to firebase/ firestore in "users"
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
    fun AddGameDataToFireBase(res: ResultsModel)
    {
        db.collection(Constants.USERSDATA)
            .document(getCurrentUserId())
            .set(res, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("Main","Sucessfull Added To FireStore")
            }
            .addOnFailureListener{
                Log.d("Main","Failed to add on fireStore")
            }
    }
    // getting user data from firestore
    fun RetrieveDataFromFireBase(activity : Activity)
    {
        db.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener {
                task->
                val user : User? =task.toObject<User>(User::class.java)
                Log.d("Main","data ${user.toString()}")
                when(activity)
                {
                    is UpdateProfileActivity->
                    {
                        if (user != null) {
                            activity.PopulatingData(user)
                        }
                    }
                    is MainActivity->
                    {
                        if (user != null) {
                            activity.updateUserProfileInNavHeader(user)
                        }
                    }
                    is FinishActivity->
                    {
                        if (user != null) {
                            activity.populateUserName(user)
                        }
                    }
                }
            }
            .addOnFailureListener{
                Log.d("Main","Failed to add on fireStore")
            }
    }
    fun RetrieveGameDataFromFireBase(activity : Activity)
    {
        try {
            db.collection(Constants.USERSDATA)
                .document(getCurrentUserId())
                .get()
                .addOnSuccessListener {task->
                    try {
                    Log.d("Main","Success to Retrieve on fireStore ${task.toObject(ResultsModel::class.java)?.res}")
                    }catch (e:Exception)
                    {
                        Log.d("Main",e.message.toString())
                    }
                    val resultsModel = task.toObject(ResultsModel::class.java)
                    Log.d("Main","1")
                    if (resultsModel != null) {
                        val dataRes = resultsModel.res
//                        Log.d("Main", "Success to Retrieve on Firestore ${resultsModel.toString()}")

                        when (activity) {
                            is FinishActivity -> {
                                activity.RetrieveGameData(dataRes)
                            }
                        }
                    }
                    else {
                        Log.d("Main", "Data is null")
                        when (activity) {
                            is FinishActivity -> {
                                activity.AddDataFirstTimeToFireStore()
                            }
                        }
                    }
                }
                .addOnFailureListener{
                    Log.d("Main","Failed to Retrieve on fireStore")
                }
        }catch (e:Exception)
        {
            Log.d("Main",e.message.toString())
        }

    }
    // update data back to firestroe for users with their id
    fun UpdateDataToFireBase(activity:Activity,mhm:HashMap<String,Any>)
    {

        db.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(mhm, SetOptions.merge())
            .addOnSuccessListener {
               when(activity)
               {
                   is UpdateProfileActivity->
                   {
                       activity.cancelAfterUplording()
                   }
               }
            }
            .addOnFailureListener{
                Log.d("Main","Failed to add on fireStore")
            }
    }
    // getting di for curently login user
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