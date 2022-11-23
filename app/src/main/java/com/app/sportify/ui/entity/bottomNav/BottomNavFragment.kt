package com.app.sportify.ui.entity.bottomNav

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.sportify.R
import com.app.sportify.databinding.FragmentBottomNavBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavFragment : Fragment(R.layout.fragment_bottom_nav) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var navController: NavController
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBottomNavBinding.bind(view)
        // Bottom Navigation Implementation
        setHasOptionsMenu(true)
        val navHost = childFragmentManager.findFragmentById(R.id.nav_host_frame_layout) as
                NavHostFragment
        navController = navHost.findNavController()

        view.findViewById<BottomNavigationView>(R.id.BottomNavigationView)
            .setupWithNavController(navController)
        initView(binding)
    }

    private fun initView(binding: FragmentBottomNavBinding) {
    }
}