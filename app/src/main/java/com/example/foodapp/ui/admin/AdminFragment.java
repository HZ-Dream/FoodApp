package com.example.foodapp.ui.admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// THAY ĐỔI IMPORT NÀY
import com.example.foodapp.databinding.FragmentAdminHomeBinding;

public class AdminFragment extends Fragment {
    private FragmentAdminHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "Admin");
        binding.tvHello.setText("Hello, " + name);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = requireActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "Admin");
        binding.tvHello.setText("Hello, " + name);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}