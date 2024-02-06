package com.example.planningbuddy.ui.dashboard;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.planningbuddy.R;
import com.example.planningbuddy.db.Course;
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.example.planningbuddy.db.TaskType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AssignmentAdapter extends ArrayAdapter<Task> {

    public AssignmentAdapter(Context context, List<Task> exams) {
        super(context, 0, exams);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate the custom layout for each item
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_task_item, parent, false);
        }

        // Get the current assignment
        Task assignment = getItem(position);
        final int assignmentPosition = position;
        // update w assignment info
        TextView examInfoTextView = convertView.findViewById(R.id.examInfoTextView);
        CheckBox completionCheckBox = convertView.findViewById(R.id.completionCheckBox);

        if (assignment != null) {
            // Use the toString method to display task information
            examInfoTextView.setText(assignment.toString());
            completionCheckBox.setChecked(assignment.isCompleted());
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        examInfoTextView.setOnClickListener(e -> {
            View popupView = inflater.inflate(R.layout.add_assignments_popup, null);
            // Create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.setTouchable(true);
            popupWindow.update();
            // Show the popup window
            popupWindow.showAtLocation(examInfoTextView, Gravity.CENTER, 0, 0);
            Button addaButton = (Button) popupView.findViewById(R.id.addAssignmentButton);
            EditText aNameEditText = popupView.findViewById(R.id.assignmentNameEditText);
            EditText aDateEditText = popupView.findViewById(R.id.assignmentDateEditText);
            EditText aCourseEditText = popupView.findViewById(R.id.assignmentCourseEditText);
            CheckBox aCompletedCheckBox = popupView.findViewById(R.id.assignmentCompletedCheckBox);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //set the fields to the data from the clicked item
            aNameEditText.setText(assignment.getName());
            aDateEditText.setText(dateFormat.format(assignment.getDueDate()));
            aCourseEditText.setText(assignment.getAssociatedCourse().getDepartment() + " " + assignment.getAssociatedCourse().getNumber());
            aCompletedCheckBox.setChecked(assignment.isCompleted());

            addaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openAssignmentPopup();
                }
                private void openAssignmentPopup() {
                    // Get assignment details from the popup
                    String aName = aNameEditText.getText().toString();
                    String aDateStr = aDateEditText.getText().toString();
                    String aCourse = aCourseEditText.getText().toString();
                    boolean isCompleted = aCompletedCheckBox.isChecked();

                    // create assignment obj
                    if (!aName.isEmpty() && !aDateStr.isEmpty() && !aCourse.isEmpty()) {
                        // Format the date using SimpleDateFormat
                        Date aDate = null;
                        try {
                            aDate = dateFormat.parse(aDateStr);
                        } catch (ParseException e) {
                            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
                            //TODO: fix this navController code
//                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                            navController.navigate(R.id.navigation_notifications);
                        }

                        // Create a Course object based on the user input
                        String[] courseParts = aCourse.split(" ");
                        if (courseParts.length >= 2) {
                            String departmentCode = courseParts[0];
                            int courseNumber = Integer.parseInt(courseParts[1]);
                            Course associatedCourse = new Course(departmentCode, courseNumber);

                            // Edit obj
                            assignment.setName(aName);
                            assignment.setDueDate(aDate);
                            assignment.setAssociatedCourse(associatedCourse);
                            assignment.setCompleted(isCompleted);

                            // Add the Task object to the TaskManager
                            TaskManager.updateTask(assignmentPosition, assignment);
                            notifyDataSetChanged();

                            // Notify user and clear the input fields
                            Toast.makeText(getContext(), "Assignment updated!", Toast.LENGTH_SHORT).show();
                            aNameEditText.setText("");
                            aDateEditText.setText("");
                            aCourseEditText.setText("");
                            aCompletedCheckBox.setChecked(false);
                            popupWindow.dismiss();
//                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                            navController.navigate(R.id.navigation_notifications);
                        } else {
                            Toast.makeText(getContext(), "Invalid course format", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
//                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                            navController.navigate(R.id.navigation_notifications);
                        }

                    } else {
                        Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
//                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                        navController.navigate(R.id.navigation_notifications);
                    }
                }
            });
        });

        // delete btn
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {

            remove(assignment); // Remove the item from the adapter
            notifyDataSetChanged(); // Notify the adapter that the data set has changed

            TaskManager.removeTask(assignmentPosition);
        });
        completionCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (assignment != null) {
                assignment.setCompleted(isChecked);
            }
        });



        return convertView;
    }
}
