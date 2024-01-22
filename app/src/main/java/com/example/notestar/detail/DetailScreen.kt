package com.example.notestar.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notestar.Utils

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel = hiltViewModel(),
    noteId: String,
    onNavigate: () -> Unit,
) {

    val detailUIState = detailViewModel.detailUIState ?: DetailUIState()

    val isFormsNotBlank = detailUIState.note.isNotBlank() &&
            detailUIState.title.isNotBlank()

    val selectedColor by animateColorAsState(
        targetValue = Utils.colors[detailUIState.colorIndex], label = "selected color"
    )

    val isNoteIdNotBlank = noteId.isNotBlank()

    val icon = if (isNoteIdNotBlank) Icons.Default.Refresh
    else Icons.Default.Check

    LaunchedEffect(key1 = Unit) {
        if (isFormsNotBlank) {
            detailViewModel.getNote(noteId)
        } else {
            detailViewModel.resetState()
        }
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (isNoteIdNotBlank) {
                    detailViewModel.updateNote(noteId)
                } else {
                    detailViewModel.addNote()
                }
            }) {
                Icon(imageVector = icon, contentDescription = "fab")
            }
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = selectedColor)
                .padding(padding)
        ) {

        }

    }


}