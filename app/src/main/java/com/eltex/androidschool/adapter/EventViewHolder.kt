package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Event

class EventViewHolder(private val binding: CardEventBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindEvent(payLoad: EventPayLoad) {
        if (payLoad.liked != null) {
            updateLike(payLoad.liked)
        }
        if (payLoad.participated != null) {
            updateParticipate(payLoad.participated)
        }
    }

    fun bindEvent(
        event: Event,
    ) {
        binding.content.text = event.content
        binding.author.text = event.author
        binding.published.text = event.published
        binding.initial.text = event.author.take(1)
        binding.eventType.text = event.type
        binding.eventDate.text = event.datetime
        binding.link.text = event.link
        updateLike(event.likedByMe)
        updateParticipate(event.participatedByMe)
    }

    private fun updateParticipate(participatedByMe: Boolean) {
        binding.participation.setIconResource(
            if (participatedByMe) {
                R.drawable.baseline_people_24
            } else {
                R.drawable.baseline_people_border_24
            }
        )

        binding.participation.text = if (participatedByMe) {
            1
        } else {
            0
        }.toString()
    }

    private fun updateLike(likedByMe: Boolean) {
        binding.like.setIconResource(
            if (likedByMe) {
                R.drawable.baseline_favorite_24
            } else {
                R.drawable.baseline_favorite_border_24
            }
        )

        binding.like.text = if (likedByMe) {
            1
        } else {
            0
        }.toString()
    }
}