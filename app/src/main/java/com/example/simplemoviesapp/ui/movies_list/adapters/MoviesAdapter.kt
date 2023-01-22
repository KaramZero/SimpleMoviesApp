package com.example.simplemoviesapp.ui.movies_list.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.simplemoviesapp.R
import com.example.simplemoviesapp.databinding.MovieRowItemBinding
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie
import com.example.simplemoviesapp.model.network.NetworkKeys
import com.example.simplemoviesapp.ui.movies_list.ListFragmentCommunicator
import jp.wasabeef.glide.transformations.BlurTransformation


class MoviesAdapter constructor(
    private val context: Context,
    private val communicator: ListFragmentCommunicator
) :
    ListAdapter<Movie, MoviesAdapter.ViewHolder>(MovieDiffCallback()) {

    private var lastPosition = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MovieRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(
            item = currentItem,
            context = context
        )

        holder.itemView.setOnClickListener {
            currentItem.id.let { id -> communicator.goToDetails(id) }
        }


        //for the views Animation, when binding each view.
        if (position >= lastPosition || (position == 0 && lastPosition != 1)) //to check the direction of creating the views(from up/from down).
            holder.itemView.animation = AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.recycle_item_from_down_animation
            )
        else holder.itemView.animation = AnimationUtils.loadAnimation(
            holder.itemView.context,
            R.anim.recycle_item_from_up_animation
        )

        lastPosition = holder.layoutPosition

    }


    class ViewHolder(private val binding: MovieRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Movie,
            context: Context,
        ) {

            binding.apply {

                titleTextView.text = item.title
                productionYearTextView.text = item.releaseDate?.take(4) ?: "0000"

                //show the image on background with BlurTransformation.
                Glide.with(context)
                    .load("${NetworkKeys.IMAGES_BASE_URL}${item.posterPath}")
                    .override(1024, 768)
                    .apply(bitmapTransform(BlurTransformation(25, 3)))
                    .into(backgroundImageView)

                Glide.with(context)
                    .load("${NetworkKeys.IMAGES_BASE_URL}${item.posterPath}")
                    .override(1024, 768)
                    .placeholder(R.drawable.loading)
                    .into(movieImageView)

            }

        }

    }

}

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

}
