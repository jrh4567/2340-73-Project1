package com.example.planningbuddy.ui.notifications;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.planningbuddy.databinding.FragmentNotificationsBinding;
import static com.example.planningbuddy.databinding.AddExamsPopupBinding.inflate;
import com.example.planningbuddy.databinding.AddExamsPopupBinding;
import com.example.planningbuddy.db.Course;
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.example.planningbuddy.db.TaskType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        AddExamsPopupBinding examPopupBinding = AddExamsPopupBinding.inflate(inflater, null, false);
        FloatingActionButton examFab = binding.examFab;
        examFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = inflater.inflate(R.layout.add_exams_popup, null);
                // Create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.setTouchable(true);
                popupWindow.update();
                // Show the popup window
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                Button addExamButton = (Button) popupView.findViewById(R.id.addExamButton);
                EditText examNameEditText = popupView.findViewById(R.id.examNameEditText);
                EditText examDateEditText = popupView.findViewById(R.id.examDateEditText);
                EditText examCourseEditText = popupView.findViewById(R.id.examCourseEditText);
                CheckBox examCompletedCheckBox = popupView.findViewById(R.id.examCompletedCheckBox);
                addExamButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openExamPopup();
                    }
                    private void openExamPopup() {
                        Log.d("AddExamButton", "Button clicked!");
                        // Get exam details from the popup
                        String examName = examNameEditText.getText().toString();
                        String examDateStr = examDateEditText.getText().toString();
                        String examCourse = examCourseEditText.getText().toString();
                        boolean isCompleted = examCompletedCheckBox.isChecked();

                        // Validate and create the Task object (TaskType.EXAM)
                        if (!examName.isEmpty() && !examDateStr.isEmpty() && !examCourse.isEmpty()) {
                            // Format the date using SimpleDateFormat
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date examDate = null;
                            try {
                                examDate = dateFormat.parse(examDateStr);
                            } catch (ParseException e) {
                                Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                                navController.navigate(R.id.navigation_notifications); // Change the destination ID accordingly
                            }

                            // Create a Course object based on the user input (modify this as needed)
                            String[] courseParts = examCourse.split(" ");
                            if (courseParts.length >= 2) {
                                String departmentCode = courseParts[0];
                                int courseNumber = Integer.parseInt(courseParts[1]);
                                Course associatedCourse = new Course(departmentCode, courseNumber);

                                // Create the Task object (TaskType.EXAM)
                                Task exam = new Task(examName, examDate, TaskType.EXAM, associatedCourse, isCompleted);

                                // Add the Task object to the TaskManager
                                TaskManager.addTask(exam);

                                // Notify user and clear the input fields
                                Toast.makeText(getContext(), "Exam added!", Toast.LENGTH_SHORT).show();
                                examNameEditText.setText("");
                                examDateEditText.setText("");
                                examCourseEditText.setText("");
                                examCompletedCheckBox.setChecked(false);
                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                                navController.navigate(R.id.navigation_notifications); // Change the destination ID accordingly
                            } else {
                                Toast.makeText(getContext(), "Invalid course format", Toast.LENGTH_SHORT).show();
                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                                navController.navigate(R.id.navigation_notifications); // Change the destination ID accordingly
                            }

                        } else {
                            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                            navController.navigate(R.id.navigation_notifications); // Change the destination ID accordingly
                        }
                    }
                });
            }
        });

        // Get the ListView from the layout
        ListView examsListView = root.findViewById(R.id.examsListView);

        // Create an adapter to populate the ListView with exam data
        ArrayAdapter<Task> examsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, TaskManager.getTasksByType(TaskType.EXAM));

        // Set the adapter for the ListView
        examsListView.setAdapter(examsAdapter);

        // Observe changes in the list of exams and update the adapter
        notificationsViewModel.getExams().observe(getViewLifecycleOwner(), exams -> {
            examsAdapter.clear();
            examsAdapter.addAll(exams);
            examsAdapter.notifyDataSetChanged();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}