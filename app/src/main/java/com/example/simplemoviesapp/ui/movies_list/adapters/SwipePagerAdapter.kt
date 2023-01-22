package com.example.simplemoviesapp.ui.movies_list.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.simplemoviesapp.model.data_classes.genre_response.Genre
import com.example.simplemoviesapp.ui.movies_list.MoviesListFragment

class SwipePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var genres: ArrayList<Genre> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(genres: ArrayList<Genre>) {
        this.genres = genres
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return genres.size + 1 //adding 1 because the "ALL" filter
    }

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = MoviesListFragment()
        fragment.arguments = Bundle().apply {
            if (position == 0)
                putInt("genre_id", 0)
            else
                genres[position-1].id?.let { putInt("genre_id", it) }
        }
        return fragment
    }
}