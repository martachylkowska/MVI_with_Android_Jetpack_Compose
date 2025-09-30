package com.example.myapplication.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.api.AnimalApi
import com.example.myapplication.api.AnimalRepo

class ViewModelFactory(private val api: AnimalApi) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(AnimalRepo(api)) as T
        } else {
            throw IllegalArgumentException("Unknows class name")
        }
    }
}