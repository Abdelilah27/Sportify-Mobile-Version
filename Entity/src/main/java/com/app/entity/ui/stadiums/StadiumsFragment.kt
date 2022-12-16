package com.app.entity.ui.stadiums

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.entity.EntityMainActivity
import com.app.entity.R
import com.app.entity.adapters.StadiumsAdapter
import com.app.entity.databinding.FragmentStadiumsBinding
import com.app.entity.model.Stadium
import com.app.entity.utils.NetworkResult
import com.app.entity.utils.OnItemSelectedInterface
import com.app.entity.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StadiumsFragment : Fragment(R.layout.fragment_stadiums), OnItemSelectedInterface {
    private val viewModel: StadiumsViewModel by viewModels()
    private lateinit var stadiumAdapter: StadiumsAdapter
    private lateinit var binding: FragmentStadiumsBinding
    private val list = ArrayList<Stadium>() // To store the data sent by the remote

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentStadiumsBinding.bind(view)
        initUI(binding)
    }

    private fun initUI(stadiumBinding: FragmentStadiumsBinding) {
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
                it.body()?.forEach {
                    list.add(it)
                }
                stadiumAdapter.setData(list)
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

        viewModel.liveStadiumsFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Delete Stadium")
                    //Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Delete Stadium")
                    //Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("Delete Stadium")
                }
            }
        })

        // Swipe for delete
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                v: RecyclerView,
                h: RecyclerView.ViewHolder,
                t: RecyclerView.ViewHolder
            ) = false

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(h: RecyclerView.ViewHolder, dir: Int) {
                val id = h.itemView.findViewById<TextView>(R.id.id)
                try {
                    val position = id.text.toString()
                    viewModel.deleteStadium(position)
                    // Notify adapter
                    list.forEach {
                        if (position.toInt() == it.id) {
                            list.removeAt(list.indexOf(it))
                            stadiumAdapter.notifyItemRemoved(position.toInt())
                            stadiumAdapter.notifyItemRangeChanged(position.toInt(), list.size)
                            stadiumAdapter.notifyDataSetChanged()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

        }).attachToRecyclerView(binding.stadiumsList)
    }

    override fun onItemClick(position: Int) {
        Log.d("TAG", "onItemClick: $position")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.liveDataFlow.removeObservers(viewLifecycleOwner)
        viewModel.liveStadiumsFlow.removeObservers(viewLifecycleOwner)
        viewModel.stadiums.removeObservers(viewLifecycleOwner)
        list.removeAll(list.toSet())
    }
}
