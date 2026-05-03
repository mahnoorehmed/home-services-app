package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.ServiceItem
import com.example.myapplication.data.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ServiceListState {
    object Idle : ServiceListState()
    object Loading : ServiceListState()
    data class Success(val services: List<ServiceItem>) : ServiceListState()
    data class Error(val message: String) : ServiceListState()
}

class ServiceViewModel : ViewModel() {
    private val repository = ServiceRepository()

    private val _serviceListState = MutableStateFlow<ServiceListState>(ServiceListState.Idle)
    val serviceListState: StateFlow<ServiceListState> = _serviceListState

    fun fetchProvidersByCategory(category: String) {
        _serviceListState.value = ServiceListState.Loading
        viewModelScope.launch {
            val result = repository.getProvidersByCategory(category)
            result.onSuccess { services ->
                _serviceListState.value = ServiceListState.Success(services)
            }.onFailure { e ->
                _serviceListState.value = ServiceListState.Error(e.message ?: "Failed to load services")
            }
        }
    }

    fun resetState() {
        _serviceListState.value = ServiceListState.Idle
    }
}
