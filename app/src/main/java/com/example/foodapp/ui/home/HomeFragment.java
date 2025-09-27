package com.example.foodapp.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        // resetData();
        addInitialData();

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
    }

    private void resetData() {
        productsDAO.deleteAllProduct();
        categoriesDAO.deleteAllCategory();
        ordersDAO.deleteAllOrder();
        ordersDAO.deleteAllOrderDetails();
    }

    private void addInitialData() {
//        if (categoriesDAO.getAllCategories().isEmpty()) {
//            categoriesDAO.addCategory(new Categories(1, "Pizza", "pizza"));
//            categoriesDAO.addCategory(new Categories(2, "Burger", "burger"));
//            categoriesDAO.addCategory(new Categories(3, "Fries", "fries"));
//            categoriesDAO.addCategory(new Categories(4, "Ice Cream", "icecream"));
//            categoriesDAO.addCategory(new Categories(5, "Sandwich", "sandwich"));
//        }

        if (productsDAO.getAllProducts().isEmpty()) {
            // catId = 1 (Pizza)
            productsDAO.addProduct(new Products("Pizza", 10.0, "10 minutes", "pizza1", 1));
            productsDAO.addProduct(new Products("Pizza 2", 15.0, "15 minutes", "pizza2", 1));
            productsDAO.addProduct(new Products("Pizza 3", 12.5, "12 minutes", "pizza3", 1));
            productsDAO.addProduct(new Products("Pizza 4", 12.5, "10 minutes", "pizza4", 1));

            // catId = 2 (Burger)
            productsDAO.addProduct(new Products("Burger 1", 15.0, "15 minutes", "burger1", 2));
            productsDAO.addProduct(new Products("Burger 2", 18.0, "20 minutes", "burger2", 2));
            productsDAO.addProduct(new Products("Burger 4", 18.0, "10 minutes", "burger4", 2));

            // catId = 3 (Fries)
            productsDAO.addProduct(new Products("Fries 1", 10.0, "10 minutes", "fries1", 3));
            productsDAO.addProduct(new Products("Fries", 15.0, "10 minutes", "fries2", 3));
            productsDAO.addProduct(new Products("Fries 3", 10.0, "14 minutes", "fries3", 3));
            productsDAO.addProduct(new Products("Fries 4", 15.0, "15 minutes", "fries4", 3));

            // catId = 4 (Ice Cream)
            productsDAO.addProduct(new Products("Ice Cream 1", 10.0, "5 minutes", "icecream1", 4));
            productsDAO.addProduct(new Products("Ice Cream 2", 10.0, "10 minutes", "icecream2", 4));
            productsDAO.addProduct(new Products("Ice Cream 3", 10.0, "5 minutes", "icecream3", 4));
            productsDAO.addProduct(new Products("Ice Cream 4", 10.0, "6 minutes", "icecream4", 4));

            // catId = 5 (Sandwich)
            productsDAO.addProduct(new Products("Sandwich 1", 10.0, "10 minutes", "sandwich1", 5));
            productsDAO.addProduct(new Products("Sandwich 2", 10.0, "12 minutes", "sandwich2", 5));
            productsDAO.addProduct(new Products("Sandwich 3", 10.0, "14 minutes", "sandwich3", 5));
            productsDAO.addProduct(new Products("Sandwich", 10.0, "10 minutes", "sandwich4", 5));
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