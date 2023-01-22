package com.example.simplemoviesapp.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.simplemoviesapp.R
import com.example.simplemoviesapp.databinding.FragmentPostersSwipeBinding
import com.example.simplemoviesapp.model.network.NetworkKeys

class PostersSwipeFragment : Fragment() {

    private var _binding: FragmentPostersSwipeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPostersSwipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey("file_path") }?.apply {
            val path = getString("file_path")
            Glide.with(requireContext())
                .load("${NetworkKeys.IMAGES_BASE_URL}${path}")
                .override(1024, 768)
                .placeholder(R.drawable.loading)
                .into(binding.movieImageView)
        }

    }
}