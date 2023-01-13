package com.app.user.ui.payment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.user.R
import com.app.user.databinding.FragmentPaymentBinding
import com.app.user.utils.ConstUtil.MAD
import com.app.user.utils.NetworkResult
import com.app.user.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {
    private val args: PaymentFragmentArgs by navArgs()
    private lateinit var binding: FragmentPaymentBinding
    private val viewModel: PaymentViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaymentBinding.bind(view)
        initUI(binding)
    }

    @SuppressLint("SetTextI18n")
    private fun initUI(binding: FragmentPaymentBinding) {
        //get IdStadium from args
        val idStadium = args.idStadium
        GlobalScope.launch {
            // init payment data
            viewModel.getStadiumById(idStadium)
        }

        viewModel.stadiumsData.observe(viewLifecycleOwner, Observer {
            binding.apply {
                try{
                    val pricePerPerson = it.price / 10
                    totalPaymentFragment.setText(pricePerPerson.toString()+ MAD)
                }catch(e: Exception){
                    val pricePerPerson = it.price
                    totalPaymentFragment.setText(pricePerPerson.toString()+ MAD)
                }

            }
        })

        viewModel.liveDataFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Payment")
                }
                is NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Payment")
                    Toast.makeText(
                        requireContext(), R.string.something_goes_wrong_s, Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("Payment")
                }
            }
        })
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }
}
