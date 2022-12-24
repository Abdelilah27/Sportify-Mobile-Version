package com.app.sportify.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.entity.EntityMainActivity
import com.app.sportify.R
import com.app.sportify.databinding.FragmentLoginBinding
import com.app.user.UserMainActivity
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
            val check = true
            if (check) {
//                val intent = Intent(activity, UserMainActivity::class.java)
//                startActivity(intent)
                val action =
                    LoginFragmentDirections.actionLoginFragmentToEntityMainActivity()
                findNavController().navigate(action)
            } else {
//                val intent = Intent(activity, EntityMainActivity::class.java)
//                startActivity(intent)
                val action =
                    LoginFragmentDirections.actionLoginFragmentToUserMainActivity()
                findNavController().navigate(action)
            }

        }

        loginBinding.signUpText.setOnClickListener {
            val action =
                LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            findNavController().navigate(action)
        }
    }
}