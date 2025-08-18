package com.example.quizonline.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizonline.data.model.QuizResultModel

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(results: List<QuizResultModel>)

    @Query("SELECT * FROM history WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getHistoryForUser(userId: String): List<QuizResultModel>
}