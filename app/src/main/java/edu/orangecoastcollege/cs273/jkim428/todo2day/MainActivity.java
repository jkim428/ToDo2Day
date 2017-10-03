package edu.orangecoastcollege.cs273.jkim428.todo2day;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Reference to the list of all tasks
    private List<Task> mAllTasksList = new ArrayList<>();
    // public static final String TAG = MainActivity.class.getSimpleName(); // just for debugging purpose (09a)

    // Reference to the database
    private DBHelper mDB;
    // References to the widgets needed
    private EditText mDescriptionEditText;
    private ListView mTaskListView;

    // Reference to the custom list adapter
    TaskListAdapter mTaskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDB = new DBHelper(this);
        mDescriptionEditText = (EditText) findViewById(R.id.taskEditText);
        mTaskListView = (ListView) findViewById(R.id.taskListView);

/* Just for 09a
        // Clear the existing database
        deleteDatabase(DBHelper.DATABASE_NAME);

        // Pre-populate the List with 4 tasks
        mAllTasksList.add(new Task("Study for CS 273 Midterm", false));
        mAllTasksList.add(new Task("Finish IC#08", true));
        mAllTasksList.add(new Task("Sleep at some point", false));
        mAllTasksList.add(new Task("Play League of Legends", true));

        // Let's instantiate a new DBHelper
        DBHelper db = new DBHelper(this);

        // Let's loop through the List and add each one to the database
        for (Task t : mAllTasksList)
            db.addTasks(t);

        // Let's clear out the List, the rebuild it from the database this time
        mAllTasksList.clear();
        // Retrieve all tasks from the database
        mAllTasksList = db.getAllTasks();

        // Loop through each of the Tasks, print them to Log.i
        for (Task t : mAllTasksList)
            Log.i(TAG, t.toString());


        Log.i(TAG, "After deleting task 4");
        db.deleteTask(mAllTasksList.get(3));
        mAllTasksList = db.getAllTasks();
        for (Task t : mAllTasksList)
            Log.i(TAG, t.toString());

*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Database related "stuff"
        // 1) Populate the list from the database (using DBHelper)
        mAllTasksList = mDB.getAllTasks();
        // 2) Connect the ListView with the custom list adapter
        mTaskListAdapter = new TaskListAdapter(this, R.layout.task_item, mAllTasksList);
        mTaskListView.setAdapter(mTaskListAdapter);
    }

    public void addTask(View v)
    {
        // Check to see if the description is empty or null
        String description = mDescriptionEditText.getText().toString();
        if (TextUtils.isEmpty(description))
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_LONG).show();
        else
        {
            // Create the Task
            Task newTask = new Task(description, false);
            // Add it to the database
            mDB.addTask(newTask);
            // Add it to the List
            mAllTasksList.add(newTask);
            // Notify the list adapter that it's been changed
            mTaskListAdapter.notifyDataSetChanged();
            // Clear the description EditText
            mDescriptionEditText.setText("");
        }
    }

    public void clearAllTasks(View v)
    {
        // Clear on the view
        mAllTasksList.clear();
        mTaskListAdapter.notifyDataSetChanged();

        // Delete from the database
        mDB.deleteAllTasks();
    }

    public void toggleTaskStatus(View v)
    {
        CheckBox selectedCheckBox = (CheckBox) v;
        Task selectedTask = (Task) selectedCheckBox.getTag();
        // Update the task
        selectedTask.setDone(selectedCheckBox.isChecked());
        // Update the database
        mDB.updateTask(selectedTask);
    }
}
