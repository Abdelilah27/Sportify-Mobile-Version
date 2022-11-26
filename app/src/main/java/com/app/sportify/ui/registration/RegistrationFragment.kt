package com.app.sportify.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.sportify.R
import com.app.sportify.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val registrationBinding = FragmentRegistrationBinding.inflate(inflater, container, false)
        initUI(registrationBinding)
        return registrationBinding.root
    }

    private fun initUI(registrationBinding: FragmentRegistrationBinding) {
        registrationBinding.mainButtonRegistrationFragment.setOnClickListener {
            val action =
                RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        val role = listOf("player", "entity")
        val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, role)
        registrationBinding.spinnerEntityOr.setAdapter(adapter)

        registrationBinding.spinnerEntityOr.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                if (item == "entity") {
                    registrationBinding.nameEntityLayoutRegistrationFragment.visibility =
                        View.VISIBLE
                }
            }

        val gendre = listOf("men", "women")
        val adapterGendre = ArrayAdapter(requireContext(), R.layout.drop_down_item, gendre)
        registrationBinding.spinnerGendre.setAdapter(adapterGendre)


    }

}