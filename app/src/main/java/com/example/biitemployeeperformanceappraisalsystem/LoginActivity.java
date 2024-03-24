package com.example.biitemployeeperformanceappraisalsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.biitemployeeperformanceappraisalsystem.network.services.LoginService;

public class LoginActivity extends AppCompatActivity {
    EditText emailOrAridNo, password;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailOrAridNo = findViewById(R.id.emailOrAridNo);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login_button);

        LoginService login=new LoginService(LoginActivity.this);

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
}
