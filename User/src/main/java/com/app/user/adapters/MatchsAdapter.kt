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
import com.app.networking.model.entity.Stadium
import com.app.user.R
import com.app.user.utils.FunUtil
import com.bumptech.glide.Glide

class MatchsAdapter (
    val context: Context,
) :
    RecyclerView.Adapter<MatchsAdapter.ItemViewHolder>() {

    private var myList: ArrayList<Stadium> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Stadium>) {
        myList = data as ArrayList<Stadium>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.reservation_item_matchs_fragment, parent, false)
        return ItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        myList[position].let {
            holder.id.text = it.id.toString()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                holder.date.text =
                    FunUtil.reformatDate(it.disponibility_from.toString()) + " | " + FunUtil.reformatDate(
                        it.disponibility_to.toString()
                    )
            } else {
                holder.date.text = ""
            }
            holder.title.text = it.name
            holder.location.text = it.location

            Glide.with(context)
                .load(R.drawable.event_stadium_default)
                .error(R.drawable.event_stadium_default)
                .centerCrop()
                .into(holder.image)
        }
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var id: TextView = view.findViewById(R.id.id)
        var date: TextView = view.findViewById(
            R.id
                .date
        )
        var title: TextView = view.findViewById(
            R.id
                .title
        )
        var location: TextView = view.findViewById(
            R.id
                .location
        )
        var image: ImageView = view.findViewById(R.id.image)
    }


    override fun getItemCount() = myList.size
}