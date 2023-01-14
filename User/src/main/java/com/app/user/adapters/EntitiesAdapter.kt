package com.app.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.networking.utils.ConstUtil
import com.app.user.R
import com.app.user.model.Entity
import com.app.user.utils.OnItemSelectedInterface
import com.bumptech.glide.Glide

class EntitiesAdapter(val context: Context, private val onItemSelected: OnItemSelectedInterface) :
    RecyclerView.Adapter<EntitiesAdapter.ItemViewHolder>() {

    private var myList: ArrayList<Entity> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Entity>) {
        myList = data as ArrayList<Entity>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.entities_item_recycler, parent, false)
        return ItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        myList[position].let {
            holder.id.text = it.id.toString()
            holder.name.text = it.name
            holder.location.text = it.location
            val stadiumImage = ConstUtil.GETSTADIUMIMAGE + it.imgFileName
            Glide.with(context)
                .load(stadiumImage)
                .error(R.drawable.entity_default)
                .centerCrop()
                .into(holder.image)
        }
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        var id: TextView = view.findViewById(R.id.entity_id)
        var name: TextView = view.findViewById(R.id.entity_name)
        var location: TextView = view.findViewById(R.id.entity_location)

        var image: ImageView = view.findViewById(R.id.image)
        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                myList[position].let {
                    onItemSelected.onItemClick(it.name)
                }
            }
        }
    }


    override fun getItemCount() = myList.size
}