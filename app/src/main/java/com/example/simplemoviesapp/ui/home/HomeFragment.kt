package com.example.simplemoviesapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplemoviesapp.R
import com.example.simplemoviesapp.databinding.FragmentHomeBinding
import com.example.simplemoviesapp.model.data_classes.Status
import com.example.simplemoviesapp.model.data_classes.genre_response.Genre
import com.example.simplemoviesapp.ui.movies_list.adapters.SwipePagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var swipePagerAdapter: SwipePagerAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getGenreList()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // adding observers to viewModel variables
        viewModel.apply {
            genres.observe(viewLifecycleOwner) {
                handleGenreList(genres = it)
            }
            status.observe(viewLifecycleOwner) {
                handleNetworkStatus(it)
                viewModel.clearStatus()
            }
        }

        binding.searchView.apply {
            isSubmitButtonEnabled = false
            setOnQueryTextListener(onQueryTextListener)
        }

        binding.warningLayout.loadAgainButton.setOnClickListener {
            viewModel.getGenreList()
            it.isEnabled =
                false //prevents clicking on the button before the last call is finished. it is enabled again on error happens.
        }

    }

    /**
     * it takes ArrayList of genres.
     *
     * pass the genre list to the swipePagerAdapter,
     * and attach TabLayoutMediator to the viewPager.
     */
    private fun handleGenreList(genres: ArrayList<Genre>) {
        if (genres.isNotEmpty()) {
            swipePagerAdapter = SwipePagerAdapter(this)

            swipePagerAdapter.setList(genres)

            binding.pager.adapter = swipePagerAdapter

            TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
                if (position == 0)
                    tab.text = getString(R.string.all)
                else
                    tab.text = viewModel.genres.value?.get(position - 1)?.name
            }.attach()


        }
    }

    /**
     * listener for SearchView QueryText Change
     * when a change happen, it navigates To Search Fragment with that query.
     */
    private val onQueryTextListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextChange(newText: String): Boolean {
            return false
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            if (query.isNotEmpty())
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                        query
                    )
                )
            return false
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
                binding.warningLayout.root.visibility = View.VISIBLE
                binding.warningLayout.loadAgainButton.isEnabled = true
                binding.pager.visibility = View.GONE
            }
            Status.DONE -> {
                binding.progressBar.visibility = View.GONE
                binding.warningLayout.root.visibility = View.GONE
                binding.pager.visibility = View.VISIBLE
            }
            null -> {}
        }
    }

}