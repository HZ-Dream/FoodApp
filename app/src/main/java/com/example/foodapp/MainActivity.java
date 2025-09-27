package com.example.foodapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavigationView navigationView = binding.navView;

        updateNavigationHeader();

        binding.btnLogout.setOnClickListener(v -> {
            finish();
        });

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_daily_meal, R.id.nav_favourite, R.id.nav_my_cart, R.id.nav_my_order, R.id.nav_editUser)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(
                this, R.id.nav_host_fragment_content_main);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            updateNavigationHeader();
        });

    }

    private void updateNavigationHeader() {
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);
        TextView viewName = headerView.findViewById(R.id.viewName);
        TextView viewEmail = headerView.findViewById(R.id.viewEmail);

        SharedPreferences prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String name = prefs.getString("name", "Beautiful Person");
        String email = prefs.getString("email", "default@email.com");

        viewName.setText(name);
        viewEmail.setText(email);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavigationHeader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.shutDownApp) {
            shutdownApplication();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shutdownApplication() {
        System.exit(0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}