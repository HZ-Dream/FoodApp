package com.example.foodapp.ui.favourite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodapp.R;
import com.example.foodapp.fragments.FragmentAdapter;

public class FavouriteFragment extends Fragment {

    ViewPager2 viewPager2;
    FragmentAdapter fragmentAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.favourite_fragment, container, false);

        viewPager2 = root.findViewById(R.id.view_pager2);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter(fm,getLifecycle());

        viewPager2.setAdapter(fragmentAdapter);

        return root;
    }
}