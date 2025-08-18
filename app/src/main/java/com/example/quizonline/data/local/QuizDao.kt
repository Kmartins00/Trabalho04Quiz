package com.example.quizonline.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizonline.data.model.QuizModel
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quizzes: List<QuizModel>)

    @Query("SELECT * FROM quizzes")
    fun getAllQuizzes(): Flow<List<QuizModel>>

    @Query("SELECT * FROM quizzes")
    suspend fun getAllQuizzesOnce(): List<QuizModel>
}
