package com.example.thinktanktrivia.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thinktanktrivia.Adapter.ResultShownAdapter
import com.example.thinktanktrivia.FireBase.FireStoreClass
import com.example.thinktanktrivia.Model.Res
import com.example.thinktanktrivia.Model.User
import com.example.thinktanktrivia.R
import com.example.thinktanktrivia.Utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // setting toolbar and navigation drawer
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        FireStoreClass().RetrieveDataFromFireBase(this)

        var letsplay=findViewById<TextView>(R.id.letsPlay)
        letsplay.setOnClickListener {
            startActivity(Intent(this,QuestionTypeChooserActivity::class.java))
        }
        FireStoreClass().RetrieveGameDataFromFireBase(this@MainActivity)

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_update -> {
                var intent =Intent(this,UpdateProfileActivity::class.java)
              startActivityForResult(intent,Constants.UPDATE_PROFILE_CODE)
            }
            R.id.nav_logout -> {
               FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,IntroActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    // update profile
    fun updateUserProfileInNavHeader(user :User)
    {

        val hView: View = navView.getHeaderView(0)
        val text=hView.findViewById<TextView>(com.example.thinktanktrivia.R.id.updateName)
        text.setText(user.name)

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.baseline_person_24)
            .into(hView.findViewById(R.id.profile_image))
    }
    // if updated from update activity then update here also
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.UPDATE_PROFILE_CODE) {
                FireStoreClass().RetrieveDataFromFireBase(this)
            }
        }
    }

    fun RetrieveGameData(user:ArrayList<Res>)
    {
        setUpRecycleView(user)
    }
    private fun setUpRecycleView(items:ArrayList<Res>) {
        var rv = findViewById<RecyclerView>(R.id.recycleView)
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        val ItemAdapter = ResultShownAdapter(items)
        rv.adapter = ItemAdapter
    }
}