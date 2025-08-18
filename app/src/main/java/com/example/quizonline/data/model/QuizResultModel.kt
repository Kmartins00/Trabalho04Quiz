package com.example.quizonline.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class QuizResultModel(
    @PrimaryKey val id: String,
    val quizId: String,
    val userId: String,
    val score: Int,
    val totalQuestions: Int,
    val timestamp: Long
){

    constructor() : this("", "", "", 0, 0, 0L)
}
