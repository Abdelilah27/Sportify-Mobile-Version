package com.app.user.ui.event

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.user.R
import com.app.user.UserMainActivity
import com.app.user.adapters.PlayersAdapter
import com.app.user.databinding.FragmentEventBinding
import com.app.user.model.Entity
import com.app.user.model.Event
import com.app.user.model.Player
import com.app.user.ui.searchFromEntity.SearchFromEntityFragmentDirections
import com.app.user.utils.ConstUtil.FULL
import com.app.user.utils.ConstUtil.GOING
import com.app.user.utils.FunUtil.getTimeFromDateString
import com.app.user.utils.FunUtil.reformatDate
import com.app.user.utils.NetworkResult
import com.app.user.utils.OnItemSelectedInterface
import com.app.user.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Integer.max

@AndroidEntryPoint
class EventFragment : Fragment(R.layout.fragment_event), OnItemSelectedInterface {
    private lateinit var binding: FragmentEventBinding
    private lateinit var playerAdapter: PlayersAdapter
    private val viewModel: EventViewModel by viewModels()
    private val args: EventFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentEventBinding.bind(view)
        initUI(binding)
    }

    @SuppressLint("SetTextI18n")
    private fun initUI(binding: FragmentEventBinding) {
        //get IdEvent, Seance from args
        val idSeance = args.idEvent
        val idStadium = args.idStadium

        GlobalScope.launch {
            // init seance data
            viewModel.reserveSeance(idStadium, idSeance)
        }

        // Setup our recycler
        binding.playerList.apply {
            playerAdapter = PlayersAdapter(context, this@EventFragment)
            adapter = playerAdapter
            layoutManager =
                LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.reservationResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.apply {
                binding.eventStadium.text = terrain.entity
                binding.eventLocation.text = terrain.location
                binding.eventDateVisibility.visibility = View.VISIBLE
                binding.eventLocationVisibility.visibility = View.VISIBLE
                Log.d("terrain.entity", terrain.entity)
                val going = max(11 - joueurs.size, 0)
                binding.eventGoing.text = if (going == 0) FULL else going.toString() + GOING
                binding.cardView.visibility = if (going == 0) View.GONE else View.VISIBLE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    binding.eventDate.text = reformatDate(seance.heureDebut)
                }
                binding.eventTime.text = getTimeFromDateString(seance.heureDebut)
                // set data
                try {
                    val players = joueurs.takeLast(10) as ArrayList
                    Log.d("players", players.toString())
                    playerAdapter.setData(players)
                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }

            }
        })

        // Error Handling get Seance Info
        viewModel.liveDataStadiumFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Stadium Info")
                }
                is NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("Stadium Info")
                    Toast.makeText(
                        requireContext(), R.string.something_goes_wrong_s, Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("Stadium Info")
                }
            }
        })

        binding.mainButtonEventFragment.setOnClickListener {
            val action =
                EventFragmentDirections.actionEventFragmentToPaiementFragment(idStadium)
            UserMainActivity.navController.navigate(action)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onItemClick(position: String?) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.reservationResponse.removeObservers(viewLifecycleOwner)
        viewModel.liveDataStadiumFlow.removeObservers(viewLifecycleOwner)
    }
}