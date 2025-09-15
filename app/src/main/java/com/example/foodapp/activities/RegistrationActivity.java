package com.example.foodapp.activities;

import android.content.Intent;
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

public class RegistrationActivity extends AppCompatActivity {
    UsersDAO usersDAO;
    EditText edtEmailReg, edtNameReg, edtPhoneReg, edtPasswordReg;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        usersDAO = new UsersDAO(this);

        edtEmailReg = findViewById(R.id.edtEmailReg);
        edtNameReg = findViewById(R.id.edtNameReg);
        edtPhoneReg = findViewById(R.id.edtPhoneReg);
        edtPasswordReg = findViewById(R.id.edtPasswordReg);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailReg.getText().toString();
                String name = edtNameReg.getText().toString();
                String phone = edtPhoneReg.getText().toString();
                String password = edtPasswordReg.getText().toString();

                if(email.isEmpty() || name.isEmpty() || phone.isEmpty() || password.isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }else{
                    boolean checkEmail = usersDAO.checkEmail(email);
                    if(checkEmail){
                        Toast.makeText(RegistrationActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                    }else{
                        Users users = new Users(email, name, phone, password);
                        boolean result = usersDAO.addUser(users);
                        if(result){
                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                        }else{
                            Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });
    }

    public void login(View view) {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }

    public void mainActivity(View view) {
        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
    }
}