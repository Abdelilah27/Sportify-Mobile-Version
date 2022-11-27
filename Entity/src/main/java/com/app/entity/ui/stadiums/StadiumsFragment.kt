package com.app.entity.ui.stadiums

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.entity.R
import com.app.entity.databinding.FragmentStadiumsBinding
import com.app.navigation.NavGraph
import com.app.navigation.Navigations
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StadiumsFragment : Fragment(R.layout.fragment_stadiums) {
    private lateinit var navController: Navigations
    override fun onAttach(context: Context) {
        super.onAttach(context)
        navController = requireActivity() as Navigations
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val stadiumBinding = FragmentStadiumsBinding.inflate(inflater, container, false)
        initUI(stadiumBinding)
        return stadiumBinding.root
    }

    private fun initUI(stadiumBinding: FragmentStadiumsBinding) {
        stadiumBinding.addNewStadiumButton.setOnClickListener {
            navController.navigate(NavGraph.ENTITYADDNEWSTADIUM)
        }
    }
}
