package com.app.user.ui.stadiumList

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.networking.model.entity.Stadium
import com.app.user.R
import com.app.user.UserMainActivity
import com.app.user.adapters.ListStadiumAdapter
import com.app.user.databinding.FragmentStadiumListBinding
import com.app.user.utils.NetworkResult
import com.app.user.utils.OnItemSelectedInterfaceWithArguments
import com.app.user.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class StadiumListFragment : Fragment(R.layout.fragment_stadium_list),
    OnItemSelectedInterfaceWithArguments {
    private lateinit var binding: FragmentStadiumListBinding
    private val viewModel: StadiumListViewModel by viewModels()
    private lateinit var stadiumAdapter: ListStadiumAdapter
    private val args: StadiumListFragmentArgs by navArgs()

    // For Searching By Date
    private var stadiumsArray = ArrayList<Stadium>()


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
            stadiumsArray = it as ArrayList<Stadium>
            stadiumAdapter.setData(it)
        })

        binding.mainButtonStadiumListFragment.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    run {
                        try {
                            Log.d("stadiumsArray", stadiumsArray.toString())
                            val listFiltered =
                                viewModel.getAvailableStadiums(stadiumsArray, dayOfMonth)
                            Log.d("TAG", listFiltered.toString())
                            stadiumAdapter.setData(listFiltered)
                        } catch (e: Exception) {
                            Log.d("Exception", e.message.toString())
                        }

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)
            )
            datePicker.show()
        }


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

    override fun onItemClick(id: String?, price: String?, stadiumName: String?) {
        val id = id.toString()
        val price = price.toString()
        val stadiumName = stadiumName.toString()
        val action =
            StadiumListFragmentDirections.actionStadiumListFragmentToSearchFromEntityFragment(
                idStadium = id, stadiumPrice = price,
                nameStadium = stadiumName
            )
        UserMainActivity.navController.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stadiums.removeObservers(viewLifecycleOwner)
        viewModel.liveDataFlow.removeObservers(viewLifecycleOwner)
    }
}