package com.example.myapplication.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.AuthRepository
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object CodeSent : AuthState()
    data class Authenticated(val uid: String) : AuthState()
    data class RoleDetermined(val role: String?, val status: String?) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // Hold the phone number for registration if needed
    var currentPhoneNumber: String = ""
        private set

    fun sendOtp(phoneNumber: String, activity: Activity) {
        _authState.value = AuthState.Loading
        currentPhoneNumber = phoneNumber
        repository.sendOtp(
            phoneNumber = phoneNumber,
            activity = activity,
            onCodeSent = {
                _authState.value = AuthState.CodeSent
            },
            onVerificationFailed = { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Verification failed")
            },
            onVerificationCompleted = { _ ->
                // For auto-retrieval
                // Not fully implemented in UI layer for edge cases yet, but could auto sign-in here
            }
        )
    }

    fun verifyOtp(otp: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = repository.signInWithOtp(otp)
            result.onSuccess { uid ->
                _authState.value = AuthState.Authenticated(uid)
                // Proceed to check role
                checkUserRole(uid)
            }.onFailure { e ->
                _authState.value = AuthState.Error(e.message ?: "Invalid OTP")
            }
        }
    }

    private fun checkUserRole(uid: String) {
        viewModelScope.launch {
            try {
                val roleAndStatus = repository.checkUserRoleAndStatus(uid)
                if (roleAndStatus != null) {
                    _authState.value = AuthState.RoleDetermined(roleAndStatus.first, roleAndStatus.second)
                } else {
                    // New user -> empty role
                    _authState.value = AuthState.RoleDetermined(null, null)
                }
            } catch (e: Exception) {
                 _authState.value = AuthState.Error(e.message ?: "Failed to check role")
            }
        }
    }

    fun registerProvider(name: String, serviceCategories: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    val result = repository.registerServiceProvider(
                        uid = uid,
                        phone = currentPhoneNumber,
                        name = name,
                        serviceCategories = serviceCategories
                    )
                    result.onSuccess {
                        // After registration, status is pending
                        _authState.value = AuthState.RoleDetermined("provider", "pending")
                    }.onFailure { e ->
                        _authState.value = AuthState.Error(e.message ?: "Registration failed")
                    }
                } else {
                    _authState.value = AuthState.Error("User not logged in")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }
    
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
