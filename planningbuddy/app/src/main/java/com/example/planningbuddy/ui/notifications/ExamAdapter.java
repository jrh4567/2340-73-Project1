package com.example.planningbuddy.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.planningbuddy.R;
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;

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

        if (exam != null) {
            // Use the toString method to display task information
            examInfoTextView.setText(exam.toString());
        }

        // Set up the delete button click listener
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            // Handle delete button click
            remove(exam); // Remove the item from the adapter
            notifyDataSetChanged(); // Notify the adapter that the data set has changed

            // Remove the item from the underlying data source (TaskManager)
            TaskManager.removeTask(examPosition);
        });

        return convertView;
    }
}
