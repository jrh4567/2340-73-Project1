package com.example.planningbuddy.ui.courses;// CourseFragment.java
import static com.example.planningbuddy.databinding.AddCoursePopupBinding.inflate;

import android.adservices.adselection.AdSelectionConfig;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.planningbuddy.R;

import com.example.planningbuddy.databinding.FragmentCoursesBinding;

import com.example.planningbuddy.db.Course;
import com.example.planningbuddy.db.MeetingType;
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.example.planningbuddy.db.TaskType;
import com.example.planningbuddy.ui.courses.CourseViewModel;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CourseFragment extends Fragment {

    private FragmentCoursesBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CourseViewModel courseViewModel =
                new ViewModelProvider(this).get(CourseViewModel.class);
        binding = FragmentCoursesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FloatingActionButton courseFab = binding.courseFab;
        courseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = inflater.inflate(R.layout.add_course_popup, null);
                // Create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.setTouchable(true);
                popupWindow.update();
                // Show the popup window
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                TextInputLayout courseDepartmentInputLayout = popupView.findViewById(R.id.courseDepartmentInputLayout);
                TextInputLayout courseNumberInputLayout = popupView.findViewById(R.id.courseNumberInputLayout);
                TextInputLayout courseNameInputLayout = popupView.findViewById(R.id.courseNameInputLayout);
                RadioGroup radioGroup = popupView.findViewById(R.id.radioGroup);
                TextInputLayout editTextLocation = popupView.findViewById(R.id.courseLocationInputLayout);
                EditText editTextStartDate = popupView.findViewById(R.id.editTextStartDate);
                EditText editTextEndDate = popupView.findViewById(R.id.editTextEndDate);
                Button addCourseButton = popupView.findViewById(R.id.addCourseButton);

                // Set up your button click listener
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
                        // Get exam details from the popup
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
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date courseStartDate = null;
                            Date courseEndDate = null;
                            try {
                                courseEndDate = dateFormat.parse(endDateStr);
                                courseStartDate = dateFormat.parse(startDateStr);
                            } catch (ParseException e) {
                                Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                                navController.navigate(R.id.navigation_courses); // Change the destination ID accordingly
                            }
                            GregorianCalendar startDateTime = new GregorianCalendar();
                            startDateTime.setTime(courseStartDate);

                            GregorianCalendar endDateTime = new GregorianCalendar();
                            endDateTime.setTime(courseEndDate);

                            // Convert type to MeetingType
                            MeetingType meetingType = MeetingType.valueOf(type.toUpperCase());
                            Course.MeetingTime meetingObj = new Course.MeetingTime(meetingType, location, startDateTime, endDateTime);
                            Course.MeetingTime[] meet = {meetingObj};
                            int number = Integer.parseInt(numberStr);
                            Course newCourse = new Course(department, number, name, meet);
                            TaskManager.addCourse(newCourse);
                            // Notify user and clear the input fields
                            Toast.makeText(getContext(), "Course added!", Toast.LENGTH_SHORT).show();
                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                            navController.navigate(R.id.navigation_courses); // Change the destination ID accordingly

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
        ListView courseListView = root.findViewById(R.id.courseListView);

        // Create an adapter to populate the ListView with exam data
        CourseAdapter courseAdapter = new CourseAdapter(requireContext(), TaskManager.getAllCourses());

        // Set the adapter for the ListView
        courseListView.setAdapter(courseAdapter);

        // Observe changes in the list of exams and update the adapter
        courseViewModel.getCourses().observe(getViewLifecycleOwner(), todo -> {
            courseAdapter.clear();
            courseAdapter.addAll(todo);
            courseAdapter.notifyDataSetChanged();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
