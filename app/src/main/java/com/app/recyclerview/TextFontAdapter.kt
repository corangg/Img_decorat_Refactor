package com.app.recyclerview

import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.app.R
import com.app.databinding.ItemTextFontBinding
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.core.util.getFileFromPath

class TextFontAdapter : BaseRecyclerView<String, TextFontAdapter.TextFontViewHolder>(object :
    DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem === newItem

    override fun areContentsTheSame(oldItem: String, newItem: String) = true
}) {
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextFontViewHolder {
        return TextFontViewHolder(
            ItemTextFontBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    inner class TextFontViewHolder(private val binding: ItemTextFontBinding) :
        BaseViewHolder<String>(binding) {
        override fun bind(
            item: String,
            position: Int,
            clickListener: ((String, Int) -> Unit)?
        ) {
            binding.textFontItem.typeface = Typeface.createFromFile(getFileFromPath(item)) ?: return
            val border = GradientDrawable().apply {
                setStroke(8, ContextCompat.getColor(binding.root.context, R.color.point_color))
            }

            if (position == selectedPosition) {
                binding.textFontItem.background = border
            } else {
                binding.textFontItem.background = null
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

