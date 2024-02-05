package com.example.planningbuddy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planningbuddy.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_courses, R.id.navigation_to_do)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        /** TODO here's the plan:
         * 1. make seperate popover views for adding courses, todos, and assignments (starting with `add_course_popup.xml`)
         * 2. use navController.getCurrentDestination() (probably) to figure out which bottom tab the user has selected
         * 3. use if's to determine which popup to show when the below FAB is clicked.
         */
        /*
        FloatingActionButton fab = findViewById(R.id.addFAB);
        int startDestinationId = navController.getGraph().getStartDestination();
        if (startDestinationId == R.id.navigation_home) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int currentDestinationId = destination.getId();

                // Update the visibility of the FAB based on the current destination
                if (currentDestinationId == R.id.navigation_home) {
                    fab.setVisibility(View.GONE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override

            public void onClick(View view) {
                // Get the current destination ID
                int currentDestinationId = Objects.requireNonNull(navController.getCurrentDestination()).getId();

                // Inflate the layout of the popup window based on the current destination
                int popupLayout;
                if (currentDestinationId == R.id.navigation_dashboard) {
                    popupLayout = R.layout.add_assignments_popup;
                } else if (currentDestinationId == R.id.navigation_notifications) {
                    popupLayout = R.layout.add_exams_popup;
                } else if (currentDestinationId == R.id.navigation_courses) {
                    popupLayout = R.layout.add_course_popup;
                } else if (currentDestinationId == R.id.navigation_to_do) {
                    popupLayout = R.layout.add_to_do_popup;
                } else {
                    return;
                }

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(popupLayout, null);

                // Create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.setTouchable(true);
                popupWindow.update();
                // Show the popup window
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // the error is wrong and i am right.
                Button addExamButton = (Button) popupView.findViewById(R.id.addExamButton);
                addExamButton.setOnClickListener(l -> {
                    Log.d("BUTTON", "onClick: clicked");
                });

                // Dismiss the popup window when touched *in*side.
//                popupView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        popupWindow.dismiss();
//                        return true;
//                    }
//                });

                // inflate the layout of the popup window
                //LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                //View popupView = inflater.inflate(R.layout.add_course_popup, null);

                // create the popup window
                //int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                //int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                //boolean focusable = true; // lets taps outside the popup also dismiss it
                //final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
//                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
//                popupView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        popupWindow.dismiss();
//                        return true;
//                    }
//                });
            }

        });
         */
    }




}