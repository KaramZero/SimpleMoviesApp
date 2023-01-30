package com.example.simplemoviesapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplemoviesapp.R
import com.example.simplemoviesapp.databinding.CastRowItemBinding
import com.example.simplemoviesapp.model.data_classes.movie_credits_response.Cast
import com.example.simplemoviesapp.model.network.NetworkKeys


class CastAdapter constructor(
    private val context: Context,
    private val communicator: CastListAdapterCommunicator
) :
    ListAdapter<Cast, CastAdapter.ViewHolder>(CastDiffCallback()) {

    private var lastPosition = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CastRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(
            item = currentItem,
            context = context
        )

        holder.itemView.setOnClickListener {
            currentItem.id?.let { id ->
                communicator.goToMoviesByActorFragment(
                    actorId = id,
                    actorName = currentItem.name.toString()
                )
            }
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


    class ViewHolder(private val binding: CastRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Cast,
            context: Context,
        ) {

            binding.apply {

                nameTextView.text = item.name
                Glide.with(context)
                    .load("${NetworkKeys.IMAGES_BASE_URL}${item.profilePath}")
                    .override(1024, 768)
                    .placeholder(R.drawable.loading)
                    .into(actorImageView)
            }

        }

    }

}

class CastDiffCallback : DiffUtil.ItemCallback<Cast>() {
    override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
        return oldItem == newItem
    }

}

interface CastListAdapterCommunicator {
    fun goToMoviesByActorFragment(actorId: Long, actorName: String)
}