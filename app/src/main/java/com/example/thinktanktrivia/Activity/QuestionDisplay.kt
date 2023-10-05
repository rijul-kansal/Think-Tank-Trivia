package com.example.thinktanktrivia.Activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.thinktanktrivia.Api.ApiService
import com.example.thinktanktrivia.Model.Result
import com.example.thinktanktrivia.R
import com.example.thinktanktrivia.Utils.BaseActivity
import com.example.thinktanktrivia.Utils.Constants
import com.example.thinktanktrivia.databinding.ActivityQuestionDisplayBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class QuestionDisplay : BaseActivity(),View.OnClickListener {
    var valueCat=-1
    var valueDiff=-1
    var valueType=-1
    var valueAmt:Int=0
    var realcat:String=""
    var Category=0
    var Diff:String=""
    var type:String=""
    var Amount:Int=0
    var idx=0
    var x=0

    var totalcorr=0
    lateinit var binding:ActivityQuestionDisplayBinding
    lateinit var item:ArrayList<Result>

    var selectedOption: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityQuestionDisplayBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        item= ArrayList<Result>()
        getValues()
        getdata()
        binding.submit.setOnClickListener(this)
        binding.Option1.setOnClickListener(this)
        binding.Option2.setOnClickListener(this)
        binding.Option3.setOnClickListener(this)
        binding.Option4.setOnClickListener(this)
        binding.nextQuestion.setOnClickListener(this)

    }
    private fun setBackToOriginal()
    {
        binding.Option1.setBackgroundResource(R.drawable.sign_up_btn_bg)
        binding.Option2.setBackgroundResource(R.drawable.sign_up_btn_bg)
        binding.Option3.setBackgroundResource(R.drawable.sign_up_btn_bg)
        binding.Option4.setBackgroundResource(R.drawable.sign_up_btn_bg)
    }
    private fun populateData(ques:String,option1:String,option2:String,option3:String,option4:String)
    {
        binding.Question.text=ques
        binding.Option1.text=option1
        binding.Option2.text=option2
        binding.Option3.text=option3
        binding.Option4.text=option4
    }
    private fun getValues()
    {
        valueCat=intent.getIntExtra(com.example.thinktanktrivia.Utils.Constants.CAT,-1)
        valueAmt=intent.getIntExtra(com.example.thinktanktrivia.Utils.Constants.AMT,0)
        valueDiff=intent.getIntExtra(com.example.thinktanktrivia.Utils.Constants.Diff,-1)
        valueType=intent.getIntExtra(com.example.thinktanktrivia.Utils.Constants.Type,-1)
        Log.d("Main", "Cat:$valueCat   Amt: $valueAmt  Diff: $valueDiff  Type: $valueType")

        Category=valueCat
        when(valueDiff)
        {
            0->{Diff="easy"}
            1->{Diff="medium"}
            2->{Diff="hard"}
        }

        when(valueType)
        {
            0->{type="multiple"}
            1->{type="truefalse"}
        }

        Amount=valueAmt

    }
    private fun Question()
    {
        var arrlis:ArrayList<String> = ArrayList()
        arrlis.add(item[idx].correct_answer)
        arrlis.add(item[idx].incorrect_answers[0])
        arrlis.add(item[idx].incorrect_answers[1])
        arrlis.add(item[idx].incorrect_answers[2])

        val random = Random
        x= random.nextInt(0, 4)
        val xx=arrlis[0]
        arrlis[0]=arrlis[x]
        arrlis[x]=xx
        populateData(item[idx].question,arrlis[0],arrlis[1],arrlis[2],arrlis[3])
    }
    private fun getdata() {
        val matchApi = com.example.thinktanktrivia.Utils.Constants.getInstance().create(ApiService::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val result = matchApi.getSeries(Amount, Category, Diff, type)
                if (result.isSuccessful && result.body() != null) {
                    withContext(Dispatchers.Main) {
                        val responseData = result.body()
                        if (responseData != null) {
                            binding.Category.text=responseData.results[0].category
                            realcat=responseData.results[0].category
                        }
                        if (responseData != null) {
                            var s=responseData.results[0].difficulty
                            binding.difficulty.text= s[0].toUpperCase() +s.substring(1,s.length)
                        }
                        Log.e("rijul", responseData.toString())
                        for (i in 0 until responseData!!.results.size) {
//                            Toast.makeText(this@QuestionDisplay, responseData.results[i].correct_answer.toString(), Toast.LENGTH_LONG).show()
                            item.add(responseData.results[i])
                        }
                        Question()
                        Toast.makeText(this@QuestionDisplay, "Data loaded successfully  ${item.size}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@QuestionDisplay, "API request failed", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("rijul", "Exception: ${e.message}")
            }
        }
    }

    private fun checkForInternet(context: Context): Boolean
    {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.Option1->
            {
                setBackToOriginal()
                binding.Option1.setBackgroundResource(R.drawable.selected_option_bg)
                selectedOption = binding.Option1
            }
            R.id.Option2->
            {
                setBackToOriginal()
                binding.Option2.setBackgroundResource(R.drawable.selected_option_bg)
                selectedOption = binding.Option2
            }
            R.id.Option3->
            {
                setBackToOriginal()
                selectedOption = binding.Option3
                binding.Option3.setBackgroundResource(R.drawable.selected_option_bg)
            }
            R.id.Option4->
            {
                setBackToOriginal()
                selectedOption = binding.Option4
                binding.Option4.setBackgroundResource(R.drawable.selected_option_bg)
            }
            R.id.submit->
            {
                if (selectedOption != null && selectedOption != correctOptionView) {
                    setWrongOptionBackground(selectedOption!!)
                }
                if(selectedOption!=null && selectedOption==correctOptionView)
                {
                    totalcorr++
                }
                selectedOption=null
                when(x)
                {
                    0-> {binding.Option1.setBackgroundResource(R.drawable.correct_option_bg)}
                    1-> {binding.Option2.setBackgroundResource(R.drawable.correct_option_bg)}
                    2-> {binding.Option3.setBackgroundResource(R.drawable.correct_option_bg)}
                    3-> {binding.Option4.setBackgroundResource(R.drawable.correct_option_bg)}
                }
                binding.submit.visibility= View.GONE
                binding.nextQuestion.visibility=View.VISIBLE
            }
            R.id.nextQuestion->
            {
                setBackToOriginal()
                idx++
                if(idx<item.size) {
                    binding.toolbar.title="Question No. ${idx+1}"
                    Question()
                    binding.submit.visibility= View.VISIBLE
                    binding.nextQuestion.visibility=View.GONE
                }
                else
                {
                   var intent= Intent(this@QuestionDisplay,FinishActivity::class.java)
                    intent.putExtra(Constants.TOTAL_SCORE,totalcorr)
                    intent.putExtra(Constants.TOTALQ,Amount)
                    intent.putExtra(Constants.CAT2,realcat)
                    intent.putExtra(Constants.TYPE2,type)
                    intent.putExtra(Constants.DIFF2,Diff)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
    private val correctOptionView: View // Store the correct option view
        get() {
            return when (x) {
                0 -> binding.Option1
                1 -> binding.Option2
                2 -> binding.Option3
                3 -> binding.Option4
                else -> binding.Option1 // Default to Option1 if x is out of range
            }
        }

    private fun setWrongOptionBackground(optionView: View) {
        optionView.setBackgroundResource(R.drawable.wrong_option_bg)
    }
}