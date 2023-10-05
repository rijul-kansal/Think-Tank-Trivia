package com.example.thinktanktrivia.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.thinktanktrivia.FireBase.FireStoreClass
import com.example.thinktanktrivia.Model.Res
import com.example.thinktanktrivia.Model.ResultsModel
import com.example.thinktanktrivia.Model.User
import com.example.thinktanktrivia.Utils.Constants
import com.example.thinktanktrivia.databinding.ActivityFinishBinding

class FinishActivity : AppCompatActivity() {
    lateinit var binding:ActivityFinishBinding
    var corrQ:Int=0
    var TotalQ:Int=0
    lateinit var cat:String
    lateinit var diff:String
    lateinit var type:String
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityFinishBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        corrQ=intent.getIntExtra(Constants.TOTAL_SCORE,0)
        TotalQ=intent.getIntExtra(Constants.TOTALQ,0)
        cat=intent.getStringExtra(Constants.CAT2).toString()
        diff=intent.getStringExtra(Constants.DIFF2).toString()
        type=intent.getStringExtra(Constants.TYPE2).toString()


        binding.Score.text="Your Score is ${corrQ}/${TotalQ}"
        FireStoreClass().RetrieveDataFromFireBase(this@FinishActivity)

        binding.FinishBtn.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }


        FireStoreClass().RetrieveGameDataFromFireBase(this@FinishActivity)
    }
    fun populateUserName(user:User)
    {
        binding.congoandname.text="Congratulations ${user.name}"
    }
    fun RetrieveGameData(user: ArrayList<Res>)
    {
        var userid=FireStoreClass().getCurrentUserId()
        if (user.size!=0) {
            user.add(Res(cat,diff,corrQ,TotalQ,type))
            val user= ResultsModel(user,userid)
            FireStoreClass().AddGameDataToFireBase(user)
        }
    }
    fun AddDataFirstTimeToFireStore()
    {
        var userid=FireStoreClass().getCurrentUserId()
        var arrlis:ArrayList<Res> =ArrayList()
        arrlis.add(Res(cat,diff,corrQ,TotalQ,type))
        val user= ResultsModel(id=userid,res=arrlis)
        FireStoreClass().AddGameDataToFireBase(user)
    }
}