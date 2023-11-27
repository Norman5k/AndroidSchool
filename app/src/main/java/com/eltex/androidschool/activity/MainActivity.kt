package com.eltex.androidschool.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.ActivityMainBinding
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.repository.InMemoryPostRepository
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.PostViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<PostViewModel> {
            viewModelFactory {
                initializer {
                    PostViewModel(InMemoryPostRepository())
                }
            }
        }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.state.flowWithLifecycle(lifecycle)
            .onEach {
                bindPost(binding, it.post)
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
    }

    private fun bindPost(
        binding: ActivityMainBinding,
        post: Post
    ) {
        binding.content.text = post.content
        binding.author.text = post.author
        binding.published.text = post.published
        binding.initial.text = post.author.take(1)
        binding.like.setIconResource(
            if (post.likedByMe) {
                R.drawable.baseline_favorite_24
            } else {
                R.drawable.baseline_favorite_border_24
            }
        )
        binding.like.text = if (post.likedByMe) {
            1
        } else {
            0
        }.toString()
    }
}