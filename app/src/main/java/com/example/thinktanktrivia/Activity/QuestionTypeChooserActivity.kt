package com.example.thinktanktrivia.Activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thinktanktrivia.Adapter.CategoryChooseAdapter
import com.example.thinktanktrivia.Model.CatogeryModel
import com.example.thinktanktrivia.databinding.ActivityQuestionTypeChooserBinding


class QuestionTypeChooserActivity : AppCompatActivity() {
    lateinit var binding:ActivityQuestionTypeChooserBinding
    // For Category Model
    lateinit var List :List<CatogeryModel>
    // Flag For Choose Category
    var ChooceOneCatFlag=0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityQuestionTypeChooserBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Adding All Catogries
        List=GetCategoryChoice()
        // Animation
        var animSlideDown: Animation =
            AnimationUtils.loadAnimation(applicationContext, com.example.thinktanktrivia.R.anim.slide_down)
        var animSlideUp = AnimationUtils.loadAnimation(
            applicationContext, com.example.thinktanktrivia.R.anim.slide_up)
        // Ceating Adapter as well as Connecting Adapter with grid view and setting item click listener on items
        val Adapter = CategoryChooseAdapter(List, this@QuestionTypeChooserActivity)
        binding.idGRV.adapter = Adapter
        binding.idGRV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(applicationContext, List[position].name + " selected",
                Toast.LENGTH_SHORT).show()
            binding.idGRV.startAnimation(animSlideUp)
            ChooceOneCatFlag=0
            Handler().postDelayed({ binding.ChooceOneCatCV.visibility= View.GONE},500)
        }
        // Btn for Sliding Animation
        binding.ChooceOneCatBtn.setOnClickListener {

            if(ChooceOneCatFlag==0)
            {
                binding.idGRV.startAnimation(animSlideDown)
                ChooceOneCatFlag=1
                binding.ChooceOneCatCV.visibility= View.VISIBLE
            }
            else
            {
                binding.idGRV.startAnimation(animSlideUp)
                ChooceOneCatFlag=0
                Handler().postDelayed({ binding.ChooceOneCatCV.visibility= View.GONE},500)
            }
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
}