package com.mysite.taskapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mysite.taskapp.R;
import com.mysite.taskapp.adapter.TasksAdapter;
import com.mysite.taskapp.interfaces.TaskInterface;
import com.mysite.taskapp.model.TasksModel;

import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity implements TaskInterface {
    private SharedPreferences sh;
    private RecyclerView recyclerTasks;
    private TasksAdapter recyclerAdapter;
    private List<TasksModel> tasksList;
    private DatabaseReference reference;
    private EditText etTaskName;
    private Toolbar toolbar;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        sh = getSharedPreferences(getString(R.string.localData), Context.MODE_PRIVATE);
        key = sh.getString("key", "");
        Log.d("key", "onCreate: " + key);

        if (!sh.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Tasks");
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        etTaskName = findViewById(R.id.etTask);
        Button btnAddTask = findViewById(R.id.addTaskBtn);
        TasksActivity taskThis = this;
        reference = FirebaseDatabase.getInstance().getReference("tasks");
        tasksList = new ArrayList<>();
        recyclerTasks = findViewById(R.id.tasksRecycler);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasksList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    Log.d("task1234", "onDataChange: " + postSnapshot.getValue());
                    TasksModel task = postSnapshot.getValue(TasksModel.class);
                    assert task != null;
                    if (task.getUserId().equals(key))
                        tasksList.add(task);
                }
                progressBar.setVisibility(View.GONE);
                recyclerAdapter = new TasksAdapter(taskThis, tasksList, taskThis);
                recyclerTasks.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
                recyclerTasks.setLayoutManager(new LinearLayoutManager(taskThis));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(taskThis, "Error while fetching data ", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddTask.setOnClickListener(v -> {
            String taskName = etTaskName.getText().toString();
            if (taskName.isEmpty()) {
                Toast.makeText(this, "Enter a valid task!!", Toast.LENGTH_SHORT).show();
                return;
            }
            String taskId = reference.push().getKey();
            sh.edit().putString("taskId", taskId).apply();
            assert taskId != null;
            reference.child(taskId).setValue(new TasksModel(taskId, taskName, false, key));
            recyclerAdapter.notifyDataSetChanged();
            etTaskName.setText("");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            sh.edit().putBoolean("isLoggedIn", false).apply();
            startActivity(new Intent(this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(TasksModel tasksModel) {
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra("taskId", tasksModel.getTaskId());
        intent.putExtra("taskName", tasksModel.getTaskName());
        intent.putExtra("isTaskDaily", tasksModel.isDaily());
        intent.putExtra("key", tasksModel.getUserId());
        startActivity(intent);
    }
}