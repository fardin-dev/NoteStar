package com.example.notestar.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notestar.Utils
import com.example.notestar.ui.theme.NoteStarTheme

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
        if (isNoteIdNotBlank) {
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
            AnimatedVisibility(visible = isFormsNotBlank) {
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
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = selectedColor)
                .padding(padding)
        ) {
            if (detailUIState.noteAddedStatus) {
                LaunchedEffect(key1 = Unit) {
                    snackbarHostState.showSnackbar(
                        message = "Added Note Successfully",
                        duration = SnackbarDuration.Short
                    )
                    detailViewModel.resetNoteAddedStatus()
                    onNavigate.invoke()
                }
            }

            if (detailUIState.updateNoteStatus) {
                LaunchedEffect(key1 = Unit) {
                    snackbarHostState.showSnackbar(
                        message = "Note Updated Successfully",
                        duration = SnackbarDuration.Short
                    )
                    detailViewModel.resetNoteAddedStatus()
                    onNavigate.invoke()
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                contentPadding = PaddingValues(
                    vertical = 16.dp,
                    horizontal = 8.dp
                )
            ) {
                itemsIndexed(Utils.colors){ colorIndex, color ->
                    ColorItem(color = color) {
                        detailViewModel.onColorChange(colorIndex = colorIndex)
                    }
                }
            }

            OutlinedTextField(
                value = detailUIState.title,
                onValueChange = {
                    detailViewModel.onTitleChange(it)
                },
                label = { Text(text = "Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            OutlinedTextField(
                value = detailUIState.note,
                onValueChange = { detailViewModel.onNoteChange(it) },
                label = { Text(text = "Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .weight(1f)
            )
        }

    }


}

@Composable
fun ColorItem(
    color: Color,
    onClick: () -> Unit
) {
    Surface (
        color = color,
        shape = CircleShape,
        modifier = Modifier
            .padding(8.dp)
            .size(36.dp)
            .clickable {
                onClick.invoke()
            },
        border = BorderStroke(2.dp, color = Color.Black)
    ) {

    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevDetailScreen() {
    NoteStarTheme {
        DetailScreen(noteId = "101") {
        }
    }
}