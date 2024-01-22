package com.example.notestar.detail

import com.example.notestar.models.Notes

data class DetailUIState(
    val colorIndex: Int = 0,
    val title: String = "",
    val note: String = "",
    val noteAddedStatus: Boolean = false,
    val updateNoteStatus: Boolean = false,
    val selectedNote: Notes? = null,
)
