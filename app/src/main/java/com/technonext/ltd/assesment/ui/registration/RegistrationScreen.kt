package com.technonext.ltd.assesment.ui.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.technonext.ltd.assesment.viewmodel.RegistrationViewModel
import com.technonext.ltd.assesment.viewmodel.UiState

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val state by viewModel.registerState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(email, onValueChange = viewModel::onEmailChange, label = { Text("Email") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(password, onValueChange = viewModel::onPasswordChange, label = { Text("Password") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(confirmPassword, onValueChange = viewModel::onConfirmPasswordChange, label = { Text("Confirm Password") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.register() }, modifier = Modifier.fillMaxWidth()) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {
                Text((state as UiState.Success).message, color = Color.Green)
                LaunchedEffect(Unit) { onRegisterSuccess() }
            }
            is UiState.Error -> Text((state as UiState.Error).message, color = Color.Red)
            UiState.Empty -> {}
        }
    }
}
