package com.technonext.ltd.assesment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technonext.ltd.assesment.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    // Mutable state inside ViewModel
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    fun onEmailChange(newEmail: String) { _email.value = newEmail }
    fun onPasswordChange(newPassword: String) { _password.value = newPassword }

    // UI state for authentication
    private val _loginState = MutableStateFlow<UiState>(UiState.Empty)
    val loginState: StateFlow<UiState> = _loginState

    fun login() {
        if (_email.value.isEmpty() || _password.value.isEmpty()) {
            _loginState.value = UiState.Error("Fields cannot be empty")
        } else {
            viewModelScope.launch {
                _loginState.value = UiState.Loading
                try {
                    val user = repository.getUserByEmail(_email.value)
                    if (user != null && user.password == _password.value) {
                        _loginState.value = UiState.Success("Login Successful")
                    } else {
                        _loginState.value = UiState.Error("Invalid credentials")
                    }
                } catch (e: Exception) {
                    _loginState.value = UiState.Error("Unexpected error: ${e.message}")
                }
            }
        }
    }
}
