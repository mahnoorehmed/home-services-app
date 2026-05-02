package com.example.myapplication.data.repository

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Holds the verification ID temporarily
    var verificationId: String = ""
        private set

    /**
     * Send OTP to the given phone number
     */
    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        onCodeSent: (String) -> Unit,
        onVerificationFailed: (Exception) -> Unit,
        onVerificationCompleted: (PhoneAuthCredential) -> Unit
    ) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                onVerificationCompleted(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                onVerificationFailed(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@AuthRepository.verificationId = verificationId
                onCodeSent(verificationId)
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)             // Activity (for callback binding)
            .setCallbacks(callbacks)           // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /**
     * Sign in with the provided OTP
     */
    suspend fun signInWithOtp(otp: String): Result<String> {
        return try {
            if (verificationId.isEmpty()) {
                return Result.failure(Exception("Verification ID is missing."))
            }
            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            val authResult = auth.signInWithCredential(credential).await()
            val uid = authResult.user?.uid
            if (uid != null) {
                Result.success(uid)
            } else {
                Result.failure(Exception("Failed to get user ID after sign-in."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Check if user exists in the 'users' collection and determine their role and status.
     * Returns a Pair of (Role?, Status?). Null if not found.
     */
    suspend fun checkUserRoleAndStatus(uid: String): Pair<String, String>? {
        return try {
            val document = db.collection("users").document(uid).get().await()
            if (document.exists()) {
                val role = document.getString("role") ?: "customer"
                val status = document.getString("status") ?: "active"
                Pair(role, status)
            } else {
                null
            }
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Register a new service provider in the 'users' collection
     */
    suspend fun registerServiceProvider(
        uid: String,
        phone: String,
        name: String,
        serviceCategories: String
    ): Result<Unit> {
        return try {
            val providerData = hashMapOf(
                "role" to "provider",
                "status" to "pending",
                "phone" to phone,
                "name" to name,
                "serviceCategory" to serviceCategories,
                "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )
            db.collection("users").document(uid).set(providerData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
