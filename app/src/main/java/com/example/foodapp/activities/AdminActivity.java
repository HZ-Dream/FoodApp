package com.example.foodapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityAdminBinding;
import com.google.android.material.navigation.NavigationView;

public class AdminActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogout.setOnClickListener(v -> {
            finish();
        });

        setSupportActionBar(binding.appBarAdmin.toolbar);

        DrawerLayout drawer = binding.drawerAdminLayout;
        NavigationView navigationView = binding.navAdminView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_admin_home, R.id.nav_admin_category, R.id.nav_admin_product, R.id.nav_admin_order, R.id.nav_admin_user)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        updateNavigationHeader();
    }

    private void updateNavigationHeader() {
        NavigationView navigationView = binding.navAdminView;
        View headerView = navigationView.getHeaderView(0);
        TextView viewName = headerView.findViewById(R.id.viewAdminName);
        TextView viewEmail = headerView.findViewById(R.id.viewAdminEmail);

        SharedPreferences prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String name = prefs.getString("name", "null");
        String email = prefs.getString("email", "null@gmail.com");

        viewName.setText(name);
        viewEmail.setText(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}