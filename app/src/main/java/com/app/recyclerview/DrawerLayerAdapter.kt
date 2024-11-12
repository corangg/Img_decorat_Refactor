package com.app.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.databinding.ItemDrawerBinding
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.domain.model.DrawerLayerItemData
import java.util.Collections

class DrawerLayerAdapter :
    BaseRecyclerView<DrawerLayerItemData, DrawerLayerAdapter.DrawerLayerViewHolder>(object :
        DiffUtil.ItemCallback<DrawerLayerItemData>() {
        override fun areItemsTheSame(oldItem: DrawerLayerItemData, newItem: DrawerLayerItemData) =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: DrawerLayerItemData,
            newItem: DrawerLayerItemData
        ) = oldItem == newItem
    }) {
    private lateinit var mContext: Context
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerLayerViewHolder {
        mContext = parent.context
        return DrawerLayerViewHolder(
            ItemDrawerBinding.inflate(
                LayoutInflater.from(mContext), parent, false
            )
        )
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        Collections.swap(currentList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)

        for (i in 0 until currentList.size) {
            notifyItemChanged(i)
        }
    }
    inner class DrawerLayerViewHolder(
        private val binding: ItemDrawerBinding
    ) : BaseViewHolder<DrawerLayerItemData>(binding) {
        override fun bind(
            item: DrawerLayerItemData,
            position: Int,
            clickListener: ((DrawerLayerItemData, Int) -> Unit)?
        ) {
            TODO("Not yet implemented")
        }
    }
}