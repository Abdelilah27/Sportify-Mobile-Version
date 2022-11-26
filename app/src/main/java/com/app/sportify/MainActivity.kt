package com.app.sportify

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.navigation.NavGraph
import com.app.navigation.Navigations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigations {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var navController: NavController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun navigate(graph: NavGraph, args: Bundle?) {
        when (graph) {
            NavGraph.ENTITY -> navController.navigate(R.id.action_loginFragment_to_bottomNavFragment2)
        }
    }

    override fun popBackStack() {
        navController.popBackStack()
    }
}