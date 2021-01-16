package com.pasaoglu.basemvvmapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pasaoglu.basemvvmapp.data.model.CardListResponseModel
import com.pasaoglu.basemvvmapp.databinding.CardItemLayoutBinding


class CardListAdapter(
    private val cardItemResponseList: ArrayList<CardListResponseModel>,
    private val listener: (cardResponse: CardListResponseModel, imageView: ImageView) -> Unit
) :
    ListAdapter<CardListResponseModel, RecyclerView.ViewHolder>(PokeDiffCallback()) {

    private class DataViewHolder(
        private val binding: CardItemLayoutBinding,
        var listener: (cardResponse: CardListResponseModel, imageView: ImageView) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener.invoke(binding.pokeItemModel!!, binding.posterImageView)
            }
        }

        fun bind(response: CardListResponseModel) {
            binding.apply {
                pokeItemModel = response
                executePendingBindings()
            }
        }
    }


    override fun getItemCount(): Int = cardItemResponseList.size


    fun addCards(cardItemResponseList: List<CardListResponseModel>) {
        this.cardItemResponseList.apply {
            clear()
            addAll(cardItemResponseList)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CardItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding, listener)
    }




    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as DataViewHolder
        itemViewHolder.bind(cardItemResponseList[position])
    }
}

private class PokeDiffCallback : DiffUtil.ItemCallback<CardListResponseModel>() {
    override fun areItemsTheSame(old: CardListResponseModel, aNew: CardListResponseModel): Boolean {
        return old.id == aNew.id
    }

    override fun areContentsTheSame(old: CardListResponseModel, newResponse: CardListResponseModel): Boolean {
        return old == newResponse
    }
}