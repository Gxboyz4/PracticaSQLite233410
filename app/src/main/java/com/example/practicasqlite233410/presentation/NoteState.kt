package com.example.practicasqlite233410.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.practicasqlite233410.data.Note

data class NoteState(
    val id: MutableState<Int> = mutableStateOf(0),
    val notes: List<Note> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val description: MutableState<String> = mutableStateOf("")

)