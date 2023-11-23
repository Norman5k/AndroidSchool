package com.eltex.androidschool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eltex.androidschool.databinding.ActivityMainBinding
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.utils.toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var post = Post(
            id = 1L,
            content = "Слушайте, а как вы относитесь к тому, чтобы собраться большой компанией и поиграть в настолки? У меня есть несколько клевых игр, можем устроить вечер настолок! Пишите в лс или звоните",
            author = "Lydia Westervelt",
            published = "11.05.22 11:21",
            likedByMe = false,
        )

        bindPost(binding, post)

        binding.like.setOnClickListener {
            post = post.copy(likedByMe = !post.likedByMe)

            bindPost(binding, post)
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