package com.app.hackathon.presentation.widget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hackathon.domain.entity.SearchHistoryEntity
import com.app.hackathon.presentation.databinding.ItemSearchHistoryBinding

class HistoryAdapter(
    private val searchHistoryList: MutableList<SearchHistoryEntity>,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: ItemSearchHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchHistoryEntity) {
            with(binding) {
                model = item
                root.setOnClickListener { onClickListener.onClickItem(item) }
                deleteBtn.setOnClickListener { onClickListener.onDelete(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(searchHistoryList[position])
    }

    override fun getItemCount(): Int {
        return searchHistoryList.size
    }

    fun updateData(newSearchHistoryList: List<SearchHistoryEntity>) {
        searchHistoryList.clear()
        searchHistoryList.addAll(newSearchHistoryList)
        notifyDataSetChanged()
    }

    fun removeData(removedData: SearchHistoryEntity) {
        searchHistoryList.remove(removedData)
        notifyDataSetChanged()
    }


    interface OnClickListener {
        fun onClickItem(item: SearchHistoryEntity)
        fun onDelete(item: SearchHistoryEntity)
    }
}