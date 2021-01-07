package me.andreandyp.androidtechnicalchallenge.network

sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class Error<out T>(val statusCode: Int) : NetworkResponse<T>()
    data class NetworkError<out T>(val message: T) : NetworkResponse<T>()
    object Loading : NetworkResponse<Nothing>()
}