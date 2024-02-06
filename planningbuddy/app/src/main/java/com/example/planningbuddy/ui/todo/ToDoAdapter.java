package com.example.planningbuddy.ui.todo;

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

import com.example.planningbuddy.R;
import com.example.planningbuddy.db.Course;
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        LayoutInflater inflater = LayoutInflater.from(getContext());
        todoInfoTextView.setOnClickListener(e -> {
            View popupView = inflater.inflate(R.layout.add_to_do_popup, null);
            // Create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.setTouchable(true);
            popupWindow.update();
            // Show the popup window
            popupWindow.showAtLocation(todoInfoTextView, Gravity.CENTER, 0, 0);
            Button addToDoButton = (Button) popupView.findViewById(R.id.addToDoButton);
            EditText todoNameEditText = popupView.findViewById(R.id.todoNameEditText);
            EditText todoDateEditText = popupView.findViewById(R.id.todoDateEditText);
            EditText todoCourseEditText = popupView.findViewById(R.id.todoCourseEditText);
            CheckBox todoCompletedCheckBox = popupView.findViewById(R.id.todoCompletedCheckBox);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //set the fields to the data from the clicked item
            todoNameEditText.setText(todo.getName());
            todoDateEditText.setText(dateFormat.format(todo.getDueDate()));
            todoCourseEditText.setText(todo.getAssociatedCourse().getDepartment() + " " + todo.getAssociatedCourse().getNumber());
            todoCompletedCheckBox.setChecked(todo.isCompleted());

            addToDoButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    openTodoPopup();
                }
                private void openTodoPopup() {
                    String todoName = todoNameEditText.getText().toString();
                    String todoDateStr = todoDateEditText.getText().toString();
                    String todoCourse = todoCourseEditText.getText().toString();
                    boolean isCompleted = todoCompletedCheckBox.isChecked();

                    // create to do object
                    if (!todoName.isEmpty() && !todoDateStr.isEmpty() && !todoCourse.isEmpty()) {
                        // Format the date using SimpleDateFormat
                        Date todoDate = null;
                        try {
                            todoDate = dateFormat.parse(todoDateStr);
                        } catch (ParseException e) {
                            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
                            //TODO: fix this navController code
//                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                            navController.navigate(R.id.navigation_notifications); // Change the destination ID accordingly
                        }

                        // Create a Course object based on the user input (modify this as needed)
                        String[] courseParts = todoCourse.split(" ");
                        if (courseParts.length >= 2) {
                            String departmentCode = courseParts[0];
                            int courseNumber = Integer.parseInt(courseParts[1]);
                            Course associatedCourse = new Course(departmentCode, courseNumber);

                            // Edit the Task object (TaskType.EXAM)
                            todo.setName(todoName);
                            todo.setDueDate(todoDate);
                            todo.setAssociatedCourse(associatedCourse);
                            todo.setCompleted(isCompleted);

                            // Add the Task object to the TaskManager
                            TaskManager.updateTask(todoPosition, todo);
                            notifyDataSetChanged();


                            // Notify user and clear the input fields
                            Toast.makeText(getContext(), "To Do updated!", Toast.LENGTH_SHORT).show();
                            todoNameEditText.setText("");
                            todoDateEditText.setText("");
                            todoCourseEditText.setText("");
                            todoCompletedCheckBox.setChecked(false);
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

        return convertView;
    }
}
