package com.app.user.ui.stadiumList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.user.R
import com.app.user.adapters.ListStadiumAdapter
import com.app.user.databinding.FragmentStadiumListBinding
import com.app.user.utils.NetworkResult
import com.app.user.utils.OnItemSelectedInterface
import com.app.user.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StadiumListFragment : Fragment(R.layout.fragment_stadium_list), OnItemSelectedInterface {
    private lateinit var binding: FragmentStadiumListBinding
    private val viewModel: StadiumListViewModel by viewModels()
    private lateinit var stadiumAdapter: ListStadiumAdapter
    private val args: StadiumListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentStadiumListBinding.bind(view)
        initUI(binding)
    }

    private fun initUI(binding: FragmentStadiumListBinding) {
        // GetStadiumList
        lifecycleScope.launch {
            //get Id from args
            val entityName = args.entityName
            viewModel.getStadiumList(entityName)
        }

        binding.recyclerStadiumList.apply {
            stadiumAdapter = ListStadiumAdapter(context, this@StadiumListFragment)
            adapter = stadiumAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        // Set data to adapter
        viewModel.stadiums.observe(viewLifecycleOwner, Observer {
            stadiumAdapter.setData(it)
        })


        // Error Handling get Data
        viewModel.liveDataFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Stadium List")
                }
                is NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Stadium List")
                    Toast.makeText(
                        requireContext(), R.string.something_goes_wrong_s, Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("Stadium List")
                }
            }
        })

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onItemClick(position: String?) {

    }
}