package com.example.planningbuddy.ui.courses;

import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.planningbuddy.R;
import com.example.planningbuddy.db.Course;
import com.example.planningbuddy.db.MeetingType;
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CourseAdapter extends ArrayAdapter<Course> {

    public CourseAdapter(Context context, List<Course> courses) {
        super(context, 0, courses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate the custom layout for each item
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_course_item, parent, false);
        }

        // Get the current exam
        Course course = getItem(position);
        final int coursePosition = position;

        TextView courseInfoTextView = convertView.findViewById(R.id.courseInfoTextView);

        if (course != null) {
            //display course information
            courseInfoTextView.setText(course.toString());
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        courseInfoTextView.setOnClickListener(e -> {
            View popupView = inflater.inflate(R.layout.add_course_popup, null);
            // Create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.setTouchable(true);
            popupWindow.update();
            // Show the popup window
            popupWindow.showAtLocation(courseInfoTextView, Gravity.CENTER, 0, 0);
            TextInputLayout courseDepartmentInputLayout = popupView.findViewById(R.id.courseDepartmentInputLayout);
            TextInputLayout courseNumberInputLayout = popupView.findViewById(R.id.courseNumberInputLayout);
            TextInputLayout courseNameInputLayout = popupView.findViewById(R.id.courseNameInputLayout);
            RadioGroup radioGroup = popupView.findViewById(R.id.radioGroup);
            TextInputLayout editTextLocation = popupView.findViewById(R.id.courseLocationInputLayout);
            EditText editTextStartDate = popupView.findViewById(R.id.editTextStartDate);
            EditText editTextEndDate = popupView.findViewById(R.id.editTextEndDate);
            Button addCourseButton = popupView.findViewById(R.id.addCourseButton);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //set the fields to the data from the clicked item


            addCourseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openCoursePopup();
                }
                private String getSelectedType(RadioGroup radioGroup) {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId != -1) {
                        RadioButton selectedRadioButton = popupView.findViewById(selectedId);
                        return selectedRadioButton.getText().toString();
                    }
                    return "";
                }
                private void openCoursePopup() {
                    // Get assignment details from the popup
                    String department = courseDepartmentInputLayout.getEditText().getText().toString();
                    String numberStr = courseNumberInputLayout.getEditText().getText().toString();
                    String name = courseNameInputLayout.getEditText().getText().toString();
                    String type = getSelectedType(radioGroup);
                    String location = editTextLocation.getEditText().getText().toString();
                    String startDateStr = editTextStartDate.getText().toString();
                    String endDateStr = editTextEndDate.getText().toString();

                    // Validate and create the Task object (TaskType.EXAM)
                    if (!department.isEmpty() && !numberStr.isEmpty() && !name.isEmpty() && !type.isEmpty() && !startDateStr.isEmpty() && !endDateStr.isEmpty()) {
                        // Format the date using SimpleDateFormat
                        Date courseStartDate = null;
                        Date courseEndDate = null;
                        try {
                            courseEndDate = dateFormat.parse(endDateStr);
                            courseStartDate = dateFormat.parse(startDateStr);
                        } catch (ParseException e) {
                            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
                            //TODO: fix this navController code
//                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                            navController.navigate(R.id.navigation_notifications); // Change the destination ID accordingly
                        }

                        MeetingType meetingType = MeetingType.valueOf(type.toUpperCase());
                        Course.MeetingTime meetingObj = new Course.MeetingTime(meetingType, location, courseStartDate, courseEndDate);
                        Course.MeetingTime[] meet = {meetingObj};
                        int number = Integer.parseInt(numberStr);
                        course.setMeetingTimes(meet);
                        course.setDepartment(department);
                        course.setName(name);
                        course.setNumber(number);

                        TaskManager.updateCourse(coursePosition, course);
                        notifyDataSetChanged();

                        // Notify user and clear the input fields
                        Toast.makeText(getContext(), "Course updated!", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
//                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                            navController.navigate(R.id.navigation_notifications); // Change the destination ID accordingly


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
            remove(course); // Remove the item from the adapter
            notifyDataSetChanged(); // Notify the adapter that the data set has changed

            // Remove the item from the underlying data source (TaskManager)
            TaskManager.removeCourse(coursePosition);
        });

        return convertView;
    }
}
