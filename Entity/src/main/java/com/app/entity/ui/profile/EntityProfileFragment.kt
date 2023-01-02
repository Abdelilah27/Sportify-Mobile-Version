package com.app.entity.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.entity.R
import com.app.entity.databinding.FragmentEntityProfileBinding
import com.app.entity.utils.NetworkResult
import com.app.entity.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntityProfileFragment : Fragment(R.layout.fragment_entity_profile) {
    private val viewModel: EntityProfileViewModel by viewModels()
    lateinit var binding: FragmentEntityProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentEntityProfileBinding.bind(view)
        initUI(binding)
    }

    private fun initUI(binding: FragmentEntityProfileBinding) {
        // init data
        viewModel.getAuthUser()
        // Observe User Data
        viewModel.liveUser.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.apply {
                binding.entityNameProfile.text = username
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

        // To show progressBar when log out
        viewModel.liveUserLogOutFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("LogOut")
                    // TODO
                }
                is NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("LogOut")
                    Toast.makeText(
                        requireContext(),
                        R.string.something_goes_wrong_s,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("LogOut")
                }
            }
        })


        // when log out button is pressed
        binding.logOut.setOnClickListener {
            viewModel.logOut()
        }

    }
}