package com.app.entity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.entity.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntityMainActivity : PIBaseActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var navController: NavController
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.entity_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }
}