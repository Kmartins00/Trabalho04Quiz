package com.example.quizonline.data.repository

import com.example.quizonline.data.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun signUp(name: String, email: String, password: String) {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = authResult.user ?: throw Exception("Falha ao criar utilizador.")

        val userModel = UserModel(
            name = name,
            email = email,
            totalScore = 0 // Pontuação inicial é sempre 0
        )

        firestore.collection("users").document(firebaseUser.uid).set(userModel).await()
    }

    // TODO: Adicionar aqui a função de login no futuro
}
