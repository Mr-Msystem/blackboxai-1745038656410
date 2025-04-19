package com.example.notepadapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notepadapp.databinding.ItemTodoBinding

class TodoAdapter(private val onItemClicked: (Todo) -> Unit) :
    ListAdapter<Todo, TodoAdapter.TodoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = getItem(position)
        holder.bind(currentTodo)
    }

    inner class TodoViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val todo = getItem(position)
                    onItemClicked(todo)
                }
            }
        }

        fun bind(todo: Todo) {
            binding.textViewTask.text = todo.task
            binding.checkBoxDone.isChecked = todo.isDone
            binding.checkBoxDone.setOnCheckedChangeListener { _, isChecked ->
                val updatedTodo = todo.copy(isDone = isChecked)
                onItemClicked(updatedTodo)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
        }
    }
}
