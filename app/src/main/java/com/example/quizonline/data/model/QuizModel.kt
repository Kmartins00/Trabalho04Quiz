package com.example.quizonline.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
@Entity(tableName = "quizzes")
data class QuizModel(
    @PrimaryKey val id : String,
    val title : String,
    val subtitle : String,
    val time : String,
    val questionList : List<QuestionModel>
): Parcelable {
    constructor() : this("","","","",emptyList())
}

@Parcelize
data class QuestionModel(
    val question : String,
    val options : List<String>,
    val correct : String,
): Parcelable {
    constructor() : this("", emptyList(),"")
}


