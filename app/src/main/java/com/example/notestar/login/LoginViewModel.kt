package com.example.notestar.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notestar.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val currentUser = repository.currentUser
    val hasUser: Boolean get() = repository.hasUser()
    private var loginUIState by mutableStateOf(LoginUIState())

    fun onUserNameChange(userName: String) {
        loginUIState = loginUIState.copy(userName = userName)
    }

    fun onPasswordNameChange(password: String) {
        loginUIState = loginUIState.copy(password = password)
    }

    fun onUserNameChangeSignup(userName: String) {
        loginUIState = loginUIState.copy(userNameSignUp = userName)
    }

    fun onPasswordChangeSignup(password: String) {
        loginUIState = loginUIState.copy(passwordSignUp = password)
    }

    fun onConfirmPasswordChange(password: String) {
        loginUIState = loginUIState.copy(confirmPasswordSignUp = password)
    }

    private fun validateLoginForm() =
        loginUIState.userName.isNotBlank() &&
                loginUIState.password.isNotBlank()

    private fun validateSignupForm() =
        loginUIState.userNameSignUp.isNotBlank() &&
                loginUIState.passwordSignUp.isNotBlank() &&
                loginUIState.confirmPasswordSignUp.isNotBlank()
}