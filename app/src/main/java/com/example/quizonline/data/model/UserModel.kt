package com.example.quizonline.data.model

data class UserModel(
    val name: String,
    val email: String,
    val totalScore: Int
) {
    constructor() : this("", "", 0)
}