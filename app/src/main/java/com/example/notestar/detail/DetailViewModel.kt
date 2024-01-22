package com.example.notestar.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.notestar.models.Notes
import com.example.notestar.repository.StorageRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: StorageRepository
) : ViewModel() {
    var detailUIState by mutableStateOf(DetailUIState())
        private set

    private val hasUser: Boolean
        get() = repository.hasUser

    private val user: FirebaseUser?
        get() = repository.currentUser

    fun onColorChange(colorIndex: Int) {
        detailUIState = detailUIState.copy(colorIndex = colorIndex)
    }

    fun onTitleChange(title: String) {
        detailUIState = detailUIState.copy(title = title)
    }

    fun onNoteChange(note: String) {
        detailUIState = detailUIState.copy(note = note)
    }

    fun addNote() {
        if (hasUser) {
            repository.addNote(
                userId = user?.uid ?: "Invalid Id",
                title = detailUIState.title,
                description = detailUIState.note,
                color = detailUIState.colorIndex,
                timestamp = Timestamp.now()
            ) {
                detailUIState = detailUIState.copy(noteAddedStatus = it)
            }
        }
    }

    fun setEditFields(note: Notes) {
        detailUIState = detailUIState.copy(
            colorIndex = note.colorIndex,
            title = note.title,
            note = note.description
        )
    }

    fun getNote(noteId: String) {
        repository.getNote(
            noteId = noteId,
            onError = {},
        ) {
            detailUIState = detailUIState.copy(selectedNote = it)
            detailUIState.selectedNote?.let { it1 -> setEditFields(it1) }
        }
    }

    fun updateNote(
        noteId: String
    ) {
        repository.updateNote(
            title = detailUIState.title,
            note = detailUIState.note,
            noteId = noteId,
            color = detailUIState.colorIndex
        ) {
            detailUIState = detailUIState.copy(updateNoteStatus = it)
        }
    }

    fun resetNoteAddedStatus() {
        detailUIState = detailUIState.copy(
            noteAddedStatus = false,
            updateNoteStatus = false,
        )
    }

    fun resetState() {
        detailUIState = DetailUIState()
    }

}
