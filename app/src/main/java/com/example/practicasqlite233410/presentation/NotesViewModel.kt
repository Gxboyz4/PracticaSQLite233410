package com.example.practicasqlite233410.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicasqlite233410.data.Note
import com.example.practicasqlite233410.data.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.practicasqlite233410.data.Validation
import android.content.Context
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.channels.produce

class NotesViewModel(
    private val dao: NoteDao,
    private val context: Context
) : ViewModel() {
    private val validation = Validation(context)
    private val isSortedByDateAdded = MutableStateFlow(true)

    private var notes =
        isSortedByDateAdded.flatMapLatest { sort ->
            if (sort) {
                dao.getNotesOrderdByDateAdded()
            } else {
                dao.getNotesOrderdByTitle()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state = MutableStateFlow(NoteState())
    val state =
        combine(_state, isSortedByDateAdded, notes) { state, isSortedByDateAdded, notes ->
            state.copy(
                notes = notes
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch{
                    dao.deleteNote(event.note)
                }
            }

            is NotesEvent.SaveNote -> {
                val title = state.value.title.value
                val description = state.value.description.value
                if(validation.validateTitle(title) and validation.validateDescription(description)){
                    val note = Note(
                        title,
                        description,
                        dateAdded = System.currentTimeMillis(),
                    )
                    viewModelScope.launch {
                        dao.upsertNote(note)
                    }
                    _state.update {
                        it.copy(
                            title = mutableStateOf(""),
                            description = mutableStateOf("")
                        )
                    }
                }
            }
            is NotesEvent.EditNote ->{
                val id = state.value.id.value
                val title = state.value.title.value
                val description = state.value.description.value
                if(validation.validateTitle(title) and validation.validateDescription(description)){
                    val note = Note(
                        title,
                        description,
                        dateAdded = System.currentTimeMillis(),
                        id
                    )

                    viewModelScope.launch {
                        dao.updateNote(note)
                    }
                    _state.update {
                        it.copy(
                            title = mutableStateOf(""),
                            description = mutableStateOf("")
                        )
                    }
                }

            }
            NotesEvent.SortNotes -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
        }
    }
    private fun showDeleteConfirmationDialog(context: Context, note: Note) {
        AlertDialog.Builder(context)
            .setMessage("Do you want to delete this note?")
            .setPositiveButton("Yes") { dialog, id ->

            }
            .setNegativeButton("No") { dialog, id ->
                // No hacer nada, simplemente cerrar el diálogo
            }
            .create()
            .show()
    }

}