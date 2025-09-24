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
class RegistrationViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _registerState = MutableStateFlow<UiState>(UiState.Empty)
    val registerState: StateFlow<UiState> = _registerState

    fun onEmailChange(newEmail: String) { _email.value = newEmail }
    fun onPasswordChange(newPassword: String) { _password.value = newPassword }
    fun onConfirmPasswordChange(newPassword: String) { _confirmPassword.value = newPassword }

    fun register() {
        if (_email.value.isEmpty() || _password.value.isEmpty() || _confirmPassword.value.isEmpty()) {
            _registerState.value = UiState.Error("All fields are required")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()) {
            _registerState.value = UiState.Error("Invalid email format")
            return
        }

        if (_password.value.length < 6) {
            _registerState.value = UiState.Error("Password too short")
            return
        }

        if (_password.value != _confirmPassword.value) {
            _registerState.value = UiState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            _registerState.value = UiState.Loading
            try {
                val existingUser = repository.getUserByEmail(_email.value)
                if (existingUser != null) {
                    _registerState.value = UiState.Error("User already exists")
                } else {
                    repository.registerUser(_email.value, _password.value)
                    _registerState.value = UiState.Success("Registration Successful")
                }
            } catch (e: Exception) {
                _registerState.value = UiState.Error("Unexpected error: ${e.message}")
            }
        }
    }
}
