package com.eltex.androidschool.model

sealed interface PagingModel<out T> {
    data class Data<T>(val value: T) : PagingModel<T>
    data class Progress<T>(val value: T) : PagingModel<T>
    data class Error(val reason: Throwable) : PagingModel<Nothing>
}