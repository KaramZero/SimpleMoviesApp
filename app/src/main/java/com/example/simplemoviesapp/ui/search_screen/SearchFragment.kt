package com.example.simplemoviesapp.ui.search_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemoviesapp.R
import com.example.simplemoviesapp.databinding.FragmentSearchBinding
import com.example.simplemoviesapp.model.data_classes.Status
import com.example.simplemoviesapp.ui.adapters.MoviesAdapter
import com.example.simplemoviesapp.ui.adapters.MoviesListAdapterCommunicator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment(), MoviesListAdapterCommunicator {

    private val viewModel: SearchMoviesViewModel by viewModels()

    private lateinit var moviesAdapter: MoviesAdapter
    private var query = ""

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        moviesAdapter = MoviesAdapter(requireContext(), this)

        arguments?.takeIf { it.containsKey("query") }?.apply {
            val query = getString("query")
            this@SearchFragment.query = query.toString()
        }

        viewModel.movies.observe(requireActivity()) {
            if (it.isEmpty()){
                binding.moviesListLayout.root.visibility = View.GONE
                binding.noResultLayout.visibility = View.VISIBLE
            }else{
                binding.moviesListLayout.root.visibility = View.VISIBLE
                binding.noResultLayout.visibility = View.GONE
            }
            moviesAdapter.submitList(it.toList())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.movies.removeObservers(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.moviesListLayout.moviesRecycleView.adapter = moviesAdapter
        binding.moviesListLayout.moviesRecycleView.addOnScrollListener(recycleViewListener)

        viewModel.status.observe(viewLifecycleOwner) {
            handleNetworkStatus(it)
            viewModel.clearStatus()
        }

        binding.moviesListLayout.swipeRefresh.setOnRefreshListener {
            viewModel.searchForMovies(query)
            binding.moviesListLayout.swipeRefresh.isRefreshing = false
        }

        binding.searchView.apply {
            isSubmitButtonEnabled = false
            setOnQueryTextListener(onQueryTextListener)
            setQuery(this@SearchFragment.query, true)
        }

    }

    /**
     * listener for SearchView QueryText Change
     * when a change happen, it calls the viewModel to get a new list with that query as a search
     */
    private val onQueryTextListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextChange(newText: String): Boolean {
            return false
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            if (query.isNotEmpty()) {
                this@SearchFragment.query = query
                viewModel.searchForMovies(query)
            }
            return false
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
                viewModel.getNextMovies(query)
            }
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
            SearchFragmentDirections.actionSearchFragmentToDetailsFragment(
                movieId.toString()
            )
        )
    }


}