package com.example.planningbuddy.ui.courses;// CourseFragment.java
import static com.example.planningbuddy.databinding.AddCoursePopupBinding.inflate;

import android.adservices.adselection.AdSelectionConfig;
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

import com.example.planningbuddy.R;
import com.example.planningbuddy.databinding.AddCoursePopupBinding;
import com.example.planningbuddy.databinding.FragmentCoursesBinding;
import com.example.planningbuddy.db.Course;
import com.example.planningbuddy.db.MeetingType;
import com.example.planningbuddy.ui.courses.CourseViewModel;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CourseFragment extends Fragment {

    private FragmentCoursesBinding binding;
    private CourseViewModel courseViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        binding = FragmentCoursesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //TODO also need to add an "add" button in the xml to submit the changes
        AddCoursePopupBinding popupBinding = AddCoursePopupBinding.inflate(LayoutInflater.from(getContext()), null, false);

        final EditText textStartDate = popupBinding.editTextStartDate; //fixme

        final EditText editTextClassName = binding.editTextClassName;
        Button addButton = binding.btnAddCourse;
        ListView listViewClasses = binding.textCourses;

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String className = editTextClassName.getText().toString().trim();
                if (!className.isEmpty()) {
                    Course.MeetingTime meet = new Course.MeetingTime(MeetingType.LECTURE, "CCB", new GregorianCalendar(2024, 2, 3, 13, 0), new GregorianCalendar(2024, 2, 3, 14, 0));
                    courseViewModel.addClass(new Course("ND", 0000, className, new Course.MeetingTime[]{meet}));
                    editTextClassName.setText("");
                }
            }
        });

        courseViewModel.getClassList().observe(getViewLifecycleOwner(), this::updateClassList);

        return root;
    }

    private void updateClassList(List<Course> classList) {
        ArrayAdapter<Course> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, classList);

        binding.textCourses.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
