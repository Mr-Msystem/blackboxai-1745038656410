package com.example.notepadapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepadapp.databinding.FragmentTodoBinding

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TodoAdapter { todo ->
            // Handle todo click for edit or delete
            viewModel.deleteTodo(todo)
        }
        binding.recyclerViewTodo.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTodo.adapter = adapter

        viewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        viewModel.allTodos.observe(viewLifecycleOwner, Observer { todos ->
            adapter.submitList(todos)
        })

        binding.fabAddTodo.setOnClickListener {
            AddEditTodoDialogFragment().show(parentFragmentManager, "AddTodo")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
