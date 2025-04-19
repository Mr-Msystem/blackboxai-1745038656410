package com.example.notepadapp

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.notepadapp.databinding.DialogAddEditTodoBinding

class AddEditTodoDialogFragment(private val todo: Todo? = null) : DialogFragment() {

    private var _binding: DialogAddEditTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TodoViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddEditTodoBinding.inflate(LayoutInflater.from(context))

        viewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]

        if (todo != null) {
            binding.editTextTask.setText(todo.task)
            binding.checkBoxDone.isChecked = todo.isDone
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(if (todo == null) "Add Todo" else "Edit Todo")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                val task = binding.editTextTask.text.toString()
                val isDone = binding.checkBoxDone.isChecked
                if (todo == null) {
                    viewModel.addTodo(Todo(task = task, isDone = isDone))
                } else {
                    viewModel.updateTodo(todo.copy(task = task, isDone = isDone))
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
