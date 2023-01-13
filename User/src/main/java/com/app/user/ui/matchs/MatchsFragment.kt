package com.app.user.ui.matchs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.networking.model.entity.Stadium
import com.app.user.R
import com.app.user.adapters.MatchsAdapter
import com.app.user.databinding.FragmentMatchsBinding
import com.app.user.utils.NetworkResult
import com.app.user.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MatchsFragment : Fragment(R.layout.fragment_matchs) {
    private lateinit var binding: FragmentMatchsBinding
    private val viewModel: MatchsViewModel by viewModels()
    private lateinit var matchsAdapter: MatchsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMatchsBinding.bind(view)
        initUI(binding)
    }

    private fun initUI(binding: FragmentMatchsBinding) {
        // Setup our recycler
        binding.reservationList.apply {
            matchsAdapter = MatchsAdapter(context)
            adapter = matchsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        GlobalScope.launch {
            // init reservations data
            viewModel.getReservations()
        }

        viewModel.stadiumsData.observe(viewLifecycleOwner, Observer {
            val stadiumList = ArrayList<Stadium>()
            stadiumList.add(it)
            matchsAdapter.setData(stadiumList)
        })

        viewModel.liveDataFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("ReservationsFlow")
                }
                is NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("ReservationsFlow")
                    Toast.makeText(
                        requireContext(), R.string.something_goes_wrong_s, Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("ReservationsFlow")
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stadiumsData.removeObservers(viewLifecycleOwner)
        viewModel.liveDataFlow.removeObservers(viewLifecycleOwner)
    }
}