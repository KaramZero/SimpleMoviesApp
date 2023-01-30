package com.example.simplemoviesapp.ui.movie_details

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
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.example.Posters
import com.example.simplemoviesapp.R
import com.example.simplemoviesapp.databinding.FragmentDetailsBinding
import com.example.simplemoviesapp.model.data_classes.Status
import com.example.simplemoviesapp.model.data_classes.movie_credits_response.Cast
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie
import com.example.simplemoviesapp.model.network.NetworkKeys
import com.example.simplemoviesapp.ui.adapters.CastAdapter
import com.example.simplemoviesapp.ui.adapters.CastListAdapterCommunicator
import com.example.simplemoviesapp.ui.movie_details.adapters.PostersSwipePagerAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.BlurTransformation


@AndroidEntryPoint
class DetailsFragment : Fragment(), CastListAdapterCommunicator {
    private val viewModel: MovieDetailsViewModel by viewModels()

    //used for view pager images switching with buttons.
    private var currentIndex = 0
    private var imagesCount = 0

    private lateinit var movieId: String

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = DetailsFragmentArgs.fromBundle(requireArguments()).movieId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createToolBar()

        handleImageSwitcher()

        viewModel.loadMovieData(movieId)

        //add observers to viewModel LiveData variables
        viewModel.apply {
            posters.observe(viewLifecycleOwner) { handleMoviePosters(posters = it) }

            movie.observe(viewLifecycleOwner) { handleMovieDetails(movie = it) }

            cast.observe(viewLifecycleOwner) { handleMovieCast(cast = it) }

            status.observe(viewLifecycleOwner) {
                handleNetworkStatus(it)
                viewModel.clearStatus()
            }
        }

        binding.pager.registerOnPageChangeCallback(onPageChangeCallback)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadMovieData(movieId)
            binding.swipeRefresh.isRefreshing = false
        }

        binding.warningLayout.loadAgainButton.setOnClickListener {
            viewModel.loadMovieData(movieId)
            it.isEnabled = false //prevents clicking on the button before the last call is finished. it is enabled again on error happens.
        }

    }

    /**
     * to listen to page changes on the viewPager.
     * it displays the poster image on the background with Blur Transformation and with CrossFade Transition.
     */
    private val onPageChangeCallback = object : OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val poster = viewModel.posters.value?.get(position)

            Glide.with(requireContext())
                .load("${NetworkKeys.IMAGES_BASE_URL}${poster?.filePath}")
                .override(1024, 768)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 5)))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(binding.backgroundImageView.drawable) //using previous image to prevent image flickering when switching images.
                .into(binding.backgroundImageView)

        }
    }


    /**
     * it takes an ArrayList of Cast.
     *
     * it creates a CastAdapter with that list and attach it to the castRecycleView
     */
    private fun handleMovieCast(cast: ArrayList<Cast>?) {
        val adapter = CastAdapter(requireContext(),this)
        adapter.submitList(cast)
        binding.castRecycleView.adapter = adapter
    }


    /**
     * it takes movie, and bind its data to the views
     */
    private fun handleMovieDetails(movie: Movie) {
        binding.apply {
            titleTextView.text = movie.title
            productionYearTextView.text = movie.releaseDate?.take(4) ?: "0000"  //take first for to get the year only form the date.
            ratingBar.rating = (movie.voteAverage?.div(2))?.toFloat() ?: 0f  //div with 2 because our rating bar is five stars, and the av is 10.
            descriptionTextView.text = movie.overview
            binding.toolbar.title = movie.title

        }
    }

    /**
     * it takes an ArrayList of Posters.
     *
     * it creates a PostersSwipePagerAdapter with that list and attach it to the pager
     */
    private fun handleMoviePosters(posters: ArrayList<Posters>) {
        val swipePagerAdapter = PostersSwipePagerAdapter(this@DetailsFragment)
        swipePagerAdapter.setList(posters)
        binding.pager.adapter = swipePagerAdapter
        imagesCount = posters.size
    }

    /**
     * it creates a ToolBar and setupWithNavController.
     * and set its title to loading..
     */
    private fun createToolBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.apply {
            setupWithNavController(findNavController(), appBarConfiguration)
            title = getString(R.string.loading)
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
                binding.scrollView.visibility = View.GONE
            }
            Status.DONE -> {
                binding.scrollView.visibility = View.VISIBLE
                binding.warningLayout.root.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
            null -> {}
        }
    }


    /**
     * create and handle the estateItem images to be displayed as an imageSwitcher
     */
    private fun handleImageSwitcher() {

        // set on click listener on left button
        binding.btPrevious.setOnClickListener {
            --currentIndex
            // condition
            if (currentIndex < 0) currentIndex = imagesCount - 1
            binding.pager.setCurrentItem(currentIndex, true)
        }

        // set on click listener on right(next) button
        binding.btNext.setOnClickListener {
            currentIndex++
            // condition
            if (currentIndex >= imagesCount) currentIndex = 0
            binding.pager.setCurrentItem(currentIndex, true)
        }
    }

    override fun goToMoviesByActorFragment(actorId: Long, actorName: String) {
        findNavController().navigate(
            DetailsFragmentDirections.actionDetailsFragmentToMoviesByActorFragment(
                actorId.toString(),
                actorName
            )
        )
    }

}