package com.app.recyclerview

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.app.R
import com.app.databinding.ItemDrawerBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.domain.model.ViewItemData
import java.util.Collections

class DrawerLayerAdapter :
    BaseRecyclerView<ViewItemData, DrawerLayerAdapter.DrawerLayerViewHolder>(object :
        DiffUtil.ItemCallback<ViewItemData>() {
        override fun areItemsTheSame(oldItem: ViewItemData, newItem: ViewItemData) =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: ViewItemData,
            newItem: ViewItemData
        ) = oldItem == newItem
    }) {
    private var selectedPosition = -1
    private lateinit var mContext: Context

    private var onItemDeleteListener: ((Int) -> Unit)? = null
    private var onItemCheckListener: ((Int, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerLayerViewHolder {
        mContext = parent.context
        return DrawerLayerViewHolder(
            ItemDrawerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val mutableList = currentList.toMutableList()
        Collections.swap(mutableList, fromPosition, toPosition)
        submitList(mutableList)
    }

    override fun onBindViewHolder(holder: DrawerLayerViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.addBind(position, onItemCheckListener, onItemDeleteListener)
    }

    fun setOnItemDeleteListener(listener: (Int) -> Unit) {
        onItemDeleteListener = listener
    }

    fun setOnItemCheckListener(listener: (Int, Boolean) -> Unit) {
        onItemCheckListener = listener
    }

    fun selectView(select: Int) {
        val previousPosition = selectedPosition
        selectedPosition = select

        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }

    inner class DrawerLayerViewHolder(
        private val binding: ItemDrawerBinding
    ) : BaseViewHolder<ViewItemData>(binding) {
        override fun bind(
            item: ViewItemData,
            position: Int,
            clickListener: ((ViewItemData, Int) -> Unit)?
        ) {
            if (position == selectedPosition) {
                itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.point_color))
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT)
            }

            binding.check.isChecked = item.visible
            binding.editImageView.apply {
                setMatrixData(item.matrixValues, item.scale, item.rotationDegrees)
                layoutParams.apply {
                    width = FrameLayout.LayoutParams.WRAP_CONTENT
                    height = FrameLayout.LayoutParams.WRAP_CONTENT
                }
            }

            Glide.with(binding.itemView.context).load(item.img).override(256, 128).format(
                DecodeFormat.PREFER_RGB_565
            ).into(binding.editImageView)

            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = position

                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                clickListener?.invoke(item, position)
            }
        }

        fun addBind(
            position: Int,
            checkListener: ((Int, Boolean) -> Unit)?,
            deleteListener: ((Int) -> Unit)?
        ) {
            binding.check.setOnClickListener {
                checkListener?.invoke(position, binding.check.isChecked)
            }

            binding.layerDelete.setOnClickListener {
                deleteListener?.invoke(position)
            }
        }


    }
}


/*
class DrawerLayerAdapter :
    BaseRecyclerView<ViewItemData, DrawerLayerAdapter.DrawerLayerViewHolder>(object :
        DiffUtil.ItemCallback<ViewItemData>() {
        override fun areItemsTheSame(oldItem: ViewItemData, newItem: ViewItemData) =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: ViewItemData,
            newItem: ViewItemData
        ) = oldItem == newItem
    }) {
    private var selectedPosition = 0

    private var screenSize = Pair(0, 0)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerLayerViewHolder {
        return DrawerLayerViewHolder(
            ItemDrawerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val mutableList = currentList.toMutableList()
        Collections.swap(mutableList, fromPosition, toPosition)
        submitList(mutableList)
    }

    fun setScreenSize(size: Pair<Int, Int>) {
        screenSize = size
    }

    inner class DrawerLayerViewHolder(
        private val binding: ItemDrawerBinding
    ) : BaseViewHolder<ViewItemData>(binding) {
        override fun bind(
            item: ViewItemData,
            position: Int,
            clickListener: ((ViewItemData, Int) -> Unit)?
        ) {
            binding.editImageView.reset()

            Glide.with(binding.editImageView.context)
                .load(item.img)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: com.bumptech.glide.request.transition.Transition<in Drawable>?
                    ) {
                        binding.editImageView.setImageDrawable(resource)
                        setMatrixData(item.matrixValues, item.scale, item.rotationDegrees)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // 필요에 따라 처리
                    }
                })

            itemView.setOnClickListener {
                clickListener?.invoke(item, position)
            }
        }

        fun setMatrixData(
            matrixValue: Array<Float>,
            scale: Float,
            rotationDegrees: Float
        ) {
            if (matrixValue.size != 9 || screenSize.first == 0 || screenSize.second == 0) return
            binding.editImageView.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.editImageView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    val currentWidth = binding.editImageView.width.toFloat()
                    val currentHeight = binding.editImageView.height.toFloat()

                    val scaleX = currentWidth / screenSize.first
                    val scaleY = currentHeight / screenSize.second

                    val floatArray = matrixValue.toFloatArray()
                    floatArray[Matrix.MTRANS_X] *= scaleX
                    floatArray[Matrix.MTRANS_Y] *= scaleY

                    floatArray[Matrix.MSCALE_X] *= scaleX
                    floatArray[Matrix.MSCALE_Y] *= scaleY

                    val adjustedMatrix = Array(9) { index -> floatArray[index] }

                    binding.editImageView.setMatrixData(
                        adjustedMatrix,
                        scale,
                        rotationDegrees
                    )
                }
            })
        }
    }
}*/
