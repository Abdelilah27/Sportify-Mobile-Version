package com.app.user.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.user.R
import com.app.user.databinding.FragmentUserProfileBinding
import com.app.user.utils.NetworkResult
import com.app.user.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {
    private val viewModel: UserProfileViewModel by viewModels()
    lateinit var binding: FragmentUserProfileBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentUserProfileBinding.bind(view)
        initUI(binding)
    }

    private fun initUI(binding: FragmentUserProfileBinding) {
        // init data
        viewModel.getAuthUser()
        // Observe User Data
        viewModel.liveUser.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.apply {
                binding.playerName.text = username
                binding.playerAge.text = age
                binding.playerGendre.text = gendre
            }
        })
        // To show progressBar when loading data
        viewModel.liveUserAuthFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Profile")

                }
                is NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Profile")
                    Toast.makeText(
                        requireContext(),
                        R.string.profile,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("Profile")

                }
            }
        })
    }
}