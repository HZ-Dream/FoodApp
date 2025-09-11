package com.example.foodapp.ui.dailymeal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.adapters.DailyMealAdapter;
import com.example.foodapp.models.DailyMealModel;

import java.util.ArrayList;
import java.util.List;

public class DailyMealFragment extends Fragment {
    RecyclerView recyclerView;
    List<DailyMealModel> dailyMealModels;
    DailyMealAdapter dailyMealAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.daily_meal_fragment, container, false);

        recyclerView = root.findViewById(R.id.daily_meal_rec);
        dailyMealModels = new ArrayList<>();

        dailyMealModels.add(new DailyMealModel(R.drawable.breakfast, "Breakfast", "20% OFF", "Breakfast Description","breakfast"));
        dailyMealModels.add(new DailyMealModel(R.drawable.lunch, "Lunch", "30% OFF", "Lunch Description","lunch"));
        dailyMealModels.add(new DailyMealModel(R.drawable.dinner, "Dinner", "10% OFF", "Dinner Description","dinner"));
        dailyMealModels.add(new DailyMealModel(R.drawable.sweets, "Sweets", "15% OFF", "Sweets Description","sweets"));
        dailyMealModels.add(new DailyMealModel(R.drawable.coffee, "Coffee", "10% OFF", "Coffee Description","coffee"));

        dailyMealAdapter = new DailyMealAdapter(getContext(), dailyMealModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(dailyMealAdapter);
        dailyMealAdapter.notifyDataSetChanged();

        return root;
    }
}