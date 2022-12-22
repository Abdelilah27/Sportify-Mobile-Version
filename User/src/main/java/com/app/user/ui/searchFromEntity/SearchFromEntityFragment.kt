package com.app.user.ui.searchFromEntity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.user.R
import com.app.user.UserMainActivity
import com.app.user.adapters.EventAdapter
import com.app.user.databinding.FragmentSearchFromEntityBinding
import com.app.user.model.Event
import com.app.user.utils.OnItemSelectedInterface
import dagger.hilt.android.AndroidEntryPoint
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
        //get Id from args
        val idEntity = args.idEntity

        // Setup our recycler
        binding.EventList.apply {
            stadiumAdapter = EventAdapter(context, this@SearchFromEntityFragment)
            adapter = stadiumAdapter
            layoutManager =
                LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        getData0()

        binding.mainButtonSearchFromFragment.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    run {
                        getData()
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)
            )
            datePicker.show()

        }
    }

    private fun getData() {
        // Set Static Data
        var s = Event(
            name = "Stadium 1", price = "100", numberOfPlayer = "10",
            date = "SAT-May 2:00 PM-3:00PM"
        )
        var s2 = Event(
            name = "Stadium 2", price = "200", numberOfPlayer = "10",
            date = "SAT-May 2:00 PM-3:00PM"
        )
        var s3 = Event(
            name = "Stadium 4", price = "100", numberOfPlayer = "10",
            date = "SAT-May 2:00 PM-3:00PM"
        )
        var s4 = Event(
            name = "Stadium 3", price = "250", numberOfPlayer = "10",
            date = "SAT-May 2:00 PM-3:00PM"
        )
        var myList: ArrayList<Event> = ArrayList()
        myList.add(s)
        myList.add(s)
        myList.add(s2)
        myList.add(s2)
        myList.add(s3)
        myList.add(s3)
        myList.add(s3)
        myList.add(s4)
        myList.add(s4)
        myList.add(s4)
        stadiumAdapter.setData(myList)
    }


    private fun getData0() {
        // Set Static Data
        var s = Event(
            name = "Stadium 1", price = "100", numberOfPlayer = "10",
            date = "SAT-May 2:00 PM-3:00PM"
        )
        var s2 = Event(
            name = "Stadium 2", price = "200", numberOfPlayer = "10",
            date = "SAT-May 2:00 PM-3:00PM"
        )
        var s3 = Event(
            name = "Stadium 4", price = "100", numberOfPlayer = "10",
            date = "SAT-May 2:00 PM-3:00PM"
        )
        var s4 = Event(
            name = "Stadium 3", price = "250", numberOfPlayer = "10",
            date = "SAT-May 2:00 PM-3:00PM"
        )
        var myList: ArrayList<Event> = ArrayList()
        myList.add(s)
        myList.add(s2)
        myList.add(s3)
        myList.add(s4)
        stadiumAdapter.setData(myList)
    }

    override fun onItemClick(position: Int) {
        val args = position.toString()
        val action =
            SearchFromEntityFragmentDirections.actionSearchFromEntityFragmentToEventFragment(
                args
            )
        UserMainActivity.navController.navigate(action)
    }
}