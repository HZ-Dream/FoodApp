package com.example.foodapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // <-- Thêm
import com.example.foodapp.R;
import com.example.foodapp.adapters.FavouriteAdapter; // <-- Dùng adapter mới
import com.example.foodapp.datas.ProductsDAO; // <-- Thêm
import com.example.foodapp.datas.UsersDAO; // <-- Thêm
import com.example.foodapp.models.Products; // <-- Dùng model Products
import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavouriteAdapter favouriteAdapter;
    private List<Products> favouriteProductsList;
    private UsersDAO usersDAO;
    private ProductsDAO productsDAO;
    private TextView emptyWishlistText;

    public FavouriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        usersDAO = new UsersDAO(getContext());
        productsDAO = new ProductsDAO(getContext());

        recyclerView = view.findViewById(R.id.featured_ver_rec);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favouriteProductsList = new ArrayList<>();
        favouriteAdapter = new FavouriteAdapter(getContext(), favouriteProductsList);
        recyclerView.setAdapter(favouriteAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavouriteProducts();
    }

    private void loadFavouriteProducts() {
        favouriteProductsList.clear();

        SharedPreferences prefs = requireActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            return;
        }

        String wishListStr = usersDAO.getWishListString(userId);

        if (wishListStr == null || wishListStr.isEmpty()) {
        } else {
            String[] productIds = wishListStr.split(",");

            for (String idStr : productIds) {
                try {
                    int productId = Integer.parseInt(idStr);
                    Products product = productsDAO.getProductById(productId);
                    if (product != null) {
                        favouriteProductsList.add(product);
                    }
                } catch (NumberFormatException e) {
                }
            }
        }

        // Cập nhật lại adapter
        favouriteAdapter.notifyDataSetChanged();
    }
}