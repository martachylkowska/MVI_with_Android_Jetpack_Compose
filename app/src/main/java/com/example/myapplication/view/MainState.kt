package com.example.myapplication.view

import com.example.myapplication.model.Animal

sealed class MainState {

    object Idle : MainState()
    object Loading : MainState()
    data class Animals(val animals: List<Animal>) : MainState()
    data class Error(val error: String?) : MainState()

}