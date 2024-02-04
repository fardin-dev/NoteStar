package com.example.notestar.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notestar.repository.Resources
import com.example.notestar.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: StorageRepository
) : ViewModel() {

    var homeUIState by mutableStateOf(HomeUIState())
        private set

    val user = repository.currentUser()

    private val hasUser: Boolean
        get() = repository.hasUser

    private val userId: String
        get() = repository.getUserId()

    fun loadNotes() {
        if (hasUser) {
            if (userId.isNotBlank()) {
                getUserNotes(userId)
            }
        } else {
            homeUIState = homeUIState.copy(
                notesList = Resources.Error(
                    throwable = Throwable(message = "User is not Logged in")
                )
            )
        }
    }

    private fun getUserNotes(userId: String) = viewModelScope.launch {
        repository.getUserNotes(userId).collect {
            homeUIState = homeUIState.copy(notesList = it)
        }
    }

    fun deleteNote(noteId: String) = repository.deleteNote(noteId) { isDeletedSuccessfully ->
        homeUIState = homeUIState.copy(noteDeletedStatus = isDeletedSuccessfully)
    }

    fun signOut() = repository.signOut()

}