package com.app.hackathon.presentation.widget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hackathon.domain.entity.FilterOption
import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.presentation.databinding.ItemFilterOptionBinding
import com.app.hackathon.presentation.databinding.ItemSearchLotBinding

class FilterOptionAdapter(
    private val filterOptionList: MutableList<FilterOption>,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<FilterOptionAdapter.FilterOptionViewHolder>() {

    inner class FilterOptionViewHolder(val binding: ItemFilterOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FilterOption) {
            with(binding) {
                model = item
                root.setOnClickListener { onClickListener.onClickItem(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterOptionViewHolder {
        val binding =
            ItemFilterOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterOptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterOptionViewHolder, position: Int) {
        holder.bind(filterOptionList[position])
    }

    override fun getItemCount(): Int {
        return filterOptionList.size
    }

    fun updateData(newFilterOptionList: List<FilterOption>) {
        filterOptionList.clear()

        // 체크된 옵션 태그만 그리기
        filterOptionList.addAll(newFilterOptionList.filter {
            it.isChecked == 1
        })
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onClickItem(item: FilterOption)
    }
}