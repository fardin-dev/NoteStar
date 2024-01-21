package com.example.notestar.repository

sealed class Resources<T> (
    val data: T? = null,
    val throwable: Throwable? = null
){
    class Loading<T>: Resources<T>()
    class Success<T>(data: T?): Resources<T>(data = data)
    class Error<T>(throwable: Throwable?): Resources<T>(throwable = throwable)
}