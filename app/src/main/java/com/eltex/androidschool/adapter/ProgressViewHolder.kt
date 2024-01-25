package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.databinding.CardEventSkeletonBinding
import com.eltex.androidschool.model.EventUiModel

class ProgressViewHolder(private val binding: CardEventSkeletonBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindSkeleton(
        skeleton: EventUiModel,
    ) {
        binding.content.text = skeleton.content
        binding.author.text = skeleton.author
        binding.published.text = skeleton.published
        binding.eventType.text = skeleton.type
        binding.eventDate.text = skeleton.datetime
        binding.link.text = skeleton.link
        binding.like.text = skeleton.likes.toString()
        binding.participation.text = skeleton.participants.toString()
    }
}