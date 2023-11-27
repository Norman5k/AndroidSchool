package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class PostViewModel(private val repository: PostRepository) : ViewModel() {
    private val _state: MutableStateFlow<PostUiState> = MutableStateFlow(PostUiState())
    val state: StateFlow<PostUiState> = _state.asStateFlow()

    init {
        repository.getPost()
            .onEach { post ->
                _state.update {
                    it.copy(post = post)
                }
            }
            .launchIn(viewModelScope)
    }

    fun like() {
        repository.like()
    }
}