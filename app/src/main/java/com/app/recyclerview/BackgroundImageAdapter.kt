package com.app.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.databinding.ItemBackgroundImgBinding
import com.bumptech.glide.Glide
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.domain.model.UnSplashData

class BackgroundImageAdapter :
    BaseRecyclerView<UnSplashData, BackgroundImageAdapter.BackgroundImageViewHolder>(object :
        DiffUtil.ItemCallback<UnSplashData>() {
        override fun areItemsTheSame(oldItem: UnSplashData, newItem: UnSplashData) =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: UnSplashData, newItem: UnSplashData) = oldItem == newItem
    }) {
    private lateinit var mContext: Context
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundImageViewHolder {
        mContext = parent.context
        return BackgroundImageViewHolder(
            ItemBackgroundImgBinding.inflate(
                LayoutInflater.from(mContext), parent, false
            )
        )
    }

    inner class BackgroundImageViewHolder(
        private val binding: ItemBackgroundImgBinding
    ) : BaseViewHolder<UnSplashData>(binding) {
        override fun bind(item: UnSplashData, position: Int, clickListener: ((UnSplashData, Int) -> Unit)?) {
            Glide.with(binding.root).load(item.small).override(60, 100).into(binding.imageItem)
            itemView.setOnClickListener {
                clickListener?.invoke(item, position)
            }
        }
    }
}