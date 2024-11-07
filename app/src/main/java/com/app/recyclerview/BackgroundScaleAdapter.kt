package com.app.recyclerview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.app.R
import com.app.databinding.ItemBackgroundScaleBinding
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.core.util.dpToPx
import com.domain.model.BackgroundScaleItem

class BackgroundScaleAdapter :
    BaseRecyclerView<BackgroundScaleItem, BackgroundScaleAdapter.BackgroundScaleViewHolder>(object :
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
    private lateinit var mContext: Context
    private var selectedPosition = 0

    inner class BackgroundScaleViewHolder(
        private val binding: ItemBackgroundScaleBinding
    ) : BaseViewHolder<BackgroundScaleItem>(binding) {
        private val border = GradientDrawable().apply {
            setStroke(8, ContextCompat.getColor(mContext, R.color.point_color))
            cornerRadius = 16f
        }

        override fun bind(
            item: BackgroundScaleItem,
            position: Int,
            clickListener: ((BackgroundScaleItem, Int) -> Unit)?
        ) {
            binding.itemScaleView.layoutParams.width = itemView.context.dpToPx(item.width)
            binding.itemScaleView.layoutParams.height = itemView.context.dpToPx(item.height)
            binding.itemScaleText.text = item.scale

            if (position == selectedPosition) {
                binding.itemBackgroundScale.background = border
            } else {
                binding.itemBackgroundScale.background = null
            }

            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = position

                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                clickListener?.invoke(item, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundScaleViewHolder {
        mContext = parent.context
        return BackgroundScaleViewHolder(
            ItemBackgroundScaleBinding.inflate(
                LayoutInflater.from(mContext), parent, false
            )
        )
    }
}