package com.example.notepadapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class NotesViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val notesCollection = db.collection("notes")

    private val _allNotes = MutableLiveData<List<Note>>()
    val allNotes: LiveData<List<Note>> = _allNotes

    private var listenerRegistration: ListenerRegistration? = null

    init {
        fetchNotes()
    }

    private fun fetchNotes() {
        listenerRegistration = notesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val notes = snapshot.documents.map { doc ->
                    Note(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        content = doc.getString("content") ?: ""
                    )
                }
                _allNotes.value = notes
            }
        }
    }

    fun addNote(note: Note) {
        notesCollection.add(
            mapOf(
                "title" to note.title,
                "content" to note.content
            )
        )
    }

    fun updateNote(note: Note) {
        notesCollection.document(note.id).set(
            mapOf(
                "title" to note.title,
                "content" to note.content
            )
        )
    }

    fun deleteNote(note: Note) {
        notesCollection.document(note.id).delete()
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
