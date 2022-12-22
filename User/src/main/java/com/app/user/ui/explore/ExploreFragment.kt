package com.app.user.ui.explore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.user.R
import com.app.user.UserMainActivity
import com.app.user.adapters.EntitiesAdapter
import com.app.user.adapters.NearbyEventAdapter
import com.app.user.databinding.FragmentExploreBinding
import com.app.user.model.Entity
import com.app.user.model.Event
import com.app.user.ui.bottomNavUser.BottomNavUserFragmentDirections
import com.app.user.utils.OnItemSelectedInterface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment(R.layout.fragment_explore), OnItemSelectedInterface {
    private lateinit var entitiesAdapter: EntitiesAdapter
    private lateinit var nearbyAdapter: NearbyEventAdapter
    private lateinit var binding: FragmentExploreBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentExploreBinding.bind(view)
        initUI(binding)
    }

    private fun initUI(binding: FragmentExploreBinding) {
        // Setup our recycler
        binding.recyclerPopularEntities.apply {
            entitiesAdapter = EntitiesAdapter(context, this@ExploreFragment)
            adapter = entitiesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
        // Set Static Data
        var entity = Entity(id = 1, name = "Palmarena", location = "123, Marrakech")
        var entity2 = Entity(id = 2, name = "Sareem Foot", location = "123, Marrakech")
        var entity3 = Entity(id = 3, name = "Urban 5", location = "123, Marrakech")
        var entity4 = Entity(id = 4, name = "Kick Of", location = "123, Marrakech")
        var myList: ArrayList<Entity> = ArrayList()
        myList.add(entity)
        myList.add(entity4)
        myList.add(entity2)
        myList.add(entity3)
        entitiesAdapter.setData(myList)

        // Setup our nearby recycler
        binding.recyclerNearby.apply {
            nearbyAdapter = NearbyEventAdapter(context, this@ExploreFragment)
            adapter = nearbyAdapter
            layoutManager =
                LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        // Set Static Data
        var event = Event(
            id = 1, name = "Palmarena", location = "123, Marrakech", numberOfPlayer
            = "10", price = "200", date = "10PM - 11PM"
        )

        var myList2: ArrayList<Event> = ArrayList()
        myList2.add(event)
        myList2.add(event)
        myList2.add(event)
        myList2.add(event)
        myList2.add(event)
        myList2.add(event)
        nearbyAdapter.setData(myList2)
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