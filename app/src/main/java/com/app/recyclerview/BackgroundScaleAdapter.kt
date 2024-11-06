package com.app.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.databinding.ItemBackgroundScaleBinding
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.core.util.dpToPx
import com.domain.model.BackgroundScaleItem

class BackgroundScaleViewHolder(
    private val binding: ItemBackgroundScaleBinding
) : BaseViewHolder<BackgroundScaleItem>(binding) {
    override fun bind(
        item: BackgroundScaleItem,
        position: Int,
        clickListener: ((BackgroundScaleItem, Int) -> Unit)?
    ) {
        binding.itemScaleView.layoutParams.width = itemView.context.dpToPx(item.width)
        binding.itemScaleView.layoutParams.height = itemView.context.dpToPx(item.height)
        binding.itemScaleText.text = item.scale
        itemView.setOnClickListener {
            clickListener?.invoke(item, position)
        }
    }
}

class BackgroundScaleAdapter :
    BaseRecyclerView<BackgroundScaleItem, BackgroundScaleViewHolder>(object :
        DiffUtil.ItemCallback<BackgroundScaleItem>() {
        override fun areItemsTheSame(
            oldItem: BackgroundScaleItem,
            newItem: BackgroundScaleItem
        ) = oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: BackgroundScaleItem,
            newItem: BackgroundScaleItem
        ) = oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BackgroundScaleViewHolder(
        ItemBackgroundScaleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )
}