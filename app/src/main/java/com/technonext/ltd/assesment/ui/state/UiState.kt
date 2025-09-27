package com.technonext.ltd.assesment.ui.state

sealed class UiState {
    object Loading : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
    object Empty : UiState()
}