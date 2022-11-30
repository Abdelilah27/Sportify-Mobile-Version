package com.app.entity.ui.stadiums

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.entity.R
import com.app.entity.adapters.StadiumsAdapter
import com.app.entity.databinding.FragmentStadiumsBinding
import com.app.entity.utils.NetworkResult
import com.app.entity.utils.OnItemSelectedInterface
import com.app.navigation.NavGraph
import com.app.navigation.Navigations
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StadiumsFragment : Fragment(R.layout.fragment_stadiums), OnItemSelectedInterface {
    private val viewModel: StadiumsViewModel by viewModels()
    private lateinit var stadiumAdapter: StadiumsAdapter


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

        // Setup our recycler
        stadiumBinding.stadiumsList.apply {
            stadiumAdapter = StadiumsAdapter(context, this@StadiumsFragment)
            adapter = stadiumAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.stadiums.observe(viewLifecycleOwner, Observer {
            try {
                stadiumAdapter.setData(it.body()!!)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        })

        // Error Handling get Data
        viewModel.liveDataFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is NetworkResult.Success -> {
                    navController.navigate(NavGraph.DISMISSPROGRESSBAR, "Stadiums")
                }
                is NetworkResult.Error -> {
                    navController.navigate(NavGraph.DISMISSPROGRESSBAR, "Stadiums")
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    Log.d("RORO2", it.message.toString())
                }
                is NetworkResult.Loading -> {
                    navController.navigate(NavGraph.SHOWPROGRESSBAR, "Stadiums")
                }
            }
        })


    }

    override fun onItemClick(position: Int) {
        Log.d("TAG", "onItemClick: ")
    }
}
