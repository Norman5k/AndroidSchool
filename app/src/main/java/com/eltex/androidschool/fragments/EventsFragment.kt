package com.eltex.androidschool.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.EventsAdapter
import com.eltex.androidschool.adapter.OffsetDecoration
import com.eltex.androidschool.databinding.FragmentEventsBinding
import com.eltex.androidschool.db.AppDb
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.repository.SQLiteEventRepository
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                initializer {
                    EventViewModel(
                        SQLiteEventRepository(
                            AppDb.getInstance(
                                requireContext().applicationContext
                            ).eventsDao
                        )
                    )
                }
            }
        }

        val binding = FragmentEventsBinding.inflate(inflater, container, false)

        val eventsAdapter = EventsAdapter(
            object : EventsAdapter.EventListener {
                override fun onLikeClickListener(event: Event) {
                    viewModel.likeById(event.id)
                }

                override fun onShareClickListener(event: Event) {
                    val intent = Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, event.content)
                        .setType("text/plain")

                    val chooser = Intent.createChooser(intent, null)
                    startActivity(chooser)
                }

                override fun onDeleteClickListener(event: Event) {
                    viewModel.deleteById(event.id)
                }

                override fun onEditClickListener(event: Event) {
                    requireParentFragment()
                        .requireParentFragment()
                        .findNavController()
                        .navigate(
                            R.id.action_bottomNavigationFragment_to_newEventFragment,
                            bundleOf(
                                NewEventFragment.ARG_ID to event.id,
                                NewEventFragment.ARG_CONTENT to event.content,
                            )
                        )

                }

                override fun onParticipateClickListener(event: Event) {
                    viewModel.participateById(event.id)
                }

            }
        )

        binding.list.adapter = eventsAdapter

        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                eventsAdapter.submitList(it.events)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}