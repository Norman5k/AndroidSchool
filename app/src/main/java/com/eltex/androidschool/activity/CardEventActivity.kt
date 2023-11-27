package com.eltex.androidschool.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.repository.InMemoryEventRepository
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CardEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                initializer {
                    EventViewModel(InMemoryEventRepository())
                }
            }
        }

        val binding = CardEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.state.flowWithLifecycle(lifecycle)
            .onEach {
                bindEvent(binding, it.event)
            }
            .launchIn(lifecycleScope)

        binding.like.setOnClickListener {
            viewModel.like()
        }

        binding.share.setOnClickListener {
            toast(R.string.not_implemented)
        }

        binding.menu.setOnClickListener {
            toast(R.string.not_implemented)
        }

        binding.participation.setOnClickListener {
            viewModel.participate()
        }
    }

    private fun bindEvent(
        binding: CardEventBinding,
        event: Event,
    ) {
        binding.content.text = event.content
        binding.author.text = event.author
        binding.published.text = event.published
        binding.initial.text = event.author.take(1)
        binding.like.setIconResource(
            if (event.likedByMe) {
                R.drawable.baseline_favorite_24
            } else {
                R.drawable.baseline_favorite_border_24
            }
        )
        binding.like.text = if (event.likedByMe) {
            1
        } else {
            0
        }.toString()
        binding.participation.text = if (event.participatedByMe) {
            1
        } else {
            0
        }.toString()
    }
}