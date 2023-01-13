package com.app.user.ui.payment

import android.annotation.SuppressLint
import android.content.Intent
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
import com.app.user.utils.ConstUtil.ACCOUNT_ID
import com.app.user.utils.ConstUtil.MAD
import com.app.user.utils.NetworkResult
import com.app.user.utils.PIBaseActivity
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.nio.file.attribute.AclEntry.newBuilder

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {
    private val args: PaymentFragmentArgs by navArgs()
    private lateinit var binding: FragmentPaymentBinding
    private val viewModel: PaymentViewModel by viewModels()

    private var pricePerPerson = 0

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
                    pricePerPerson = it.price / 10
                    totalPaymentFragment.setText(pricePerPerson.toString()+ MAD)
                }catch(e: Exception){
                    pricePerPerson = it.price
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

        binding.mainButtonPaymentFragment.setOnClickListener {
            val description = binding.descriptionPaymentFragment.text.toString()
            // Initialize PayPal configuration
            val paypalConfig = PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(ACCOUNT_ID)
            val request = PayPalPayment(
                BigDecimal(pricePerPerson), MAD, description,
                PayPalPayment.PAYMENT_INTENT_SALE)
            val service = Intent(context, PayPalService::class.java)
            service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig)
            activity?.startService(service)
            val intent = Intent(context, PaymentActivity::class.java)
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, request)
            startActivityForResult(intent, 0)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }
}
