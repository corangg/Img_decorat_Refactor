package com.core.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseViewHolder<T>(binding: ViewDataBinding) : ViewHolder(binding.root) {
    abstract fun bind(item: T, position: Int)
}

abstract class BaseRecyclerView<T, VH : BaseViewHolder<T>>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffCallback) {
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position), position)
    }
}