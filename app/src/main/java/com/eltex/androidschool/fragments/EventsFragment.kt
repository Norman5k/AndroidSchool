package com.eltex.androidschool.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.EventsAdapter
import com.eltex.androidschool.adapter.OffsetDecoration
import com.eltex.androidschool.databinding.FragmentEventsBinding
import com.eltex.androidschool.mapper.EventPagingModelMapper
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.utils.getText
import com.eltex.androidschool.viewmodel.EventViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EventsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventsBinding.inflate(inflater, container, false)

        val viewModel by viewModels<EventViewModel>()

        val eventsAdapter = EventsAdapter(
            object : EventsAdapter.EventListener {
                override fun onLikeClickListener(event: EventUiModel) {
                    viewModel.accept(EventMessage.Like(event))
                }

                override fun onShareClickListener(event: EventUiModel) {
                    val intent = Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, event.content)
                        .setType("text/plain")

                    val chooser = Intent.createChooser(intent, null)
                    startActivity(chooser)
                }

                override fun onDeleteClickListener(event: EventUiModel) {
                    viewModel.accept(EventMessage.Delete(event))
                }

                override fun onEditClickListener(event: EventUiModel) {
                    requireParentFragment()
                        .requireParentFragment()
                        .findNavController()
                        .navigate(
                            R.id.action_bottomNavigationFragment_to_editEventFragment,
                            bundleOf(
                                NewEventFragment.ARG_ID to event.id,
                                NewEventFragment.ARG_CONTENT to event.content,
                            )
                        )

                }

                override fun onParticipateClickListener(event: EventUiModel) {
                    viewModel.accept(EventMessage.Participate(event))
                }

                override fun onRetryClickListener() {
                    viewModel.accept(EventMessage.Retry)
                }

            }
        )

        binding.list.adapter = eventsAdapter

        binding.list.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener{

            override fun onChildViewAttachedToWindow(view: View) {
                val position = binding.list.getChildAdapterPosition(view)
                val adapterCount = eventsAdapter.itemCount

                if (position == adapterCount - 4 || position == adapterCount - 1) {
                    viewModel.accept(EventMessage.LoadNextPage)
                }
            }

            override fun onChildViewDetachedFromWindow(view: View) = Unit
        })

        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.accept(EventMessage.Refresh)
        }

        binding.retryButton.setOnClickListener {
            viewModel.accept(EventMessage.Refresh)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewEventFragment.EVENT_UPDATED, viewLifecycleOwner
        ) { _, _ ->
            viewModel.accept(EventMessage.Refresh)
        }

        val mapper = EventPagingModelMapper()

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                binding.swipeRefresh.isRefreshing = state.isRefreshing

                val emptyError = state.emptyError
                binding.errorGroup.isVisible = emptyError != null
                binding.errorText.text = emptyError?.getText(requireContext())

                state.singleError?.let {
                    Toast.makeText(
                        requireContext(),
                        it.getText(requireContext()),
                        Toast.LENGTH_SHORT
                    ).show()

                    viewModel.accept(EventMessage.HandleError)
                }

                eventsAdapter.submitList(mapper.map(state))
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}