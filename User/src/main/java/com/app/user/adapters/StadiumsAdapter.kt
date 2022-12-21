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
import com.app.user.model.Stadium
import com.app.user.utils.OnItemSelectedInterface
import com.bumptech.glide.Glide

class StadiumsAdapter(val context: Context, private val onItemSelected: OnItemSelectedInterface) :
    RecyclerView.Adapter<StadiumsAdapter.ItemViewHolder>() {

    private var myList: ArrayList<Stadium> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Stadium>) {
        myList = data as ArrayList<Stadium>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.stadium_item_recycler, parent, false)
        return ItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        myList[position].let {
            holder.id.text = it.id.toString()
            holder.title.text = it.name.toString()
            holder.numberOfPlayer.text = it.numberOfPlayer.toString()
            holder.price.text = it.price.toString()
            holder.date.text = it.date.toString()
            val stadiumImage = it.imgFileName
            Glide.with(context)
                .load(stadiumImage)
                .error(R.drawable.entities_default)
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
                    onItemSelected.onItemClick(it.id!!)
                }
            }
        }
    }

    override fun getItemCount() = myList.size
}