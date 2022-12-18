package com.app.entity.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.entity.R
import com.app.entity.databinding.StadiumItemStadiumsFragmentBinding
import com.app.entity.model.Stadium
import com.app.entity.utils.ConstUtil
import com.app.entity.utils.OnItemSelectedInterface
import com.bumptech.glide.Glide

class StadiumssAdapter(val context: Context, private val onItemSelected: OnItemSelectedInterface) :
    ListAdapter<Stadium, StadiumssAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: StadiumItemStadiumsFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(stadium: Stadium) {
            binding.apply {
                id.text = stadium.id.toString()
                date.text = stadium.disponibility_from + "-" + stadium.disponibility_to
                title.text = stadium.name
                location.text = stadium.location
                numberOfPlayer.text = stadium.numberOfPlayer.toString() + ConstUtil.PLAYERS
                price.text = stadium.price.toString() + ConstUtil.MAD
                description.text = stadium.description
                val stadiumImage = ConstUtil.GETSTADIUMIMAGE + stadium.imgFileName
                Glide.with(context)
                    .load(stadiumImage)
                    .error(R.drawable.stadium_default)
                    .centerCrop()
                    .into(image)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            StadiumItemStadiumsFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<Stadium>() {
        override fun areItemsTheSame(
            oldItem: Stadium,
            newItem: Stadium
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Stadium, newItem: Stadium) =
            oldItem.id == newItem.id
    }
}