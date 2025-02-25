package com.app.ui.fragment.sticker

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.app.databinding.FragmentEmojiBinding
import com.app.recyclerview.EmojiAdapter
import com.core.ui.BaseFragment
import com.presentation.EmojiViewModel
import com.presentation.HueViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmojiFragment(private val list: List<String>) : BaseFragment<FragmentEmojiBinding>(FragmentEmojiBinding::inflate) {
    private val viewModel: EmojiViewModel by viewModels()
    private val adapter by lazy { EmojiAdapter() }

    override fun setUi() {
        binding.emojiRecycle.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 8, GridLayoutManager.VERTICAL, false)
            adapter = this@EmojiFragment.adapter
        }

        adapter.setOnItemClickListener { item, position ->
            viewModel.addEmojiLayer(item)
        }
        adapter.submitList(list)
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }

}
