package com.app.entity.ui.stadiums

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.entity.EntityMainActivity
import com.app.entity.R
import com.app.entity.adapters.StadiumsAdapter
import com.app.entity.databinding.FragmentStadiumsBinding
import com.app.entity.utils.NetworkResult
import com.app.entity.utils.OnItemSelectedInterface
import com.app.entity.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StadiumsFragment : Fragment(R.layout.fragment_stadiums), OnItemSelectedInterface {
    private val viewModel: StadiumsViewModel by viewModels()
    private lateinit var stadiumAdapter: StadiumsAdapter
    private lateinit var binding: FragmentStadiumsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentStadiumsBinding.bind(view)
        initUI(binding)
    }

    private fun initUI(stadiumBinding: FragmentStadiumsBinding) {
        Log.d("QTADIUM", "initUI: ")
        stadiumBinding.addNewStadiumButton.setOnClickListener {
            EntityMainActivity.navController.navigate(R.id.addStadiumFragment)
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
                    (activity as PIBaseActivity).dismissProgressDialog("Stadiums")
                    //Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Stadiums")
                    //Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("Stadiums")
                }
            }
        })

    }

    override fun onItemClick(position: Int) {
        Log.d("TAG", "onItemClick: ")
    }
}
