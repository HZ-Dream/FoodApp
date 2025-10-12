package com.example.foodapp.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.adapters.HomeHorAdapter;
import com.example.foodapp.adapters.HomeVerAdapter;
import com.example.foodapp.adapters.SearchAdapter;
import com.example.foodapp.adapters.UpdateVerticalRec;
import com.example.foodapp.databinding.FragmentHomeBinding;
import com.example.foodapp.datas.CategoriesDAO;
import com.example.foodapp.datas.OrdersDAO;
import com.example.foodapp.datas.ProductsDAO;
import com.example.foodapp.models.Categories;
import com.example.foodapp.models.Products;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements UpdateVerticalRec {
    CategoriesDAO categoriesDAO;
    ProductsDAO productsDAO;
    OrdersDAO ordersDAO;
    List<Products> productsList;
    HomeHorAdapter homeHorAdapter;
    HomeVerAdapter homeVerAdapter;
    private FragmentHomeBinding binding;
    private SearchAdapter searchAdapter;
    private List<Products> searchResultList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "Guest");
        binding.tvHello.setText("Hello, " + name);

        categoriesDAO = new CategoriesDAO(requireActivity());
        productsDAO = new ProductsDAO(requireActivity());
        ordersDAO = new OrdersDAO(requireActivity());

        // Setup Horizontal RecyclerView
        List<Categories> categoryList = categoriesDAO.getAllCategories();
        homeHorAdapter = new HomeHorAdapter(this, requireActivity(), categoryList);
        binding.homeHorRec.setAdapter(homeHorAdapter);
        binding.homeHorRec.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false));
        binding.homeHorRec.setHasFixedSize(true);
        binding.homeHorRec.setNestedScrollingEnabled(false);

        // Setup Vertical RecyclerView
        productsList = new ArrayList<>();
        homeVerAdapter = new HomeVerAdapter(requireActivity(), productsList);
        binding.homeVerRec.setAdapter(homeVerAdapter);
        binding.homeVerRec.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));

        // Setup Search RecyclerView
        searchResultList = new ArrayList<>();
        searchAdapter = new SearchAdapter(requireActivity(), searchResultList);
        binding.searchRec.setAdapter(searchAdapter);
        binding.searchRec.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // Lắng nghe sự kiện thay đổi text trong ô tìm kiếm
        binding.txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                performSearch(s.toString());
            }
        });

        // (Tùy chọn) Lắng nghe sự kiện click nút search
        binding.btnSearch.setOnClickListener(v -> {
            performSearch(binding.txtSearch.getText().toString());
        });
    }

    private void performSearch(String query) {
        String trimmedQuery = query.trim();

        if (trimmedQuery.isEmpty()) {
            // Nếu chuỗi rỗng, hiện lại giao diện mặc định
            binding.homeHorRec.setVisibility(View.VISIBLE);
            binding.homeVerRec.setVisibility(View.VISIBLE);
            binding.searchRec.setVisibility(View.GONE);
        } else {
            // Nếu có từ khóa, ẩn giao diện mặc định và hiện kết quả tìm kiếm
            binding.homeHorRec.setVisibility(View.GONE);
            binding.homeVerRec.setVisibility(View.GONE);
            binding.searchRec.setVisibility(View.VISIBLE);

            // Lấy kết quả từ DB và cập nhật adapter
            List<Products> results = productsDAO.searchProductsByName(trimmedQuery);
            searchAdapter.updateData(results);
        }
    }

    @Override
    public void callBack(int position, List<Products> list) {
        homeVerAdapter.updateData(list);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = requireActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "Guest");
        binding.tvHello.setText("Hello, " + name);
    }
}