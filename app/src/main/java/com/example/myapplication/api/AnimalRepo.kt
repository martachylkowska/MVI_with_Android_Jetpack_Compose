package com.example.myapplication.api

class AnimalRepo(private val api: AnimalApi) {

    suspend fun getAnimals() = api.getAnimals()
}