package com.example.biitemployeeperformanceappraisalsystem;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.network.services.LoginService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;

public class LoginActivity extends AppCompatActivity {
    EditText emailOrAridNo, password;
    private Button btnLogin;
    SessionService sessionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailOrAridNo = findViewById(R.id.emailOrAridNo);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login_button);

        LoginService login=new LoginService(LoginActivity.this);
        sessionService = new SessionService(LoginActivity.this);

        // Example usage
        sessionService.getCurrentSession(
                session -> {
                    // onSuccess: Handle session retrieval success
                    Toast.makeText(LoginActivity.this, "Session ID: " + session.getId(), Toast.LENGTH_SHORT).show();
                    // Proceed with further operations that require session ID
                    // initializeViews(); // Example: Method to initialize views after session fetch
                },
                errorMessage -> {
                    // onFailure: Handle session retrieval failure
                    Toast.makeText(LoginActivity.this, errorMessage+"", Toast.LENGTH_SHORT).show();
                    // Optionally, implement retry logic or notify the user
                    retryFetchSession(); // Example: Method to handle retry logic
                }
        );

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform login request
                if (emailOrAridNo.getText().toString().toUpperCase().contains("@")){
                    login.loginEmployee(emailOrAridNo.getText().toString(),password.getText().toString());
                }else {
                    login.loginStudent(emailOrAridNo.getText().toString(),password.getText().toString());
                }
            }
        });
    }

    // Retry method example
    private void retryFetchSession() {
        // Implement your retry logic here, or show an appropriate UI for retrying
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage("Failed to fetch session. Retry?")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Retry fetching session
                        sessionService.getCurrentSession(
                                session -> {
                                    // onSuccess: Handle session retrieval success after retry
                                    Toast.makeText(LoginActivity.this, "Session ID: " + session.getId(), Toast.LENGTH_SHORT).show();
                                    // initializeViews(); // Example: Method to initialize views after session fetch
                                },
                                errorMessage -> {
                                    // onFailure: Handle session retrieval failure after retry
                                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    retryFetchSession(); // Retry logic within retryFetchSession() method
                                }
                        );
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle app exit or appropriate action
                        LoginActivity.this.finish(); // Example: Close the activity
                    }
                })
                .show();
    }
}
