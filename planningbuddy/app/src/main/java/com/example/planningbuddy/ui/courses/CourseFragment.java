package com.example.planningbuddy.ui.courses;// CourseFragment.java
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
import androidx.lifecycle.ViewModelProvider;

import com.example.planningbuddy.databinding.FragmentCoursesBinding;
import com.example.planningbuddy.ui.courses.CourseViewModel;

import java.util.List;

public class CourseFragment extends Fragment {

    private FragmentCoursesBinding binding;
    private CourseViewModel courseViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        binding = FragmentCoursesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText editTextClassName = binding.editTextClassName;
        Button addButton = binding.btnAddClass;
        ListView listViewClasses = binding.textCourses;

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String className = editTextClassName.getText().toString().trim();
                if (!className.isEmpty()) {
                    courseViewModel.addClass(className);
                    editTextClassName.setText("");
                }
            }
        });

        courseViewModel.getClassList().observe(getViewLifecycleOwner(), this::updateClassList);

        return root;
    }

    private void updateClassList(List<String> classList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, classList);

        binding.textCourses.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
