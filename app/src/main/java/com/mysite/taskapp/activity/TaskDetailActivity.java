package com.mysite.taskapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mysite.taskapp.R;
import com.mysite.taskapp.model.TasksModel;

import java.util.Objects;

public class TaskDetailActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private Query query;
    private EditText etTaskName;

    private Button btnDelTask, btnUpdateTask;
    String taskId, taskName, userId;
    private Toolbar toolbar;
    boolean isTaskDaily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        toolbar = findViewById(R.id.toolbarDetTask);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Task");
        etTaskName = findViewById(R.id.etTask);
        btnDelTask = findViewById(R.id.btnDelTask);
        btnUpdateTask = findViewById(R.id.btnUpdateTask);

        taskId = getIntent().getStringExtra("taskId");
        taskName = getIntent().getStringExtra("taskName");
        isTaskDaily = getIntent().getBooleanExtra("isTaskDaily", false);
        userId = getIntent().getStringExtra("key");
        TaskDetailActivity taskThis = this;

        etTaskName.setText(taskName);

        btnDelTask.setOnClickListener(v -> deleteTask());

        btnUpdateTask.setOnClickListener(v -> {
            String taskChanged = etTaskName.getText().toString().trim();
            if (taskChanged.isEmpty()) {
                deleteTask();
                return;
            }
            FirebaseDatabase.getInstance().getReference()
                    .child("tasks").child(taskId).setValue(new TasksModel(taskId, taskChanged, isTaskDaily, userId));
            Toast.makeText(this, "updated successfully.", Toast.LENGTH_SHORT).show();
        });
    }

    private void deleteTask() {
        FirebaseDatabase.getInstance().getReference()
                .child("tasks").child(taskId).removeValue();
        onBackPressed();

    }
}