package com.example.biitemployeeperformanceappraisalsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.biitemployeeperformanceappraisalsystem.network.ApiNetwork;
import com.example.biitemployeeperformanceappraisalsystem.network.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;

public class LoginActivity extends AppCompatActivity {
    EditText emailOrAridNo, password;
    private Button btnLogin;
    ApiNetwork apiNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailOrAridNo = findViewById(R.id.emailOrAridNo);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login_button);

        RetrofitClient retrofitClient = new RetrofitClient();
        apiNetwork = retrofitClient.getRetrofitInstance().create(ApiNetwork.class);
        CommonData login=new CommonData(LoginActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform login request
                if (emailOrAridNo.getText().toString().toUpperCase().contains("ARID")){
                    login.loginStudent(emailOrAridNo.getText().toString(),password.getText().toString());
                }else {
                    login.loginEmployee(emailOrAridNo.getText().toString(),password.getText().toString());
                }
            }
        });
    }
}
