package com.example.planningbuddy.ui.courses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.planningbuddy.R;
import com.example.planningbuddy.db.Course;
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;

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
        // Update UI with exam information
        TextView todoInfoTextView = convertView.findViewById(R.id.courseInfoTextView);

        if (course != null) {
            // Use the toString method to display task information
            todoInfoTextView.setText(course.toString());
        }

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
