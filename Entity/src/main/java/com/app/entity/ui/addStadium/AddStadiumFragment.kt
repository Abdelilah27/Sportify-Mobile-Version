package com.app.entity.ui.addStadium

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.entity.R
import com.app.entity.databinding.FragmentAddStadiumBinding
import com.app.entity.utils.Resource
import com.app.navigation.NavGraph
import com.app.navigation.Navigations
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddStadiumFragment : Fragment(R.layout.fragment_add_stadium) {
    private val viewModel: AddStadiumViewModel by viewModels()

    private lateinit var navController: Navigations
    override fun onAttach(context: Context) {
        super.onAttach(context)
        navController = requireActivity() as Navigations
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DataBindingUtil.setContentView<FragmentAddStadiumBinding>(
            requireActivity(),
            R.layout.fragment_add_stadium
        )
        binding.lifecycleOwner = this
        binding.addStadiumViewModel = viewModel
        initUI(binding)
    }


    @SuppressLint("SetTextI18n")
    private fun initUI(addStadiumBinding: FragmentAddStadiumBinding) {
        addStadiumBinding.addNewStadium.setOnClickListener {
            val disponibilityFrom = addStadiumBinding.stadiumDisponibilityFrom.text.toString()
            val disponibilityTo = addStadiumBinding.stadiumDisponibilityTo.text.toString()
            viewModel.onRegistrationClicked(disponibilityFrom, disponibilityTo)
            // To show progressBar when saving data
            viewModel.liveAddStadiumFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when (it) {
                    Resource.Status.LOADING -> {
                        navController.navigate(NavGraph.SHOWPROGRESSBAR, "AddStadium")
                    }
                    Resource.Status.SUCCESS -> {
                        navController.navigate(NavGraph.DISMISSPROGRESSBAR)
//                        val action =
//                            RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
//                        findNavController().navigate(action)
                    }
                    else -> {
                        navController.navigate(NavGraph.DISMISSPROGRESSBAR)
                    }
                }
            })
        }

        // To show the TimePicker
        addStadiumBinding.stadiumDisponibilityFrom.setOnFocusChangeListener { _, hasFocus ->
            val currentTime = Calendar.getInstance()
            val startHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = currentTime.get(Calendar.MINUTE)
            if (hasFocus) {
                TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        addStadiumBinding.stadiumDisponibilityFrom.setText("$hourOfDay")
                    },
                    startHour,
                    startMinute,
                    false
                ).show()
            }
        }
        addStadiumBinding.stadiumDisponibilityTo.setOnFocusChangeListener { _, hasFocus ->
            val currentTime = Calendar.getInstance()
            val startHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = currentTime.get(Calendar.MINUTE)
            if (hasFocus) {
                TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        addStadiumBinding.stadiumDisponibilityTo.setText("$hourOfDay")
                    },
                    startHour,
                    startMinute,
                    false
                ).show()
            }
        }
    }
}