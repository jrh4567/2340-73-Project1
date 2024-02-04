package com.example.planningbuddy.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.planningbuddy.R;
import com.example.planningbuddy.db.Course;
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.example.planningbuddy.db.TaskType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddExamFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("FragmentLifecycle", "onCreateView executed");
        View root = inflater.inflate(R.layout.add_exams_popup, container, false);

        Button addExamButton = root.findViewById(R.id.addExamButton);
        EditText examNameEditText = root.findViewById(R.id.examNameEditText);
        EditText examDateEditText = root.findViewById(R.id.examDateEditText);
        EditText examCourseEditText = root.findViewById(R.id.examCourseEditText);
        CheckBox examCompletedCheckBox = root.findViewById(R.id.examCompletedCheckBox);
        Log.d("Button", "Button Clickable: " + addExamButton.isClickable());

        addExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        throw new RuntimeException(e);
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
                    } else {
                        Toast.makeText(getContext(), "Invalid course format", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }
}
