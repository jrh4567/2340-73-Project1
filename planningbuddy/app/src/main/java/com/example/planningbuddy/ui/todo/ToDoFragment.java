package com.example.planningbuddy.ui.todo;// CourseFragment.java
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.planningbuddy.databinding.FragmentToDoBinding;
import com.example.planningbuddy.ui.todo.ToDoViewModel;


import java.util.List;

public class ToDoFragment extends Fragment {

    private FragmentToDoBinding binding;
    private ToDoViewModel toDoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);

        binding = FragmentToDoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText editTextClassName = binding.editTextToDoName;
        Button addButton = binding.btnAddTask;
        ListView listViewClasses = binding.textToDo;

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String className = editTextClassName.getText().toString().trim();
                if (!className.isEmpty()) {
                    toDoViewModel.addClass(className);
                    editTextClassName.setText("");
                }
            }
        });

        toDoViewModel.getClassList().observe(getViewLifecycleOwner(), this::updateClassList);

        return root;
    }

    private void updateClassList(List<String> classList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, classList);

        binding.textToDo.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
