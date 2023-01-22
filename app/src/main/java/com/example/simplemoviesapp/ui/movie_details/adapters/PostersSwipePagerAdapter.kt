package com.example.simplemoviesapp.ui.movie_details.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.example.Posters
import com.example.simplemoviesapp.ui.movie_details.PostersSwipeFragment

class PostersSwipePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var posters: ArrayList<Posters> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(posters: ArrayList<Posters>) {
        this.posters = posters
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return posters.size
    }

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = PostersSwipeFragment()
        fragment.arguments = Bundle().apply {
            putString("file_path",posters[position].filePath)
        }
        return fragment
    }
}