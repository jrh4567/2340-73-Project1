package com.example.planningbuddy.ui.courses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.planningbuddy.databinding.FragmentCoursesBinding;
import com.example.planningbuddy.ui.dashboard.DashboardViewModel;

public class CourseFragment extends Fragment {

    private FragmentCoursesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CourseViewModel dashboardViewModel =
                new ViewModelProvider(this).get(CourseViewModel.class);

        binding = FragmentCoursesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCourses;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}