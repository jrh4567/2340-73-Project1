package com.example.planningbuddy.ui.notifications;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.CheckBox;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.planningbuddy.R;
import com.example.planningbuddy.databinding.FragmentNotificationsBinding;
import com.example.planningbuddy.db.Course;
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.example.planningbuddy.db.TaskType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExamAdapter extends ArrayAdapter<Task> {

    public ExamAdapter(Context context, List<Task> exams) {
        super(context, 0, exams);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate the custom layout for each item
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_task_item, parent, false);
        }

        // Get the current exam
        Task exam = getItem(position);
        final int examPosition = position;
        // Update UI with exam information
        TextView examInfoTextView = convertView.findViewById(R.id.examInfoTextView);
        CheckBox completionCheckBox = convertView.findViewById(R.id.completionCheckBox);

        if (exam != null) {
            // Use the toString method to display task information
            examInfoTextView.setText(exam.toString());
            completionCheckBox.setChecked(exam.isCompleted());
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        examInfoTextView.setOnClickListener(e -> {
            View popupView = inflater.inflate(R.layout.add_exams_popup, null);
            // Create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.setTouchable(true);
            popupWindow.update();
            // Show the popup window
            popupWindow.showAtLocation(examInfoTextView, Gravity.CENTER, 0, 0);
            Button addExamButton = (Button) popupView.findViewById(R.id.addExamButton);
            EditText examNameEditText = popupView.findViewById(R.id.examNameEditText);
            EditText examDateEditText = popupView.findViewById(R.id.examDateEditText);
            EditText examCourseEditText = popupView.findViewById(R.id.examCourseEditText);
            CheckBox examCompletedCheckBox = popupView.findViewById(R.id.examCompletedCheckBox);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //set the fields to the data from the clicked item
            examNameEditText.setText(exam.getName());
            examDateEditText.setText(dateFormat.format(exam.getDueDate()));
            examCourseEditText.setText(exam.getAssociatedCourse().getDepartment() + " " + exam.getAssociatedCourse().getNumber());
            examCompletedCheckBox.setChecked(exam.isCompleted());

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
                        Date examDate = null;
                        try {
                            examDate = dateFormat.parse(examDateStr);
                        } catch (ParseException e) {
                            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
                            //TODO: fix this navController code
//                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                            navController.navigate(R.id.navigation_notifications); // Change the destination ID accordingly
                        }

                        // Create a Course object based on the user input (modify this as needed)
                        String[] courseParts = examCourse.split(" ");
                        if (courseParts.length >= 2) {
                            String departmentCode = courseParts[0];
                            int courseNumber = Integer.parseInt(courseParts[1]);
                            Course associatedCourse = new Course(departmentCode, courseNumber);

                            // Edit the Task object (TaskType.EXAM)
                            exam.setName(examName);
                            exam.setDueDate(examDate);
                            exam.setAssociatedCourse(associatedCourse);
                            exam.setCompleted(isCompleted);

                            // Add the Task object to the TaskManager
                            TaskManager.updateTask(examPosition, exam);
                            notifyDataSetChanged();


                            // Notify user and clear the input fields
                            Toast.makeText(getContext(), "Exam updated!", Toast.LENGTH_SHORT).show();
                            examNameEditText.setText("");
                            examDateEditText.setText("");
                            examCourseEditText.setText("");
                            examCompletedCheckBox.setChecked(false);
                            popupWindow.dismiss();
//                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                            navController.navigate(R.id.navigation_notifications); // Change the destination ID accordingly
                        } else {
                            Toast.makeText(getContext(), "Invalid course format", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
//                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                            navController.navigate(R.id.navigation_notifications); // Change the destination ID accordingly
                        }

                    } else {
                        Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
//                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                        navController.navigate(R.id.navigation_notifications); // Change the destination ID accordingly
                    }
                }
            });
        });

        // Set up the delete button click listener
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            // Handle delete button click
            remove(exam); // Remove the item from the adapter
            notifyDataSetChanged(); // Notify the adapter that the data set has changed

            // Remove the item from the underlying data source (TaskManager)
            TaskManager.removeTask(examPosition);
        });
        completionCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the completion status when checkbox state changes
            if (exam != null) {
                exam.setCompleted(isChecked);
            }
        });




        return convertView;
    }
}
