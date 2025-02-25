package com.app.ui.fragment.sticker

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.app.databinding.FragmentEmojiBinding
import com.app.recyclerview.EmojiAdapter
import com.core.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmojiFragment(private val list: List<String>) : BaseFragment<FragmentEmojiBinding>(FragmentEmojiBinding::inflate) {
    //private val viewModel: HueViewModel by viewModels()
    private val adapter by lazy { EmojiAdapter() }

    override fun setUi() {
        binding.emojiRecycle.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 8, GridLayoutManager.VERTICAL, false)
            adapter = this@EmojiFragment.adapter
        }

        adapter.setOnItemClickListener { item, position ->
            position
            item
            true
        }
        adapter.submitList(list)
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }

}
