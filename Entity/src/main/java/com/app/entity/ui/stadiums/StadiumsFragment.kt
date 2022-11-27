package com.app.entity.ui.stadiums

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.entity.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StadiumsFragment : Fragment(R.layout.fragment_stadiums) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("StadiumsFragment", findNavController().currentDestination.toString())
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}