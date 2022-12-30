package com.app.sportify.ui.registration

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.sportify.R
import com.app.sportify.databinding.FragmentRegistrationBinding
import com.app.sportify.utils.ConstUtil.GENDRE
import com.app.sportify.utils.ConstUtil.ROLES
import com.app.sportify.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private val viewModel: RegistrationViewModel by viewModels()
    lateinit var binding: FragmentRegistrationBinding

    private var appRole: String = ""
    private var gendre: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentRegistrationBinding.bind(view)
        binding.lifecycleOwner = this
        binding.registrationViewModel = viewModel
        initUI(binding)
    }

    private fun initUI(registrationBinding: FragmentRegistrationBinding) {
        registrationBinding.mainButtonRegistrationFragment.setOnClickListener {
            val confirmedPassword =
                registrationBinding.confirmPasswordRegistrationFragment.text.toString()
            val age = registrationBinding.ageRegistrationFragment.text.toString()
            viewModel.onRegistrationClicked(confirmedPassword, appRole, age, gendre)
        }

        // Init spinnerEntityOr Adapter
        val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, ROLES)
        registrationBinding.spinnerEntityOr.setAdapter(adapter)

        // OnItemClickListener spinnerEntityOr Adapter
        registrationBinding.spinnerEntityOr.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                appRole = item
                if (item == ROLES[0]) {
                    registrationBinding.ageTextInput.visibility =
                        View.VISIBLE
                    registrationBinding.genderTextInput.visibility =
                        View.VISIBLE
                }
            }

        // Init gendre Adapter
        val adapterGendre = ArrayAdapter(requireContext(), R.layout.drop_down_item, GENDRE)
        registrationBinding.spinnerGendre.setAdapter(adapterGendre)

        // OnItemClickListener spinnerEntityOr Adapter
        registrationBinding.spinnerGendre.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                gendre = item
            }


        // To show progressBar when saving data
        viewModel.liveUserFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is com.app.sportify.utils.NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("CreateUser")
                }
                is com.app.sportify.utils.NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("CreateUser")
                    Toast.makeText(
                        requireContext(),
                        R.string.something_goes_wrong,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is com.app.sportify.utils.NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("CreateUser")

                }
            }
        })


        // To show progressBar when adding role to user
        viewModel.liveUserRoleFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is com.app.sportify.utils.NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("AddRole")
                    Toast.makeText(
                        requireContext(),
                        R.string.user_created_successfully,
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigateUp()
                }
                is com.app.sportify.utils.NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("AddRole")
                    Toast.makeText(
                        requireContext(),
                        R.string.something_goes_wrong,
                        Toast.LENGTH_LONG
                    ).show()

                }
                is com.app.sportify.utils.NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("AddRole")

                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.liveUserRoleFlow.removeObserver { viewLifecycleOwner }
        viewModel.liveUserFlow.removeObserver { viewLifecycleOwner }
    }

}