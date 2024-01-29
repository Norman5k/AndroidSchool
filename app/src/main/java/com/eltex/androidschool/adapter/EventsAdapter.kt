package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.databinding.CardEventSkeletonBinding
import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.PagingModel

class EventsAdapter(
    private val listener: EventListener
) : ListAdapter<PagingModel<EventUiModel>, RecyclerView.ViewHolder>(EventPagingModelItemCallback()) {

    interface EventListener {
        fun onLikeClickListener(event: EventUiModel)
        fun onShareClickListener(event: EventUiModel)
        fun onDeleteClickListener(event: EventUiModel)
        fun onEditClickListener(event: EventUiModel)
        fun onParticipateClickListener(event: EventUiModel)
        fun onRetryClickListener()
    }

    override fun getItemViewType(position: Int): Int =
        when(getItem(position)) {
            is PagingModel.Data -> R.layout.card_event
            is PagingModel.Error -> R.layout.item_error
            is PagingModel.Progress -> R.layout.card_event_skeleton
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType) {
            R.layout.card_event -> createEventViewHolder(parent)
            R.layout.item_error -> createErrorViewHolder(parent)
            R.layout.card_event_skeleton -> createProgressViewHolder(parent)
            else -> error("Unknown ViewType: $viewType")
        }

    private fun createProgressViewHolder(parent: ViewGroup) : ProgressViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardEventSkeletonBinding.inflate(inflater, parent, false)

        return ProgressViewHolder(binding)
    }
    private fun createErrorViewHolder(parent: ViewGroup) : ErrorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemErrorBinding.inflate(inflater, parent, false)

        binding.retryButton.setOnClickListener {
            listener.onRetryClickListener()
        }
        return ErrorViewHolder(binding)
    }

    private fun createEventViewHolder(parent: ViewGroup) : EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardEventBinding.inflate(inflater, parent, false)
        val eventViewHolder = EventViewHolder(binding)

        binding.like.setOnClickListener {
            listener.onLikeClickListener((getItem(eventViewHolder.adapterPosition) as PagingModel.Data).value)
        }
        binding.share.setOnClickListener {
            listener.onShareClickListener((getItem(eventViewHolder.adapterPosition) as PagingModel.Data).value)
        }
        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.event_actions_menu)

                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            listener.onDeleteClickListener((getItem(eventViewHolder.adapterPosition) as PagingModel.Data).value)
                            true
                        }

                        R.id.edit -> {
                            listener.onEditClickListener((getItem(eventViewHolder.adapterPosition) as PagingModel.Data).value)
                            true
                        }

                        else -> false
                    }
                }
                show()
            }
        }
        binding.participation.setOnClickListener {
            listener.onParticipateClickListener((getItem(eventViewHolder.adapterPosition) as PagingModel.Data).value)
        }

        return eventViewHolder
    }
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach {
                if (it is EventPayLoad && holder is EventViewHolder) {
                    holder.bindEvent(it)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is PagingModel.Data -> (holder as EventViewHolder).bindEvent(item.value)
            is PagingModel.Error -> (holder as ErrorViewHolder).bind(item.reason)
            is PagingModel.Progress -> Unit
            else -> {}
        }

    }
}