package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Event

class EventsAdapter(
    private val listener: EventListener
) : ListAdapter<Event, EventViewHolder>(EventItemCallback()) {

    interface EventListener {
        fun onLikeClickListener(event: Event)
        fun onShareClickListener(event: Event)
        fun onDeleteClickListener(event: Event)
        fun onEditClickListener(event: Event)
        fun onParticipateClickListener(event: Event)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardEventBinding.inflate(inflater, parent, false)
        val eventViewHolder = EventViewHolder(binding)

        binding.like.setOnClickListener {
            listener.onLikeClickListener(getItem(eventViewHolder.adapterPosition))
        }
        binding.share.setOnClickListener {
            listener.onShareClickListener(getItem(eventViewHolder.adapterPosition))
        }
        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.event_actions_menu)

                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            listener.onDeleteClickListener(getItem(eventViewHolder.adapterPosition))
                            true
                        }

                        R.id.edit -> {
                            listener.onEditClickListener(getItem(eventViewHolder.adapterPosition))
                            true
                        }

                        else -> false

                    }
                }
                show()
            }

        }
        binding.participation.setOnClickListener {
            listener.onParticipateClickListener(getItem(eventViewHolder.adapterPosition))
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