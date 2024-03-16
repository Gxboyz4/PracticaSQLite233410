package com.example.practicasqlite233410.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    //Editar nota existente
    @Update
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM note ORDER BY dateAdded")
    fun getNotesOrderdByDateAdded(): Flow<List<Note>>


    @Query("SELECT * FROM note ORDER BY title ASC")
    fun getNotesOrderdByTitle(): Flow<List<Note>>

}