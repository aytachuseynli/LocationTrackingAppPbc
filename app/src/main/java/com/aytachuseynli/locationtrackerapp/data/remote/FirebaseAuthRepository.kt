package com.aytachuseynli.locationtrackerapp.data.remote

import com.aytachuseynli.locationtrackerapp.domain.model.AuthState
import com.aytachuseynli.locationtrackerapp.domain.model.User
import com.aytachuseynli.locationtrackerapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override val authState: Flow<AuthState> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if (user != null) {
                trySend(AuthState.Authenticated(user.toUser()))
            } else {
                trySend(AuthState.Unauthenticated)
            }
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    override val currentUser: User?
        get() = firebaseAuth.currentUser?.toUser()

    override suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                Result.success(it.toUser())
            } ?: Result.failure(Exception("Sign in failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(email: String, password: String, displayName: String): Result<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                // Update display name
                val profileUpdates = userProfileChangeRequest {
                    this.displayName = displayName
                }
                firebaseUser.updateProfile(profileUpdates).await()

                // Create user document in Firestore
                val userDoc = hashMapOf(
                    "uid" to firebaseUser.uid,
                    "email" to email,
                    "displayName" to displayName,
                    "createdAt" to System.currentTimeMillis()
                )
                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .set(userDoc)
                    .await()

                Result.success(firebaseUser.toUser().copy(displayName = displayName))
            } ?: Result.failure(Exception("Sign up failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser
                ?: return Result.failure(Exception("No user logged in"))

            val uid = user.uid

            // Delete user's locations from Firestore
            val locationsRef = firestore.collection("locations")
                .whereEqualTo("userId", uid)
                .get()
                .await()

            for (doc in locationsRef.documents) {
                doc.reference.delete().await()
            }

            // Delete user document from Firestore
            firestore.collection("users")
                .document(uid)
                .delete()
                .await()

            // Delete Firebase Auth account
            user.delete().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    private fun FirebaseUser.toUser(): User {
        return User(
            uid = uid,
            email = email ?: "",
            displayName = displayName
        )
    }
}
