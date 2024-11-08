package com.app.recyclerview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.app.R
import com.app.databinding.ItemBackgroundColorBinding
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder

class BackgroundColorAdapter :
    BaseRecyclerView<Int, BackgroundColorAdapter.BackgroundColorViewHolder>(object :
        DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(
            oldItem: Int,
            newItem: Int
        ) = oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: Int,
            newItem: Int
        ) = oldItem == newItem
    }) {
    private lateinit var mContext: Context
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundColorViewHolder {
        mContext = parent.context
        return BackgroundColorViewHolder(
            ItemBackgroundColorBinding.inflate(
                LayoutInflater.from(mContext), parent, false
            )
        )
    }

    inner class BackgroundColorViewHolder(
        private val binding: ItemBackgroundColorBinding
    ) : BaseViewHolder<Int>(binding) {
        private val border = GradientDrawable().apply {
            setStroke(8, ContextCompat.getColor(mContext, R.color.point_color))
        }

        override fun bind(
            item: Int,
            position: Int,
            clickListener: ((Int, Int) -> Unit)?
        ) {

            if (position == selectedPosition) {
                binding.colorItem.background = border.apply {
                    setColor(item)
                }
            } else {
                binding.colorItem.setBackgroundColor(item)
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
}