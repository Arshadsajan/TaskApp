package com.mysite.taskapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mysite.taskapp.R;
import com.mysite.taskapp.model.User;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private TextInputLayout etEmail, etPassword;
    private SharedPreferences sh;

    @Override
    protected void onPause() {
        super.onPause();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sh = getSharedPreferences(getString(R.string.localData), MODE_PRIVATE);

        Button regBtn = findViewById(R.id.btnReg);
        Button btnRegLogin = findViewById(R.id.btnRegLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("user");

        regBtn.setOnClickListener(v -> {
            String email = etEmail.getEditText().getText().toString();
            String password = etPassword.getEditText().getText().toString();

            if (!validateEmail(email) || !validatePassword(password))
                return;

            User user = new User(email, password);
            String key = reference.push().getKey();
            sh.edit().putString("key", key).apply();
            sh.edit().putBoolean("isLoggedIn", true).apply();
            assert key != null;
            reference.child(key).setValue(user);
            Intent intent = new Intent(this, TasksActivity.class);
            Toast.makeText(this, "Registered successfully.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });

        btnRegLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    private boolean validateEmail(String email) {
        String emailPattern = "^([a-z0-9_]+)@([a-z0-9]+).([a-z]{2,5})(.[a-z]{2,5})?$";

        if (email.isEmpty()) {
            etEmail.setError("Email cannot be empty");
            return false;
        } else if (!email.matches(emailPattern)) {
            etEmail.setError("Invalid Email Address");
            return false;
        } else {
            etEmail.setError( null);
            etEmail.isEnabled();
            return true;
        }
    }
    private boolean validatePassword(String password) {
        if(password.length()<4){
            etPassword.setError("password must be at least 4 digit long.");
            return false;

        }
        return true;
    }

}