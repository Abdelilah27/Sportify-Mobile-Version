package com.app.sportify.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.sportify.R
import com.app.sportify.databinding.FragmentLoginBinding
import com.app.sportify.utils.ConstUtil.ROLES
import com.app.sportify.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentLoginBinding.bind(view)
        binding.lifecycleOwner = this
        binding.loginViewModel = viewModel
        initUI(binding)
    }

    private fun initUI(loginBinding: FragmentLoginBinding) {
        loginBinding.mainButtonLoginFragment.setOnClickListener {
            viewModel. onLoginClicked()
        }

        // To show progressBar when login data
        viewModel.liveUserFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is com.app.sportify.utils.NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Login")

                }
                is com.app.sportify.utils.NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Login")
                    Toast.makeText(
                        requireContext(),
                        R.string.something_goes_wrong,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is com.app.sportify.utils.NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("Login")

                }
            }
        })

        // Track user connected
        viewModel.liveUserAuthFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is com.app.sportify.utils.NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("User")
                    // Get role
                    val userRole = it.data?.appRoles?.getOrNull(0)?.roleName ?: "null"
                    if (userRole == ROLES[1]) {
                        // Send to Entity Module
                        val action =
                            LoginFragmentDirections.actionLoginFragmentToEntityMainActivity()
                        findNavController().navigate(action)
                    } else {
                        // Send to User Module
                        val action =
                            LoginFragmentDirections.actionLoginFragmentToUserMainActivity()
                        findNavController().navigate(action)
                    }
                }
                is com.app.sportify.utils.NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("User")
                    Toast.makeText(
                        requireContext(),
                        R.string.something_goes_wrong,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is com.app.sportify.utils.NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("User")

                }
            }
        })

        loginBinding.signUpText.setOnClickListener {
            val action =
                LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.liveUserAuthFlow.removeObservers(viewLifecycleOwner)
        viewModel.liveUserFlow.removeObservers(viewLifecycleOwner)
        clearInput()
    }

    override fun onStop() {
        super.onStop()
        clearInput()
    }

    override fun onPause() {
        super.onPause()
        clearInput()
    }

    private fun clearInput() {
        binding.emailLoginFragment.setText("")
        binding.passwordLoginFragment.setText("")
    }
}