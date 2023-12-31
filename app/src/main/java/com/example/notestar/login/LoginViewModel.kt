package com.example.notestar.login

import android.content.Context
import android.widget.Toast
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
    var loginUIState by mutableStateOf(LoginUIState())

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


    fun createUser(context: Context) = viewModelScope.launch {
        if (validateLoginForm()) {
            loginUIState = loginUIState.copy(isLoading = true)
            if (loginUIState.passwordSignUp == loginUIState.confirmPasswordSignUp) {
                loginUIState = loginUIState.copy(signUpError = null)
            }
            repository.createUser(
                email = loginUIState.userNameSignUp,
                password = loginUIState.passwordSignUp
            ) { isSuccess ->
                if (isSuccess) {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    loginUIState = loginUIState.copy(isSuccessLogin = true)
                } else {
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                    loginUIState = loginUIState.copy(isSuccessLogin = false)
                }
            }
        } else {
            Toast.makeText(context, "Login Form Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun loginUser(context: Context) = viewModelScope.launch {
        if (validateLoginForm()) {
            loginUIState = loginUIState.copy(isLoading = true)
            loginUIState = loginUIState.copy(loginError = null)
            repository.loginUser(
                email = loginUIState.userName,
                password = loginUIState.password
            ) { isSuccess ->
                if (isSuccess) {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    loginUIState = loginUIState.copy(isSuccessLogin = true)
                } else {
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                    loginUIState = loginUIState.copy(isSuccessLogin = false)
                }
            }
        } else {
            Toast.makeText(context, "Login Form Error", Toast.LENGTH_SHORT).show()
        }
    }


}