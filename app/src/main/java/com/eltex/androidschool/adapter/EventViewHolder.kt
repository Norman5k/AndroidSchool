package com.eltex.androidschool.adapter

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Attachment
import com.eltex.androidschool.model.EventUiModel

class EventViewHolder(
    private val binding: CardEventBinding) :
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
        if (payLoad.attachment != null) {
            updateAttachment(payLoad.attachment)
        }
        if (payLoad.authorAvatar != null) {
            updateAuthorAvatar(payLoad.authorAvatar)
        }
    }

    private fun updateAuthorAvatar(authorAvatar: String) {
        Glide.with(binding.avatar)
            .load(authorAvatar)
            .transform(CircleCrop())
            .into(binding.avatar)
    }

    private fun updateAttachment(attachment: Attachment) {
        Glide.with(binding.attachmentPhoto)
            .load(attachment.url)
            .into(binding.attachmentPhoto)
    }

    fun bindEvent(
        event: EventUiModel,
    ) {
        /*binding.root.setContent {
            CardEvent(
                event = event,
                onDeleteClickListener = { listener.onDeleteClickListener(event) },
                onEditClickListener = { listener.onEditClickListener(event) },
                onLikeClickListener = { listener.onLikeClickListener(event) },
                onShareClickListener = { listener.onShareClickListener(event) },
                onParticipateClickListener = {listener.onParticipateClickListener(event)}
            )
        }
        }*/
        binding.content.text = event.content
        binding.author.text = event.author
        binding.published.text = event.published
        if (event.authorAvatar != null) {
            binding.initial.isGone = true
            updateAuthorAvatar(event.authorAvatar)
        } else {
            binding.avatar.setImageResource(R.drawable.avatar_background)
            binding.initial.isVisible = true
            binding.initial.text = event.author.take(1)
        }
        binding.eventType.text = event.type
        binding.eventDate.text = event.datetime
        binding.link.text = event.link
        updateLikeIcon(event.likedByMe)
        updateLikeCount(event.likes)
        updateParticipateIcon(event.participatedByMe)
        updateParticipateCount(event.participants)
        if (event.attachment != null) {
            binding.attachmentPhoto.isVisible = true
            updateAttachment(event.attachment)
        } else {
            binding.attachmentPhoto.isGone = true
        }
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