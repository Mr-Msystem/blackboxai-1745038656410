package com.example.notepadapp

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.notepadapp.databinding.DialogAddEditNoteBinding

class AddEditNoteDialogFragment(private val note: Note? = null) : DialogFragment() {

    private var _binding: DialogAddEditNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NotesViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddEditNoteBinding.inflate(LayoutInflater.from(context))

        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]

        if (note != null) {
            binding.editTextTitle.setText(note.title)
            binding.editTextContent.setText(note.content)
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(if (note == null) "Add Note" else "Edit Note")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                val title = binding.editTextTitle.text.toString()
                val content = binding.editTextContent.text.toString()
                if (note == null) {
                    viewModel.addNote(Note(title = title, content = content))
                } else {
                    viewModel.updateNote(note.copy(title = title, content = content))
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
