package com.app.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.databinding.ItemEmojiBinding
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder

class EmojiAdapter : BaseRecyclerView<String, EmojiAdapter.EmojiViewHolder>(object :
    DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) =
        oldItem.hashCode() == newItem.hashCode()

    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}) {
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        mContext = parent.context
        return EmojiViewHolder(
            ItemEmojiBinding.inflate(
                LayoutInflater.from(mContext), parent, false
            )
        )
    }

    inner class EmojiViewHolder(
        private val binding: ItemEmojiBinding
    ) : BaseViewHolder<String>(binding) {
        override fun bind(item: String, position: Int, clickListener: ((String, Int) -> Unit)?) {
            binding.emojiItem.text = item
            itemView.setOnClickListener {
                it.animate()
                    .scaleX(1.6f)
                    .scaleY(1.6f)
                    .setDuration(200)
                    .withEndAction {
                        it.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
                    }
                    .start()
                clickListener?.invoke(item, position)
            }
        }
    }
}