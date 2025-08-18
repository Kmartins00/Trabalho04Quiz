package com.example.quizonline.data.local

import androidx.room.TypeConverter
import com.example.quizonline.data.model.QuestionModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromQuestionModelList(value: List<QuestionModel>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toQuestionModelList(value: String): List<QuestionModel> {
        val listType = object : TypeToken<List<QuestionModel>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}