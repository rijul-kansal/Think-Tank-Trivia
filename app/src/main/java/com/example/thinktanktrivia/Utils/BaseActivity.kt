/* or Reusing Fn */
package com.example.thinktanktrivia.Utils

import android.app.Activity
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.thinktanktrivia.R

open class BaseActivity : AppCompatActivity() {
    lateinit var customDialog:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
    // Code For showing ProgressBar
    fun showProgressBar(activity: Activity, message :String)
    {
        customDialog= Dialog(activity)
        customDialog.setContentView(R.layout.custom_progressbar)
        var tv=customDialog.findViewById<TextView>(R.id.text_message)
        tv.text=message
        customDialog.show()
    }
    // Code for cancelling progrtess bar
    fun cancelProgressBar()
    {
        customDialog.cancel()
    }
    // Code for displaying Toast
    fun Toast(activity: Activity,message: String)
    {
        android.widget.Toast.makeText(activity,message, android.widget.Toast.LENGTH_LONG).show()
    }
}