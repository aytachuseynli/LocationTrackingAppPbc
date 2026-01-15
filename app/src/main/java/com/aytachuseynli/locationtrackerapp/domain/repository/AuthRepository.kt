package com.aytachuseynli.locationtrackerapp.domain.repository

import com.aytachuseynli.locationtrackerapp.domain.model.AuthState
import com.aytachuseynli.locationtrackerapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<AuthState>
    val currentUser: User?

    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signUp(email: String, password: String, displayName: String): Result<User>
    suspend fun signOut()
    suspend fun deleteAccount(): Result<Unit>
    fun isUserLoggedIn(): Boolean
}
