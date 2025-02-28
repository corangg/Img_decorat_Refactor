package com.app.recyclerview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.app.R
import com.app.databinding.ItemTextColorBinding
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder

class TextColorAdapter : BaseRecyclerView<Int, TextColorAdapter.TextColorViewHolder>(object :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextColorViewHolder {
        mContext = parent.context
        return TextColorViewHolder(
            ItemTextColorBinding.inflate(
                LayoutInflater.from(mContext), parent, false
            )
        )
    }

    inner class TextColorViewHolder(private val binding: ItemTextColorBinding) :
        BaseViewHolder<Int>(binding) {
        private val border = GradientDrawable().apply {
            setStroke(8, ContextCompat.getColor(mContext, R.color.point_color))
        }

        override fun bind(
            item: Int,
            position: Int,
            clickListener: ((Int, Int) -> Unit)?
        ) {

            if (position == selectedPosition) {
                binding.textColorItem.background = border.apply {
                    setColor(item)
                }
            } else {
                binding.textColorItem.setBackgroundColor(item)
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