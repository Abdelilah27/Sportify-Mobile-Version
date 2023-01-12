package com.app.user.ui.explore

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.user.R
import com.app.user.UserMainActivity
import com.app.user.adapters.EntitiesAdapter
import com.app.user.adapters.NearbyEventAdapter
import com.app.user.databinding.FragmentExploreBinding
import com.app.user.model.Entity
import com.app.user.model.Event
import com.app.user.ui.bottomNavUser.BottomNavUserFragmentDirections
import com.app.user.utils.NetworkResult
import com.app.user.utils.OnItemSelectedInterface
import com.app.user.utils.PIBaseActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        getLocation()
        initUI(binding)
    }

    private fun getLocation() {
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            val altitude = location.altitude
            val longitude = location.longitude
            val latitude = location.latitude
            Log.d("altitude", altitude.toString())
            Log.d("longitude", longitude.toString())
            Log.d("latitude", latitude.toString())
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

    }


    private fun initUI(binding: FragmentExploreBinding) {
        GlobalScope.launch(Dispatchers.IO) {
            // init user data
            viewModel.getAuthUser()
            // get Entities
            viewModel.getEntitiesList()
        }

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
            entitiesAdapter.setData(it)
        })

        // Setup our nearby recycler
        binding.recyclerNearby.apply {
            nearbyAdapter = NearbyEventAdapter(context, this@ExploreFragment)
            adapter = nearbyAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        // Set Static Data
        var event = Event(
            id = 1,
            name = "Palmarena",
            location = "123, Marrakech",
            numberOfPlayer = "10",
            price = "200",
            date = "10PM - 11PM"
        )

        var myList2: ArrayList<Event> = ArrayList()
        myList2.add(event)
        myList2.add(event)
        myList2.add(event)
        myList2.add(event)
        myList2.add(event)
        myList2.add(event)
        nearbyAdapter.setData(myList2)


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


    override fun onItemClick(position: Int) {
        val args = position.toString()
        val action =
            BottomNavUserFragmentDirections.actionBottomNavUserFragmentToSearchFromEntityFragment(
                args
            )
        UserMainActivity.navController.navigate(action)
    }
}