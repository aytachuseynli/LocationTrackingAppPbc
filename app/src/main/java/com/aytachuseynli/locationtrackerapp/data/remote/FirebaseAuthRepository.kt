package com.aytachuseynli.locationtrackerapp.data.remote

import com.aytachuseynli.locationtrackerapp.domain.model.AuthState
import com.aytachuseynli.locationtrackerapp.domain.model.User
import com.aytachuseynli.locationtrackerapp.domain.repository.AuthRepository
import com.aytachuseynli.locationtrackerapp.util.safeCall
import com.aytachuseynli.locationtrackerapp.util.safeCallWithMapping
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

    private companion object {
        const val USERS_COLLECTION = "users"
        const val LOCATIONS_COLLECTION = "locations"
        const val FIELD_USER_ID = "userId"
    }

    // region Auth State
    override val authState: Flow<AuthState> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser.toAuthState())
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override val currentUser: User?
        get() = firebaseAuth.currentUser?.toUser()

    override fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null
    // endregion

    // region Authentication
    override suspend fun signIn(email: String, password: String): Result<User> =
        safeCallWithMapping(
            block = { firebaseAuth.signInWithEmailAndPassword(email, password).await().user?.toUser() },
            errorMessage = { "Sign in failed" }
        )

    override suspend fun signUp(email: String, password: String, displayName: String): Result<User> =
        safeCallWithMapping(
            block = { createUserWithProfile(email, password, displayName) },
            errorMessage = { "Sign up failed" }
        )

    override suspend fun signOut() = firebaseAuth.signOut()

    override suspend fun deleteAccount(): Result<Unit> = safeCall {
        val user = requireCurrentUser()
        deleteUserData(user.uid)
        user.delete().await()
    }
    // endregion

    // region Private Helpers
    private suspend fun createUserWithProfile(
        email: String,
        password: String,
        displayName: String
    ): User? {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return authResult.user?.apply {
            updateDisplayName(displayName)
            saveUserToFirestore(uid, email, displayName)
        }?.toUser()?.copy(displayName = displayName)
    }

    private suspend fun FirebaseUser.updateDisplayName(displayName: String) {
        updateProfile(userProfileChangeRequest { this.displayName = displayName }).await()
    }

    private suspend fun saveUserToFirestore(uid: String, email: String, displayName: String) {
        val userData = mapOf(
            "uid" to uid,
            "email" to email,
            "displayName" to displayName,
            "createdAt" to System.currentTimeMillis()
        )
        firestore.collection(USERS_COLLECTION).document(uid).set(userData).await()
    }

    private suspend fun deleteUserData(uid: String) {
        deleteUserLocations(uid)
        deleteUserDocument(uid)
    }

    private suspend fun deleteUserLocations(uid: String) {
        firestore.collection(LOCATIONS_COLLECTION)
            .whereEqualTo(FIELD_USER_ID, uid)
            .get()
            .await()
            .documents
            .forEach { it.reference.delete().await() }
    }

    private suspend fun deleteUserDocument(uid: String) {
        firestore.collection(USERS_COLLECTION).document(uid).delete().await()
    }

    private fun requireCurrentUser(): FirebaseUser =
        firebaseAuth.currentUser ?: throw IllegalStateException("No user logged in")

    private fun FirebaseUser?.toAuthState(): AuthState =
        this?.let { AuthState.Authenticated(it.toUser()) } ?: AuthState.Unauthenticated

    private fun FirebaseUser.toUser(): User = User(
        uid = uid,
        email = email.orEmpty(),
        displayName = displayName
    )
    // endregion
}
