package com.example.simplemoviesapp.ui.movies_by_actor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.simplemoviesapp.R
import com.example.simplemoviesapp.databinding.FragmentMoviesByActorBinding
import com.example.simplemoviesapp.model.data_classes.Status
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie
import com.example.simplemoviesapp.ui.adapters.MoviesAdapter
import com.example.simplemoviesapp.ui.adapters.MoviesListAdapterCommunicator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MoviesByActorFragment : Fragment(), MoviesListAdapterCommunicator {

    private val viewModel: MoviesByActorViewModel by viewModels()
    private lateinit var moviesAdapter: MoviesAdapter
    private var actorId = ""
    private var actorName = ""


    private var _binding: FragmentMoviesByActorBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesByActorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actorId = MoviesByActorFragmentArgs.fromBundle(requireArguments()).actorID
        actorName = MoviesByActorFragmentArgs.fromBundle(requireArguments()).actorName

        viewModel.getMoviesByActor(actorId)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createToolBar()

        moviesAdapter = MoviesAdapter(requireContext(), this)

        binding.moviesListLayout.moviesRecycleView.adapter = moviesAdapter

        viewModel.apply {
            movies.observe(viewLifecycleOwner) {
                handleMoviesList(movies = it)
            }

            status.observe(viewLifecycleOwner) {
                handleNetworkStatus(it)
                viewModel.clearStatus()
            }
        }


        binding.moviesListLayout.swipeRefresh.setOnRefreshListener {
            viewModel.getMoviesByActor(actorId)
            binding.moviesListLayout.swipeRefresh.isRefreshing = false
        }

        binding.warningLayout.loadAgainButton.setOnClickListener {
            viewModel.getMoviesByActor(actorId)
            it.isEnabled =
                false //prevents clicking on the button before the last call is finished. it is enabled again on error happens.
        }

    }

    /**
     * it creates a ToolBar and setupWithNavController.
     * and set its title to loading..
     */
    private fun createToolBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.apply {
            setupWithNavController(findNavController(), appBarConfiguration)
            title = actorName //getString(R.string.loading)
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
            binding.moviesListLayout.moviesRecycleView.visibility = View.GONE

        } else {
            binding.warningLayout.root.visibility = View.GONE
            binding.warningLayout.loadAgainButton.isEnabled = true
            binding.moviesListLayout.moviesRecycleView.visibility = View.VISIBLE

            moviesAdapter.submitList(movies.toList())
        }
    }


    /**
     * handle the network calling changes
     */
    private fun handleNetworkStatus(it: Status?) {
        when (it) {
            Status.LOADING -> {
                binding.moviesListLayout.progressBar.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                Snackbar.make(
                    binding.root,
                    getString(R.string.error_happened),
                    Toast.LENGTH_SHORT
                ).show()
                binding.moviesListLayout.progressBar.visibility = View.GONE
            }
            Status.DONE -> {
                binding.moviesListLayout.progressBar.visibility = View.GONE
            }
            null -> {}
        }
    }

    override fun goToDetails(movieId: Long) {
        findNavController().navigate(
            MoviesByActorFragmentDirections.actionMoviesByActorFragmentToDetailsFragment(
                movieId.toString()
            )
        )
    }

}