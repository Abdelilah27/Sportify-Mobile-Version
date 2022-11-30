package com.app.sportify

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.navigation.NavGraph
import com.app.navigation.Navigations
import com.app.sportify.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : PIBaseActivity(), Navigations {
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

    override fun navigate(graph: NavGraph, args: String) {
        when (graph) {
            NavGraph.ENTITYBOTTOMNAV -> navController.navigate(R.id.action_loginFragment_to_bottomNavFragment2)
            NavGraph.ENTITYADDNEWSTADIUM -> navController.navigate(com.app.entity.R.id.addStadiumFragment)
            NavGraph.ENTITTYSTADIUMS -> navController.navigate(com.app.entity.R.id.stadiumsFragment)
            NavGraph.SHOWPROGRESSBAR -> showProgressDialog(args)
            NavGraph.DISMISSPROGRESSBAR -> dismissProgressDialog(args)
        }
    }

    override fun popBackStack() {
        navController.popBackStack()
    }
}