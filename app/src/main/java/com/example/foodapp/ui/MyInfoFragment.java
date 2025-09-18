package com.example.foodapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.foodapp.R;
import com.example.foodapp.databinding.FragmentUserInfoBinding;
import com.example.foodapp.datas.UsersDAO;
import com.example.foodapp.models.Users;

import java.util.List;

public class MyInfoFragment extends Fragment {

    private FragmentUserInfoBinding binding;
    private UsersDAO usersDAO;
    private Users currentUser;

    public MyInfoFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usersDAO = new UsersDAO(requireActivity());

        loadUserData();

        binding.userInfoBtnSave.setOnClickListener(v -> {
            saveUserData();
        });

        binding.userInfoBtnCancel.setOnClickListener(v -> {
            NavHostFragment.findNavController(MyInfoFragment.this).popBackStack();
        });
    }

    private void loadUserData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId != -1) {
            List<Users> usersList = usersDAO.getUserByUserId(userId);
            if (usersList != null && !usersList.isEmpty()) {
                currentUser = usersList.get(0);
                populateUI(currentUser);
            } else {
                Toast.makeText(requireActivity(), "User not found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void populateUI(Users user) {
        binding.userInfoName.setText(user.getName());
        binding.userInfoEmail.setText(user.getEmail());
        binding.userInfoPhone.setText(user.getPhone());
        binding.userInfoPassword.setText(user.getPassword());
    }

    private void saveUserData() {
        if (currentUser == null) {
            Toast.makeText(requireActivity(), "No user data to save.", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = binding.userInfoName.getText().toString().trim();
        String email = binding.userInfoEmail.getText().toString().trim();
        String phone = binding.userInfoPhone.getText().toString().trim();
        String password = binding.userInfoPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireActivity(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(usersDAO.checkEmail(email) && !email.equals(currentUser.getEmail())) {
            Toast.makeText(requireActivity(), "Email already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setName(name);
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setPassword(password);

        int rowsAffected = usersDAO.updateUser(currentUser);

        if (rowsAffected > 0) {
            SharedPreferences prefs = requireActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("name", name);
            editor.putString("email", email);
            editor.apply();
            Toast.makeText(requireActivity(), "Information saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), "Failed to save information.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}