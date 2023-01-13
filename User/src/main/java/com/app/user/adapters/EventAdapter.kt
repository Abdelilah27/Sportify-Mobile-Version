package com.app.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.networking.model.user.SeancesItem
import com.app.user.R
import com.app.user.utils.ConstUtil
import com.app.user.utils.FunUtil
import com.app.user.utils.FunUtil.getTimeFromDateString
import com.app.user.utils.OnItemSelectedInterface
import com.bumptech.glide.Glide

class EventAdapter(
    val context: Context,
    private val onItemSelected: OnItemSelectedInterface,
) :
    RecyclerView.Adapter<EventAdapter.ItemViewHolder>() {

    private var myList: ArrayList<SeancesItem> = ArrayList()
    private var name: String = ""
    private var price: String = ""

    @SuppressLint("NotifyDataSetChanged")
    fun setData(
        data: List<SeancesItem>, nameStadium: String,
        priceStadium: String
    ) {
        myList = data as ArrayList<SeancesItem>
        name = nameStadium
        price = priceStadium
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.event_item_recycler, parent, false)
        return ItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        myList[position].let {
            holder.id.text = it.id.toString()
            holder.date.text = getTimeFromDateString(it.heureDebut) + " | " + getTimeFromDateString(it.heureFin)
            holder.title.text = if (name.isEmpty()) "" else name
            holder.price.text = if (price.isEmpty()) "" else "$price${ConstUtil.MAD}"

            holder.numberOfPlayer.text = it.nbreParticipant.toString() + ConstUtil.PLAYERS
            Glide.with(context)
                .load(R.drawable.event_stadium_default)
                .error(R.drawable.event_stadium_default)
                .centerCrop()
                .into(holder.image)
        }
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }

        var id: TextView = view.findViewById(R.id.id)
        var title: TextView = view.findViewById(R.id.title)
        var numberOfPlayer: TextView = view.findViewById(R.id.numberOfPlayer)
        var price: TextView = view.findViewById(R.id.price)
        var date: TextView = view.findViewById(R.id.date)

        var image: ImageView = view.findViewById(R.id.image)
        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                myList[position].let {
                    onItemSelected.onItemClick(it.id.toString())
                }
            }
        }
    }

    override fun getItemCount() = myList.size
}