package com.example.thinktanktrivia.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import com.example.thinktanktrivia.Adapter.CategoryChooseAdapter
import com.example.thinktanktrivia.Adapter.DifficultyChooseAdapter
import com.example.thinktanktrivia.Adapter.TypeChooseAdapter
import com.example.thinktanktrivia.Model.CatogeryModel
import com.example.thinktanktrivia.Model.DifficultyLvlModel
import com.example.thinktanktrivia.Model.TypeModel
import com.example.thinktanktrivia.R
import com.example.thinktanktrivia.Utils.BaseActivity
import com.example.thinktanktrivia.Utils.Constants
import com.example.thinktanktrivia.databinding.ActivityQuestionTypeChooserBinding
import kotlin.random.Random


class QuestionTypeChooserActivity : BaseActivity() {
    lateinit var binding:ActivityQuestionTypeChooserBinding
    // For Category Model
    lateinit var List :List<CatogeryModel>
    lateinit var List2 :List<DifficultyLvlModel>
    lateinit var List3 :List<TypeModel>
    // Flag For Choose Category
    var ChooceOneCatFlag=0
    var ChooceOneDiffFlag=0
    var TypeFlag=0
    var AmountFlag=0

   // final value
    var valueCat=0
    var valueDiff=-1
    var valueType=-1
    var valueAmt:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityQuestionTypeChooserBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SetUpToolbar()
        // Adding All Types
        List=GetCategoryChoice()
        List2=GetDifficultyChoice()
        List3=GetTypeChoice()
        // Animation
        var animSlideDown: Animation =
            AnimationUtils.loadAnimation(applicationContext, com.example.thinktanktrivia.R.anim.slide_down)
        var animSlideUp = AnimationUtils.loadAnimation(
            applicationContext, com.example.thinktanktrivia.R.anim.slide_up)


        // Ceating Adapter as well as Connecting Adapter with grid view and setting item click listener on items
        val Adapter = CategoryChooseAdapter(List, this@QuestionTypeChooserActivity)
        binding.idGRV1.adapter = Adapter
        binding.idGRV1.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            valueCat=position+9
            Handler().postDelayed({ binding.ChooceOneCatCV.visibility= View.GONE},500)
        }
        val Adapter2 = DifficultyChooseAdapter(List2, this@QuestionTypeChooserActivity)
        binding.idGRV2.adapter = Adapter2
        binding.idGRV2.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            valueDiff=position
            binding.idGRV2.startAnimation(animSlideUp)
            ChooceOneDiffFlag=0
            Handler().postDelayed({ binding.ChooceOneDiffCV.visibility= View.GONE},500)
        }
        val Adapter3 = TypeChooseAdapter(List3, this@QuestionTypeChooserActivity)
        binding.idGRV3.adapter = Adapter3
        binding.idGRV3.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            valueType=position
            binding.idGRV3.startAnimation(animSlideUp)
            TypeFlag=0
            Handler().postDelayed({ binding.ChooceOneTypeCV.visibility= View.GONE},500)
        }
        // Btn for Sliding Animation
        binding.ChooceOneDiffBtn.setOnClickListener {

            if(ChooceOneDiffFlag==0)
            {
                binding.idGRV2.startAnimation(animSlideDown)
                ChooceOneDiffFlag=1
                binding.ChooceOneDiffCV.visibility= View.VISIBLE
            }
            else
            {
                binding.idGRV2.startAnimation(animSlideUp)
                ChooceOneDiffFlag=0
                Handler().postDelayed({ binding.ChooceOneDiffCV.visibility= View.GONE},500)
            }
        }
        binding.ChooceOneTypeBtn.setOnClickListener {

            if(TypeFlag==0)
            {
                binding.idGRV3.startAnimation(animSlideDown)
                TypeFlag=1
                binding.ChooceOneTypeCV.visibility= View.VISIBLE
            }
            else
            {
                binding.idGRV3.startAnimation(animSlideUp)
                TypeFlag=0
                Handler().postDelayed({ binding.ChooceOneTypeCV.visibility= View.GONE},500)
            }
        }
        binding.ChooceOneCatBtn.setOnClickListener {

            if(ChooceOneCatFlag==0)
            {
                binding.idGRV1.startAnimation(animSlideDown)
                ChooceOneCatFlag=1
                binding.ChooceOneCatCV.visibility= View.VISIBLE
            }
            else
            {
                binding.idGRV1.startAnimation(animSlideUp)
                ChooceOneCatFlag=0
                Handler().postDelayed({ binding.ChooceOneCatCV.visibility= View.GONE},500)
            }
        }



        binding.letsPlay.setOnClickListener {
            valueAmt=binding.etAmount.text.toString().toInt()
            Log.d("Main ","AMTT $valueAmt")
            if(valueCat==0)
            {
                Toast(this@QuestionTypeChooserActivity,"Topic will be Randomly Choose")
                val random = Random.Default
                valueCat= random.nextInt(9, 32)
            }
            if(valueType==-1)
            {
                Toast(this@QuestionTypeChooserActivity,"Type will be Randomly Choose")
                val random = Random.Default
                valueType= random.nextInt(1, 2)
            }
            if(valueDiff==-1)
            {
                Toast(this@QuestionTypeChooserActivity,"Difficulty will be Randomly Choose")
                val random = Random.Default
                valueDiff= random.nextInt(1, 3)
            }
            if(valueAmt == 0)
            {
                Toast(this@QuestionTypeChooserActivity,"No of Questions  will be Randomly Choose")
                val random = Random.Default
                valueAmt= random.nextInt(1, 50)
            }
            val intent= Intent(this,QuestionDisplay::class.java)
            Log.d("Main ","AMTT $valueAmt")
            intent.putExtra(Constants.CAT,valueCat)
            intent.putExtra(Constants.AMT,valueAmt)
            intent.putExtra(Constants.Type,valueType)
            intent.putExtra(Constants.Diff,valueDiff)
            startActivity(intent)
        }

    }

    private fun SetUpToolbar()
    {
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()?.setDisplayShowHomeEnabled(true)
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
            supportActionBar!!.title=resources.getString(R.string.Choose)
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    fun GetCategoryChoice():List<CatogeryModel> {
        var List: List<CatogeryModel> = ArrayList<CatogeryModel>()
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.GK))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Books))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Film))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Music))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Musical_Theaters))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Television))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Video_Games))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Board_Games))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Nature))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Computers))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Mathematics))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Mythlogy))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Sports))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Geography))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.History))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Polities))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Art))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Celeb))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Animals))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Vehicles))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Comics))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Gadegets))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.JapAAM))
        List=List+CatogeryModel(resources.getString(com.example.thinktanktrivia.R.string.Cartton))
        return List
    }
    fun GetDifficultyChoice():List<DifficultyLvlModel> {
        var List: List<DifficultyLvlModel> = ArrayList()
        List=List+DifficultyLvlModel(resources.getString(com.example.thinktanktrivia.R.string.Easy))
        List=List+DifficultyLvlModel(resources.getString(com.example.thinktanktrivia.R.string.Medium))
        List=List+DifficultyLvlModel(resources.getString(com.example.thinktanktrivia.R.string.Hard))
        return List
    }
    fun GetTypeChoice():List<TypeModel> {
        var List: List<TypeModel> = ArrayList()
        List=List+TypeModel(resources.getString(com.example.thinktanktrivia.R.string.Multiple))
        List=List+TypeModel(resources.getString(com.example.thinktanktrivia.R.string.True_False))
        return List
    }
}