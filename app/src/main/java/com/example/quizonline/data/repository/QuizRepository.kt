package com.example.quizonline.data.repository

import android.util.Log
import com.example.quizonline.data.local.QuizDao
import com.example.quizonline.data.model.QuestionModel
import com.example.quizonline.data.model.QuizModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class QuizRepository(private val quizDao: QuizDao) {

    private val firestore = FirebaseFirestore.getInstance()


    fun getQuizzesFlow(): Flow<List<QuizModel>> {
        return quizDao.getAllQuizzes()
    }

    suspend fun getQuizzesOnce(): List<QuizModel> {
        return quizDao.getAllQuizzesOnce()
    }

    suspend fun refreshQuizzes() {
        try {
            Log.d("QuizRepository", "Iniciando sincronização de quizzes com o Firestore...")
            val firestoreQuizzes = fetchQuizzesFromFirestore()
            if (firestoreQuizzes.isNotEmpty()) {
                Log.d("QuizRepository", "Salvando/Atualizando ${firestoreQuizzes.size} quizzes no banco de dados local.")
                quizDao.insertAll(firestoreQuizzes)
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "Falha ao sincronizar com o Firestore.", e)
            throw e
        }
    }

    private suspend fun fetchQuizzesFromFirestore(): List<QuizModel> {
        return try {
            val quizDocuments = firestore.collection("quizzes").get().await()
            if (quizDocuments.isEmpty) {
                return emptyList()
            }

            val quizzesWithQuestions = mutableListOf<QuizModel>()
            for (quizDoc in quizDocuments) {
                val quiz = quizDoc.toObject<QuizModel>()
                val questionDocuments = firestore.collection("quizzes").document(quiz.id)
                    .collection("questions").get().await()

                val questions = questionDocuments.mapNotNull { it.toObject<QuestionModel>() }
                quizzesWithQuestions.add(quiz.copy(questionList = questions))
            }
            quizzesWithQuestions
        } catch (e: Exception) {
            Log.e("QuizRepository", "Erro ao buscar quizzes do Firestore", e)
            emptyList()
        }
    }
}
