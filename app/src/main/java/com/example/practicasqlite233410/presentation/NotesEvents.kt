package com.example.practicasqlite233410.presentation

import com.example.practicasqlite233410.data.Note

sealed interface NotesEvent {
    object SortNotes: NotesEvent

    data class DeleteNote(val note: Note): NotesEvent

    data class SaveNote(
        val title: String,
        val description: String
    ): NotesEvent

    data class EditNote(
        val id: Int,
        val title: String,
        val description: String,
    ): NotesEvent

}