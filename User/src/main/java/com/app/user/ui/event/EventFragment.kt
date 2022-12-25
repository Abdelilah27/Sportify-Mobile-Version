package com.app.user.ui.event

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.user.R
import com.app.user.adapters.PlayersAdapter
import com.app.user.databinding.FragmentEventBinding
import com.app.user.model.Entity
import com.app.user.model.Event
import com.app.user.model.Player
import com.app.user.utils.ConstUtil.GOING
import com.app.user.utils.OnItemSelectedInterface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFragment : Fragment(R.layout.fragment_event), OnItemSelectedInterface {
    private val args: EventFragmentArgs by navArgs()
    private lateinit var binding: FragmentEventBinding
    private lateinit var playerAdapter: PlayersAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentEventBinding.bind(view)
        initUI(binding)
    }

    private fun initUI(binding: FragmentEventBinding) {
        //get Id from args
        val idEvent = args.idEvent

        binding.mainButtonEventFragment.setOnClickListener {
            val action = EventFragmentDirections.actionEventFragmentToPaiementFragment()
            findNavController().navigate(action)
        }


        // Setup our recycler
        binding.playerList.apply {
            playerAdapter = PlayersAdapter(context, this@EventFragment)
            adapter = playerAdapter
            layoutManager =
                LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        // set data
        binding.apply {
            val players: ArrayList<Player> = ArrayList()
            val player1 = Player(name = "Abdelilah Ngadi")
            val player2 = Player(name = "Scat Man")
            val player3 = Player(name = "Don Abdel")
            players.add(player1)
            players.add(player2)
            players.add(player3)
            val event = Event(
                location = "36 Guild Street Marrakech, MA",
                date = "14 December, 2021",
                numberOfPlayer = "10",
                entity = Entity
                    (name = "Sareem Foot"),
                players = players
            )
            eventGoing.text = event.numberOfPlayer + GOING
            eventStadium.text = event.entity!!.name
            eventDate.text = event.date
            eventTime.text = "Tuesday, 4:00PM - 9:00PM"
            event.location = event.location

            playerAdapter.setData(players)

        }
    }

    override fun onItemClick(position: Int) {

    }
}