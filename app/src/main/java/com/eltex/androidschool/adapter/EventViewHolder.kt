package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.EventUiModel

class EventViewHolder(private val binding: CardEventBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindEvent(payLoad: EventPayLoad) {
        if (payLoad.liked != null) {
            updateLikeIcon(payLoad.liked)
        }
        if (payLoad.likes != null) {
            updateLikeCount(payLoad.likes)
        }
        if (payLoad.participated != null) {
            updateParticipateIcon(payLoad.participated)
        }
        if (payLoad.participants != null) {
            updateParticipateCount(payLoad.participants)
        }
    }

    fun bindEvent(
        event: EventUiModel,
    ) {
        binding.content.text = event.content
        binding.author.text = event.author
        binding.published.text = event.published
        binding.initial.text = event.author.take(1)
        binding.eventType.text = event.type
        binding.eventDate.text = event.datetime
        binding.link.text = event.link
        updateLikeIcon(event.likedByMe)
        updateLikeCount(event.likes)
        updateParticipateIcon(event.participatedByMe)
        updateParticipateCount(event.participants)
    }

    private fun updateParticipateIcon(participatedByMe: Boolean) {
        binding.participation.setIconResource(
            if (participatedByMe) {
                R.drawable.baseline_people_24
            } else {
                R.drawable.baseline_people_border_24
            }
        )

    }

    private fun updateLikeIcon(likedByMe: Boolean) {
        binding.like.setIconResource(
            if (likedByMe) {
                R.drawable.baseline_favorite_24
            } else {
                R.drawable.baseline_favorite_border_24
            }
        )
    }

    private fun updateLikeCount(likes: Int) {
        binding.like.text = likes.toString()
    }

    private fun updateParticipateCount(participants: Int) {
        binding.participation.text = participants.toString()
    }
}