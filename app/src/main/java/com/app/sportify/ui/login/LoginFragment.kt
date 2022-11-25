package com.app.sportify.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.sportify.R
import com.app.sportify.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        initUI(loginBinding)
        return loginBinding.root
    }

    private fun initUI(loginBinding: FragmentLoginBinding) {
        loginBinding.mainButtonLoginFragment.setOnClickListener {

        }

        loginBinding.signUpText.setOnClickListener {
            val action =
                LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            findNavController().navigate(action)
        }
    }

}