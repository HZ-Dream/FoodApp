package com.example.foodapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.MainActivity;
import com.example.foodapp.R;
import com.example.foodapp.datas.UsersDAO;
import com.example.foodapp.datas.dbConnect;
import com.example.foodapp.models.Users;

public class LoginActivity extends AppCompatActivity {
    UsersDAO usersDAO;
    EditText edtEmailLog, edtPasswordLog;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        usersDAO = new UsersDAO(this);

        edtEmailLog = findViewById(R.id.edtEmailLog);
        edtPasswordLog = findViewById(R.id.edtPasswordLog);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailLog.getText().toString();
                String password = edtPasswordLog.getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }else{
                    boolean checkLogin = usersDAO.checkLogin(email, password);
                    if(checkLogin){
                        boolean isAdmin = usersDAO.checkAdmin(email);
                        String name = usersDAO.getUserNameByEmail(email);
                        int userId = usersDAO.getUserIdByEmail(email);
                        SharedPreferences prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();

                        editor.putBoolean("isLoggedIn", true);
                        editor.putInt("userId", userId);
                        editor.putString("email", email);
                        editor.putString("name", name);

                        if(isAdmin){
                            editor.putBoolean("isAdmin", true);
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Admin Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            editor.putBoolean("isAdmin", false);
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else{
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }

    public void mainActivity(View view) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}