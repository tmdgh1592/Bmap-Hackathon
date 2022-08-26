package com.app.hackathon.presentation.widget.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.domain.entity.SearchHistoryEntity
import com.app.hackathon.presentation.databinding.ItemSearchHistoryBinding
import com.app.hackathon.presentation.databinding.ItemSearchLotBinding

class SearchLotAdapter(
    private val lotList: MutableList<LotEntity>,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<SearchLotAdapter.SearchLotViewHolder>() {

    inner class SearchLotViewHolder(val binding: ItemSearchLotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LotEntity) {
            with(binding) {
                model = item
                root.setOnClickListener { onClickListener.onClickItem(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchLotViewHolder {
        val binding =
            ItemSearchLotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchLotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchLotViewHolder, position: Int) {
        holder.bind(lotList[position])
    }

    override fun getItemCount(): Int {
        return lotList.size
    }

    fun updateData(newLotList: List<LotEntity>) {
        lotList.clear()
        lotList.addAll(newLotList)
        Log.d("TAG", "onSuccess2: "+lotList.size)
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onClickItem(item: LotEntity)
    }
}