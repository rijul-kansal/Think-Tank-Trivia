package com.example.thinktanktrivia.Activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.thinktanktrivia.FireBase.FireStoreClass
import com.example.thinktanktrivia.Model.User
import com.example.thinktanktrivia.R
import com.example.thinktanktrivia.Utils.BaseActivity
import com.example.thinktanktrivia.Utils.Constants
import com.example.thinktanktrivia.databinding.ActivityUpdateProfileBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class UpdateProfileActivity : BaseActivity() {
    lateinit var binding:ActivityUpdateProfileBinding
    // sending req for first time
    var flag=0
    // uri that we got from gallery
    var mImgUri: Uri?=null
    // uri in form of string that we gewt from firebase storage
    var mProfileImage: String?=null
    // storage instaance
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
   // user model class instance
    lateinit var mUser:User
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityUpdateProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SetUpToolbar()
        binding.profileImage.setOnClickListener {
          askingForPermission(Constants.GALLERY)
        }

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.getReference()

        binding.SignUpBtn.setOnClickListener {
            // udate data on main activity also
            setResult(RESULT_OK)
            if(mImgUri!=null)
            uploadImage()
            else
                UpdataDataToFireStore()
        }
        FireStoreClass().RetrieveDataFromFireBase(this)
    }
    private fun SetUpToolbar()
    {
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()?.setDisplayShowHomeEnabled(true)
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
            supportActionBar!!.title=resources.getString(R.string.Sign_in)
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    // first it will check if permission is granted or not is not then show dialog
    private fun askingForPermission(code: Int) {
        if (flag == 0) {
            flag = 1
            requestPermission()
        } else {
            if (checkPersmission()) {
                if (code == Constants.GALLERY) {
                    val pickImg =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    startActivityForResult(pickImg, Constants.GALLERY)
                }
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Please Allow the Happy Place App to Take Images from Gallery")
                builder.setPositiveButton("Go To Settings") { dialog, which ->
                    var intent = Intent(Settings.ACTION_SETTINGS)
                    startActivity(intent)
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                builder.show()

            }
        }
    }
    // check if permission is granted code
    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED)
    }
    // else req permission
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this@UpdateProfileActivity ,permissions(), 1)

    }
    val storagePermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    val storagePermissions33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
    )
    fun permissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissions33
        } else {
            storagePermissions
        }
    }
    // if successfull we get image for gallery then update it with the profile pic
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.GALLERY) {
                mImgUri = data?.data
                Glide
                    .with(this)
                    .load(mImgUri.toString())
                    .centerCrop()
                    .placeholder(R.drawable.baseline_logout_24)
                    .into(binding.profileImage)
            }
        }
    }
    // upload image to firebase storage
    private fun uploadImage()
    {
        if (mImgUri != null)
        {

            // Code for showing progressDialog while uploading
            showProgressBar(this@UpdateProfileActivity,"")

            // Defining the child of storageReference
            val ref = storageReference?.child("user_pics/" + UUID.randomUUID().toString())
            if (ref != null)
            {
                ref.putFile(mImgUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener { uri ->
                                mProfileImage = uri.toString()
                                Log.d("Main","Profile img url + ${mProfileImage}")
                                UpdataDataToFireStore()
                                cancelProgressBar()
                            }
                    }
                    .addOnFailureListener { e -> // Error, Image not uploaded
                        cancelProgressBar()
                    }
            }

        }
    }
    // when we went to update profile activity then we need previous data
    fun PopulatingData(user:User)
    {
        mUser=user
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.baseline_logout_24)
            .into(binding.profileImage)
        binding.etName.setText(user.name)
        binding.etEmail.setText(user.email)
        binding.etMobileNo.setText(user.mobileNo)
    }
    // if user changes some data then update back to fire store
    fun UpdataDataToFireStore()
    {
        showProgressBar(this@UpdateProfileActivity," ")
        var hashMap=HashMap<String,Any>()
        if(mImgUri !=null && mUser.image!=binding.profileImage.toString())
        {
            hashMap[Constants.IMAGE]=mProfileImage.toString()
        }
        if(binding.etName.text.toString()!=mUser.name)
        {
            hashMap[Constants.NAME]=binding.etName.text.toString()
        }
        if(binding.etEmail.text.toString()!=mUser.email)
        {
            hashMap[Constants.EMAIL]=binding.etEmail.text.toString()
        }
        if(binding.etMobileNo.text.toString()!=mUser.mobileNo)
        {
            hashMap[Constants.MOBILE_NO]=binding.etMobileNo.text.toString()
        }
        FireStoreClass().UpdateDataToFireBase(this@UpdateProfileActivity,hashMap)
    }

    fun cancelAfterUplording()
    {
        cancelProgressBar()
        finish()
    }
}