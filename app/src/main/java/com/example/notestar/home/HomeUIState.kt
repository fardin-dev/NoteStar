package com.example.notestar.home

import com.example.notestar.models.Notes
import com.example.notestar.repository.Resources

data class HomeUIState(
    val notesList: Resources<List<Notes>> = Resources.Loading(),
    val noteDeletedStatus: Boolean = false
)