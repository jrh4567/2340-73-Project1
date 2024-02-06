package com.example.planningbuddy.ui.todo;

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
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;

import java.util.List;

public class ToDoAdapter extends ArrayAdapter<Task> {

    public ToDoAdapter(Context context, List<Task> exams) {
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
        Task todo = getItem(position);
        final int todoPosition = position;
        // Update UI with exam information
        TextView todoInfoTextView = convertView.findViewById(R.id.examInfoTextView);
        CheckBox completionCheckBox = convertView.findViewById(R.id.completionCheckBox);

        if (todo != null) {
            // Use the toString method to display task information
            todoInfoTextView.setText(todo.toString());
            completionCheckBox.setChecked(todo.isCompleted());
        }

        // Set up the delete button click listener
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            // Handle delete button click
            remove(todo); // Remove the item from the adapter
            notifyDataSetChanged(); // Notify the adapter that the data set has changed

            // Remove the item from the underlying data source (TaskManager)
            TaskManager.removeTask(todoPosition);
        });
        completionCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the completion status when checkbox state changes
            if (todo != null) {
                todo.setCompleted(isChecked);
            }
        });

        return convertView;
    }
}
