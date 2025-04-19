package com.example.notepadapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepadapp.databinding.FragmentNotesBinding

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NotesViewModel
    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NotesAdapter { note ->
            // Handle note click for edit or delete
            // For simplicity, just delete on click here
            viewModel.deleteNote(note)
        }
        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewNotes.adapter = adapter

        viewModel = ViewModelProvider(this)[NotesViewModel::class.java]
        viewModel.allNotes.observe(viewLifecycleOwner, Observer { notes ->
            adapter.submitList(notes)
        })

        binding.fabAddNote.setOnClickListener {
            // Open dialog or new fragment to add note
            AddEditNoteDialogFragment().show(parentFragmentManager, "AddNote")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
