package com.app.user.ui.searchFromEntity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.user.R
import com.app.user.UserMainActivity
import com.app.user.adapters.EventAdapter
import com.app.user.databinding.FragmentSearchFromEntityBinding
import com.app.user.utils.NetworkResult
import com.app.user.utils.OnItemSelectedInterface
import com.app.user.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SearchFromEntityFragment : Fragment(R.layout.fragment_search_from_entity),
    OnItemSelectedInterface {
    private val args: SearchFromEntityFragmentArgs by navArgs()
    private lateinit var binding: FragmentSearchFromEntityBinding
    private val viewModel: SearchFromEntityViewModel by viewModels()
    private lateinit var stadiumAdapter: EventAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentSearchFromEntityBinding.bind(view)
        initUI(binding)
    }

    private fun initUI(binding: FragmentSearchFromEntityBinding) {
        //get attributes from args
        val idStadium = args.idStadium
        val nameStadium = args.nameStadium
        val priceStadium = args.stadiumPrice
        Log.d("idStadium", priceStadium)

        GlobalScope.launch(Dispatchers.IO) {
            // init seance data
            viewModel.getSeancesList(idStadium)
        }

        // Setup our recycler
        binding.EventList.apply {
            stadiumAdapter = EventAdapter(context, this@SearchFromEntityFragment)
            adapter = stadiumAdapter
            layoutManager =
                LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        viewModel.seances.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            stadiumAdapter.setData(it, nameStadium, priceStadium)
        })
        // Error Handling get Seances
        viewModel.liveDataFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Seances")
                }
                is NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Seances")
                    Toast.makeText(
                        requireContext(), R.string.something_goes_wrong_s, Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("Seances")
                }
            }
        })

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }


    override fun onItemClick(position: String?) {
        val args = position.toString()
        val action =
            SearchFromEntityFragmentDirections.actionSearchFromEntityFragmentToEventFragment(
                args
            )
        UserMainActivity.navController.navigate(action)
    }


}