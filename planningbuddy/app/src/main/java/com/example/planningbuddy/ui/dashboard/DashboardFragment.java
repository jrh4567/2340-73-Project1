package com.example.planningbuddy.ui.dashboard;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.planningbuddy.R;
import com.example.planningbuddy.databinding.FragmentDashboardBinding;

import com.example.planningbuddy.db.Course;
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.example.planningbuddy.db.TaskType;
import com.example.planningbuddy.ui.dashboard.AssignmentAdapter;

import com.example.planningbuddy.ui.dashboard.DashboardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FloatingActionButton assignmentFab = binding.assignmentFab;
        assignmentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = inflater.inflate(R.layout.add_assignments_popup, null);
                // Create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.setTouchable(true);
                popupWindow.update();
                // Show the popup window
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                Button addExamButton = (Button) popupView.findViewById(R.id.addAssignmentButton);
                EditText assignmentNameEditText = popupView.findViewById(R.id.assignmentNameEditText);
                EditText assignmentDateEditText = popupView.findViewById(R.id.assignmentDateEditText);
                EditText assignmentCourseEditText = popupView.findViewById(R.id.assignmentCourseEditText);
                CheckBox assignmentCompletedCheckBox = popupView.findViewById(R.id.assignmentCompletedCheckBox);
                addExamButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openAssignmentPopup();
                    }
                    private void openAssignmentPopup() {
                        // assignment details from popup
                        String assignmentName = assignmentNameEditText.getText().toString();
                        String assignmentDateStr = assignmentDateEditText.getText().toString();
                        String assignmentCourse = assignmentCourseEditText.getText().toString();
                        boolean isCompleted = assignmentCompletedCheckBox.isChecked();

                        // create assignment obj
                        if (!assignmentName.isEmpty() && !assignmentDateStr.isEmpty() && !assignmentCourse.isEmpty()) {
                            // Format the date using SimpleDateFormat
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date examDate = null;
                            try {
                                examDate = dateFormat.parse(assignmentDateStr);
                            } catch (ParseException e) {
                                Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                                navController.navigate(R.id.navigation_dashboard);
                            }

                            // Create a Course object based on the user input
                            String[] courseParts = assignmentCourse.split(" ");
                            if (courseParts.length >= 2) {
                                String departmentCode = courseParts[0];
                                int courseNumber = Integer.parseInt(courseParts[1]);
                                Course associatedCourse = new Course(departmentCode, courseNumber);

                                // create obj
                                Task exam = new Task(assignmentName, examDate, TaskType.ASSIGNMENT, associatedCourse, isCompleted);

                                // Add the Task object to the TaskManager
                                TaskManager.addTask(exam);

                                // Notify user and clear the input fields
                                Toast.makeText(getContext(), "Assignment added!", Toast.LENGTH_SHORT).show();
                                assignmentNameEditText.setText("");
                                assignmentDateEditText.setText("");
                                assignmentCourseEditText.setText("");
                                assignmentCompletedCheckBox.setChecked(false);
                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                                navController.navigate(R.id.navigation_dashboard);
                            } else {
                                Toast.makeText(getContext(), "Invalid course format", Toast.LENGTH_SHORT).show();
                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                                navController.navigate(R.id.navigation_dashboard);
                            }

                        } else {
                            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                            navController.navigate(R.id.navigation_dashboard);
                        }
                    }
                });
            }
        });
        // spinner code for functionality
        Spinner sortSpinner = binding.sortSpinner;

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_options,
                android.R.layout.simple_spinner_item
        );

        // dropdown layout
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // bind spinner to adapter
        sortSpinner.setAdapter(spinnerAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle selection
                String selectedItem = parentView.getItemAtPosition(position).toString();
                if (selectedItem.equals("Sort by Course")) {
                    dashboardViewModel.setSortByDate(false);
                } else if (selectedItem.equals("Sort by Date")) {
                    dashboardViewModel.setSortByDate(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }
        });
        ListView assignmentListView = root.findViewById(R.id.assignmentListView);
        AssignmentAdapter assignmentAdapter;

        // to set filter by course or date
        if (dashboardViewModel.getSortByDate()) {
            assignmentAdapter = new AssignmentAdapter(requireContext(), TaskManager.getAssignmentsByDate());
        } else {
            assignmentAdapter = new AssignmentAdapter(requireContext(), TaskManager.getAssignmentsByCourse());
        }
        //assignmentAdapter = new AssignmentAdapter(requireContext(), TaskManager.getAssignmentsByDate());
        // Set the adapter for the ListView
        assignmentListView.setAdapter(assignmentAdapter);

        // Observe changes in the list of assignments and update the adapter
        dashboardViewModel.getAssignments().observe(getViewLifecycleOwner(), assignments -> {
            assignmentAdapter.clear();
            assignmentAdapter.addAll(assignments);
            assignmentAdapter.notifyDataSetChanged();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}