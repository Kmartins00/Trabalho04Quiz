
package com.example.quizonline.data.repository

import android.util.Log
import com.example.quizonline.data.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun fetchRanking(
        onSuccess: (List<UserModel>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("users")
            .orderBy("totalScore", Query.Direction.DESCENDING)
            .limit(100)
            .get()
            .addOnSuccessListener { documents ->
                val userList = documents.mapNotNull { it.toObject(UserModel::class.java) }
                Log.d("UserRepository", "Ranking fetched successfully: ${userList.size} users.")
                onSuccess(userList)
            }
            .addOnFailureListener { exception ->
                Log.e("UserRepository", "Error fetching ranking", exception)
                onFailure(exception)
            }
    }
    fun observeCurrentUser(): Flow<UserModel?> = callbackFlow {
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            trySend(null)
            awaitClose { }
            return@callbackFlow
        }

        val userRef = firestore.collection("users").document(firebaseUser.uid)


        val listener = userRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("UserRepository", "Erro ao observar o utilizador", error)
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(UserModel::class.java)
                Log.d("UserRepository", "Dados do utilizador atualizados em tempo real: ${user?.name}")
                trySend(user)
            } else {
                trySend(null)
            }
        }

        awaitClose {
            listener.remove()
        }
    }

    fun logout() {
        auth.signOut()
    }
}
