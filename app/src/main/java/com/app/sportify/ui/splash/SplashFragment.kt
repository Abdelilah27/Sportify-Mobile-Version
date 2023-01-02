package com.app.sportify.ui.splash

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.sportify.R
import com.app.sportify.utils.ConstUtil.ROLES
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel.isLogged()
        // To show progressBar when loading data
        viewModel.isLogged.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("it", it.toString())
            val destination = when (it) {
                //TODO
                "noLogged" -> SplashFragmentDirections.actionSplashFragmentToWelcomeFragment()
                ROLES[0] -> SplashFragmentDirections.actionSplashFragmentToUserMainActivity()
                ROLES[1] -> SplashFragmentDirections.actionSplashFragmentToEntityMainActivity()
                else -> SplashFragmentDirections.actionSplashFragmentToWelcomeFragment()
            }
            findNavController().navigate(destination)
        })

    }
}