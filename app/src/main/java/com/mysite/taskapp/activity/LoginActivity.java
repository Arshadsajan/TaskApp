package com.mysite.taskapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mysite.taskapp.R;
import com.mysite.taskapp.model.User;

public class LoginActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private TextInputLayout etEmail, etPassword;
    private SharedPreferences sh;
    private boolean found;

    @Override
    protected void onPause() {
        super.onPause();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sh = getSharedPreferences(getString(R.string.localData), MODE_PRIVATE);
        Button loginBtn = findViewById(R.id.btnLogin);
        Button btnLogRegister = findViewById(R.id.btnLoginReg);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("user");
        found = false;

        loginBtn.setOnClickListener(v -> {
            String email = etEmail.getEditText().getText().toString();
            String password = etPassword.getEditText().getText().toString();

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        User user = postSnapshot.getValue(User.class);
                        assert user != null;
                        if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                            found = true;
                            Intent intent = new Intent(LoginActivity.this, TasksActivity.class);
                            String key = postSnapshot.getKey();
                            sh.edit().putString("key", key).apply();
                            sh.edit().putBoolean("isLoggedIn", true).apply();
                            Log.d("loginKey", "onChildAdded: " + key);
                            Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            break;
                        }
                    }
                    if (!found)
                        Toast.makeText(LoginActivity.this, "User not found!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LoginActivity.this, "Some error occurred!!" +error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnLogRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }
}