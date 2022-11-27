package com.app.entity.ui.bottomNav

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.entity.R
import com.app.entity.databinding.FragmentBottomNavBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavFragment : Fragment(R.layout.fragment_bottom_nav) {
    companion object {
        lateinit var navController: NavController
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBottomNavBinding.bind(view)

        Log.d("BottomNavFragment", findNavController().currentDestination.toString())

        // Bottom Navigation Implementation
        setHasOptionsMenu(true)
        val navHost = childFragmentManager.findFragmentById(R.id.nav_host_frame_layout_entity2) as
                NavHostFragment
        navController = navHost.findNavController()

        view.findViewById<BottomNavigationView>(R.id.BottomNavigationView)
            .setupWithNavController(navController)
    }
}