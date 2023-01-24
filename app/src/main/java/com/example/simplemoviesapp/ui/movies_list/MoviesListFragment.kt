package com.example.simplemoviesapp.ui.movies_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemoviesapp.R
import com.example.simplemoviesapp.databinding.FragmentMoviesListBinding
import com.example.simplemoviesapp.model.data_classes.Status
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie
import com.example.simplemoviesapp.ui.home.HomeFragmentDirections
import com.example.simplemoviesapp.ui.movies_list.adapters.MoviesAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesListFragment : Fragment(), ListFragmentCommunicator {

    private val viewModel: MoviesListViewModel by viewModels()

    private lateinit var moviesAdapter: MoviesAdapter

    private var genreId = 0
    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //getting the genre id from arguments and calling the viewModel to get MoviesList with it.
        arguments?.takeIf { it.containsKey("genre_id") }?.apply {
            val genreId = getInt("genre_id")
            this@MoviesListFragment.genreId = genreId
            viewModel.getMovies(genreId = genreId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviesAdapter = MoviesAdapter(requireContext(), this)

        binding.moviesRecycleView.adapter = moviesAdapter
        binding.moviesRecycleView.addOnScrollListener(recycleViewListener)

        viewModel.apply {
            movies.observe(viewLifecycleOwner) {
                handleMoviesList(movies = it)
            }

            status.observe(viewLifecycleOwner) {
                handleNetworkStatus(it)
                viewModel.clearStatus()
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getMovies(genreId)
            binding.swipeRefresh.isRefreshing = false
        }

        binding.warningLayout.loadAgainButton.setOnClickListener {
            viewModel.getMovies(genreId)
            it.isEnabled =
                false //prevents clicking on the button before the last call is finished. it is enabled again on error happens.
        }

    }

    /**
     * listener on scrolling in recycleView
     * when it reaches the end of the list it calls the viewModel to get the next page
     */
    private val recycleViewListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (!recyclerView.canScrollVertically(1)) {
                viewModel.getNextMovies(genreId)
            }
        }
    }

    /**
     * it takes a list of Movie
     *
     * it submit it to the moviesAdapter
     * and if the list is empty it displays a warring layout.
     */
    private fun handleMoviesList(movies: ArrayList<Movie>) {
        if (movies.isEmpty()) {
            binding.warningLayout.root.visibility = View.VISIBLE
            binding.warningLayout.loadAgainButton.isEnabled = true
            binding.moviesRecycleView.visibility = View.GONE

        } else {
            binding.warningLayout.root.visibility = View.GONE
            binding.warningLayout.loadAgainButton.isEnabled = true
            binding.moviesRecycleView.visibility = View.VISIBLE

            moviesAdapter.submitList(movies.toList())
        }
    }


    /**
     * handle the network calling changes
     */
    private fun handleNetworkStatus(it: Status?) {
        when (it) {
            Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                Snackbar.make(
                    binding.root,
                    getString(R.string.error_happened),
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = View.GONE
            }
            Status.DONE -> {
                binding.progressBar.visibility = View.GONE
            }
            null -> {}
        }
    }

    override fun goToDetails(movieId: Int) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                movieId
            )
        )
    }

}

interface ListFragmentCommunicator {
    fun goToDetails(movieId: Int)
}