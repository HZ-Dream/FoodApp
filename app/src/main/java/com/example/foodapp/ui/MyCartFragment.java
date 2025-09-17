package com.example.foodapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.adapters.CartAdapter;
import com.example.foodapp.datas.CartsDAO;
import com.example.foodapp.models.CartItem;

import java.util.List;
import java.util.Locale;

public class MyCartFragment extends Fragment implements CartAdapter.OnCartUpdatedListener {

    private RecyclerView recyclerView;
    private TextView emptyCartTextView;
    private TextView totalPriceTextView;
    private Group groupCheckout;

    private CartAdapter cartAdapter;
    private CartsDAO cartsDAO;
    private int userId;

    public MyCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.cart_rec);
        emptyCartTextView = view.findViewById(R.id.empty_cart_text);
        totalPriceTextView = view.findViewById(R.id.total_price);
        groupCheckout = view.findViewById(R.id.group_checkout);

        cartsDAO = new CartsDAO(requireActivity());

        SharedPreferences prefs = requireActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        this.userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            showEmptyView("Please log in to see your cart");
            return;
        }

        updateCartView();
    }

    private void updateCartView() {
        if (userId == -1) {
            showEmptyView("Please log in to see your cart");
            return;
        }

        List<CartItem> cartItemList = cartsDAO.getCartItemsForUser(userId);

        if (cartItemList.isEmpty()) {
            showEmptyView("Your cart is empty");
        } else {
            showCartView(cartItemList);
        }
    }

    private void showEmptyView(String message) {
        emptyCartTextView.setText(message);
        emptyCartTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        groupCheckout.setVisibility(View.GONE);
    }

    private void showCartView(List<CartItem> cartItemList) {
        emptyCartTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        groupCheckout.setVisibility(View.VISIBLE);

        double totalPrice = cartsDAO.getTotalPrice(userId);
        totalPriceTextView.setText(String.format(Locale.US, "$%.2f", totalPrice));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(requireActivity(), cartItemList, this);
        recyclerView.setAdapter(cartAdapter);
    }

    @Override
    public void onCartUpdated() {
        updateCartView();
    }
}