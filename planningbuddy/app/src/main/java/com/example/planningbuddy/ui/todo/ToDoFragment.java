package com.example.planningbuddy.ui.todo;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.planningbuddy.R;
import com.example.planningbuddy.databinding.FragmentToDoBinding;

import com.example.planningbuddy.db.Course;
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.example.planningbuddy.db.TaskType;
import com.example.planningbuddy.ui.todo.ToDoAdapter;
import com.example.planningbuddy.ui.todo.ToDoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ToDoFragment extends Fragment {

    private FragmentToDoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ToDoViewModel todoViewModel =
                new ViewModelProvider(this).get(ToDoViewModel.class);
        binding = FragmentToDoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FloatingActionButton todoFab = binding.todoFab;
        todoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = inflater.inflate(R.layout.add_to_do_popup, null);
                // Create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.setTouchable(true);
                popupWindow.update();
                // Show the popup window
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                Button addtodoButton = (Button) popupView.findViewById(R.id.addToDoButton);
                EditText todoNameEditText = popupView.findViewById(R.id.todoNameEditText);
                EditText todoDateEditText = popupView.findViewById(R.id.todoDateEditText);
                EditText todoCourseEditText = popupView.findViewById(R.id.todoCourseEditText);
                CheckBox todoCompletedCheckBox = popupView.findViewById(R.id.todoCompletedCheckBox);
                addtodoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openToDoPopup();
                    }
                    private void openToDoPopup() {
                        // input from popup
                        String todoName = todoNameEditText.getText().toString();
                        String todoDateStr = todoDateEditText.getText().toString();
                        String todoCourse = todoCourseEditText.getText().toString();
                        boolean isCompleted = todoCompletedCheckBox.isChecked();

                        // Validate and create the Task object (to do)
                        if (!todoName.isEmpty() && !todoDateStr.isEmpty() && !todoCourse.isEmpty()) {
                            // Format the date using SimpleDateFormat
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date examDate = null;
                            try {
                                examDate = dateFormat.parse(todoDateStr);
                            } catch (ParseException e) {
                                Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                                navController.navigate(R.id.navigation_to_do); // Change the destination ID accordingly
                            }

                            // Create a Course object based on the user input (modify this as needed)
                            String[] courseParts = todoCourse.split(" ");
                            if (courseParts.length >= 2) {
                                String departmentCode = courseParts[0];
                                int courseNumber = Integer.parseInt(courseParts[1]);
                                Course associatedCourse = new Course(departmentCode, courseNumber);

                                // Create the Task object (TaskType.EXAM)
                                Task todo = new Task(todoName, examDate, TaskType.TODO, associatedCourse, isCompleted);

                                // Add the Task object to the TaskManager
                                TaskManager.addTask(todo);

                                // Notify user and clear the input fields
                                Toast.makeText(getContext(), "To do added!", Toast.LENGTH_SHORT).show();
                                todoNameEditText.setText("");
                                todoDateEditText.setText("");
                                todoCourseEditText.setText("");
                                todoCompletedCheckBox.setChecked(false);
                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                                navController.navigate(R.id.navigation_to_do); // Change the destination ID accordingly
                            } else {
                                Toast.makeText(getContext(), "Invalid course format", Toast.LENGTH_SHORT).show();
                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                                navController.navigate(R.id.navigation_to_do); // Change the destination ID accordingly
                            }

                        } else {
                            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                            navController.navigate(R.id.navigation_to_do); // Change the destination ID accordingly
                        }
                    }
                });
            }
        });

        // Get the ListView from the layout
        ListView todoListView = binding.todoListView;

        // Create an adapter to populate the ListView with exam data
        ToDoAdapter todoAdapter = new ToDoAdapter(requireContext(), TaskManager.getTasksByType(TaskType.TODO));

        // Set the adapter for the ListView
        todoListView.setAdapter(todoAdapter);

        // Observe changes in the list of exams and update the adapter
        todoViewModel.getToDos().observe(getViewLifecycleOwner(), todo -> {
            todoAdapter.clear();
            todoAdapter.addAll(todo);
            todoAdapter.notifyDataSetChanged();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}