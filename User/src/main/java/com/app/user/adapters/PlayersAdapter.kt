package com.app.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.user.R
import com.app.user.model.Player
import com.app.user.utils.OnItemSelectedInterface
import com.bumptech.glide.Glide

class PlayersAdapter(
    val context: Context,
    private val onItemSelected: OnItemSelectedInterface
) :
    RecyclerView.Adapter<PlayersAdapter.ItemViewHolder>() {

    private var myList: ArrayList<String> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<String>) {
        myList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.player_item_event_fragment, parent, false)
        return ItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        myList[position].let {
            holder.name.text = it
            Glide.with(context)
                .load(R.drawable.player_default)
                .error(R.drawable.player_default)
                .centerCrop()
                .into(holder.image)
        }
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        var id: TextView = view.findViewById(R.id.player_id)
        var name: TextView = view.findViewById(R.id.player_name)
        var image: ImageView = view.findViewById(R.id.player_image)
        override fun onClick(p0: View?) {
        }
    }


    override fun getItemCount() = myList.size
}