package com.app.user.ui.event

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.app.user.R
import com.app.user.databinding.FragmentEventBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFragment : Fragment(R.layout.fragment_event) {
    private val args: EventFragmentArgs by navArgs()
    private lateinit var binding: FragmentEventBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentEventBinding.bind(view)
        initUI(binding)
    }

    private fun initUI(binding: FragmentEventBinding) {
        //get Id from args
        val idEvent = args.idEvent
    }
}