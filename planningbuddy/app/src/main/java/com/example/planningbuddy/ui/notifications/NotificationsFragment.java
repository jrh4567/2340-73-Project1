package com.example.planningbuddy.ui.notifications;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.planningbuddy.R;
import com.example.planningbuddy.databinding.FragmentNotificationsBinding;
import static com.example.planningbuddy.databinding.AddExamsPopupBinding.inflate;
import com.example.planningbuddy.databinding.AddExamsPopupBinding;
import com.example.planningbuddy.db.Course;
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.example.planningbuddy.db.TaskType;

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

        View popupView = examPopupBinding.getRoot(); //i don't think this is working right, but i don't know how else to get the view

        Button addExamButton = (Button) popupView.findViewById(R.id.addExamButton);
        EditText examNameEditText = examPopupBinding.examNameEditText;
        EditText examDateEditText = examPopupBinding.examDateEditText;
        EditText examCourseEditText = examPopupBinding.examCourseEditText;
        CheckBox examCompletedCheckBox = examPopupBinding.examCompletedCheckBox;
//        addExamButton.setClickable(true);
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
        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}