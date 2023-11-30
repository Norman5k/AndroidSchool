package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Event

class EventsAdapter(
    private val likeClickListener: (Event) -> Unit,
    private val shareClickListener: () -> Unit,
    private val menuClickListener: () -> Unit,
    private val participateClickListener: (Event) -> Unit
) : ListAdapter<Event, EventViewHolder>(EventItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardEventBinding.inflate(inflater, parent, false)
        val eventViewHolder = EventViewHolder(binding)

        binding.like.setOnClickListener {
            likeClickListener(getItem(eventViewHolder.adapterPosition))
        }
        binding.share.setOnClickListener {
            shareClickListener()
        }
        binding.menu.setOnClickListener {
            menuClickListener()
        }
        binding.participation.setOnClickListener {
            participateClickListener(getItem(eventViewHolder.adapterPosition))
        }

        return eventViewHolder
    }

    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach {
                if (it is EventPayLoad) {
                    holder.bindEvent(it)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindEvent(getItem(position))
    }
}