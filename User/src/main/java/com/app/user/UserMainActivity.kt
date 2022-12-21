package com.app.user

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserMainActivity : AppCompatActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var navController: NavController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.user_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}