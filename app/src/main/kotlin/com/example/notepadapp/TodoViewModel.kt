package com.example.notepadapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class TodoViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val todosCollection = db.collection("todos")

    private val _allTodos = MutableLiveData<List<Todo>>()
    val allTodos: LiveData<List<Todo>> = _allTodos

    private var listenerRegistration: ListenerRegistration? = null

    init {
        fetchTodos()
    }

    private fun fetchTodos() {
        listenerRegistration = todosCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val todos = snapshot.documents.map { doc ->
                    Todo(
                        id = doc.id,
                        task = doc.getString("task") ?: "",
                        isDone = doc.getBoolean("isDone") ?: false
                    )
                }
                _allTodos.value = todos
            }
        }
    }

    fun addTodo(todo: Todo) {
        todosCollection.add(
            mapOf(
                "task" to todo.task,
                "isDone" to todo.isDone
            )
        )
    }

    fun updateTodo(todo: Todo) {
        todosCollection.document(todo.id).set(
            mapOf(
                "task" to todo.task,
                "isDone" to todo.isDone
            )
        )
    }

    fun deleteTodo(todo: Todo) {
        todosCollection.document(todo.id).delete()
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
