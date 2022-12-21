package com.app.user.ui.bottomNavUser

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.app.user.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BottomNavUserFragment : Fragment(R.layout.fragment_bottom_nav_user) {
    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Bottom Navigation Implementation
        setHasOptionsMenu(true)
        val navHost =
            childFragmentManager.findFragmentById(R.id.nav_host_frame_layout_user) as
                    NavHostFragment
        navController = navHost.navController

        view.findViewById<BottomNavigationView>(R.id.BottomNavigationView)
            .setupWithNavController(navController)
    }

}