package com.app.recyclerview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.ui.fragment.sticker.EmojiFragment

class EmojiGroupAdapter(activity: FragmentActivity, private val listSize: Int) :
    FragmentStateAdapter(activity) {
    override fun getItemCount() = listSize

    override fun createFragment(position: Int): Fragment {
        return EmojiFragment()
    }
}