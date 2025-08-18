package com.example.quizonline.data.repository

import android.util.Log
import com.example.quizonline.data.local.HistoryDao
import com.example.quizonline.data.model.QuizResultModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class HistoryRepository(private val historyDao: HistoryDao) {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun saveQuizResult(result: QuizResultModel) {
        val currentUser = auth.currentUser ?: return

        try {
            val batch = firestore.batch()

            val historyRef = firestore.collection("history").document()

            val finalResult = result.copy(id = historyRef.id, userId = currentUser.uid)

            batch.set(historyRef, finalResult)

            val userRef = firestore.collection("users").document(currentUser.uid)
            batch.update(userRef, "totalScore", FieldValue.increment(result.score.toLong()))

            batch.commit().await()
            Log.d("HistoryRepository", "Resultado do quiz salvo e pontuação atualizada.")

            historyDao.insertAll(listOf(finalResult))

        } catch (e: Exception) {
            Log.e("HistoryRepository", "Erro ao salvar o resultado do quiz", e)
        }
    }

    suspend fun fetchHistory(): List<QuizResultModel> {
        val currentUser = auth.currentUser ?: return emptyList()
        val userId = currentUser.uid

        syncHistory()
        val localHistory = historyDao.getHistoryForUser(userId)
        if(localHistory.isNotEmpty()){
            return localHistory
        }

        return fetchHistoryFromFirestore()
    }

    private suspend fun syncHistory() {
        try {
            val remoteHistory = fetchHistoryFromFirestore()
            if (remoteHistory.isNotEmpty()) {
                historyDao.insertAll(remoteHistory)
                Log.d("HistoryRepository", "Histórico sincronizado com o Firestore.")
            }
        } catch (e: Exception) {
            Log.e("HistoryRepository", "Erro ao sincronizar o histórico", e)
        }
    }

    private suspend fun fetchHistoryFromFirestore(): List<QuizResultModel> {
        val currentUser = auth.currentUser ?: return emptyList()
        val userId = currentUser.uid
        return try {
            val documents = firestore.collection("history")
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            val results = documents.mapNotNull { it.toObject(QuizResultModel::class.java) }
            Log.d("HistoryRepository", "Histórico buscado do Firestore: ${results.size} itens.")
            results
        } catch (e: Exception) {
            Log.e("HistoryRepository", "Erro ao buscar histórico do Firestore", e)
            emptyList()
        }
    }
}
