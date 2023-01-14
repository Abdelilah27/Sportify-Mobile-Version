package com.app.user.ui.explore

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.networking.model.reservation.orderNomCompletResponse.OrderNonCompletResponseItem
import com.app.user.R
import com.app.user.UserMainActivity
import com.app.user.adapters.EntitiesAdapter
import com.app.user.adapters.NearbyEventAdapter
import com.app.user.databinding.FragmentExploreBinding
import com.app.user.model.Event
import com.app.user.ui.bottomNavUser.BottomNavUserFragmentDirections
import com.app.user.utils.NetworkResult
import com.app.user.utils.OnItemSelectedInterface
import com.app.user.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ExploreFragment : Fragment(R.layout.fragment_explore), OnItemSelectedInterface {
    private lateinit var entitiesAdapter: EntitiesAdapter
    private lateinit var nearbyAdapter: NearbyEventAdapter
    private lateinit var binding: FragmentExploreBinding
    private val viewModel: ExploreViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentExploreBinding.bind(view)
        initUI(binding)
    }

    private fun initUI(binding: FragmentExploreBinding) {
        GlobalScope.launch(Dispatchers.IO) {
            // get Entities
            viewModel.getEntitiesList()
            // init user data
            viewModel.getAuthUser()
        }
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.getLocation(requireActivity())
            delay(1000)
            neabyStaticData()
        }


        viewModel.currentLocation.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                binding.profileLocation.text = it
            }
        })

        // Observe User Data To Display Name
        viewModel.liveUser.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.apply {
                binding.profileName.text = username
            }
        })

        // Setup our recycler
        binding.recyclerPopularEntities.apply {
            entitiesAdapter = EntitiesAdapter(context, this@ExploreFragment)
            adapter = entitiesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
        // Set data to adapter
        viewModel.entities.observe(viewLifecycleOwner, Observer {
            binding.poplarTitle.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            entitiesAdapter.setData(it)
        })

        // Setup our nearby recycler
        binding.recyclerNearby.apply {
            nearbyAdapter = NearbyEventAdapter(context)
            adapter = nearbyAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        // Error Handling get EntitiesList
        viewModel.liveDataFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Entities")
                }
                is NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Entities")
                    Toast.makeText(
                        requireContext(), R.string.something_goes_wrong_s, Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("Entities")
                }
            }
        })
    }

    private fun neabyStaticData() {
        val data= ArrayList<OrderNonCompletResponseItem>()
        data.add(OrderNonCompletResponseItem(
            name = "Cobox",
            location = "marrakech",
            disponibility_from = "2022-01-23T18:30:00",
            disponibility_to = "2022-01-23T19:30:00"
        ))
        data.add(OrderNonCompletResponseItem(
            name = "R1",
            location = "marrakech",
            disponibility_from = "2022-01-24T10:30:00",
            disponibility_to = "2022-01-24T11:30:00"
        ))
        data.add(OrderNonCompletResponseItem(
            name = "Cobox",
            location = "marrakech",
            disponibility_from = "2022-01-24T18:00:00",
            disponibility_to = "2022-01-24T19:00:00"
        ))
        binding.nearbyTitle.visibility = View.VISIBLE
        nearbyAdapter.setData(data)
    }


    override fun onItemClick(position: String?) {
        val args = position.toString()
        val action =
            BottomNavUserFragmentDirections.actionBottomNavUserFragmentToStadiumListFragment(
                args
            )
        UserMainActivity.navController.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.entities.removeObservers(viewLifecycleOwner)
        viewModel.currentLocation.removeObservers(viewLifecycleOwner)
        viewModel.liveUser.removeObservers(viewLifecycleOwner)
        viewModel.liveDataFlow.removeObservers(viewLifecycleOwner)
        viewModel.orders.removeObservers(viewLifecycleOwner)
    }

}