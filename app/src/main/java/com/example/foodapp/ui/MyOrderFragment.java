package com.example.foodapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.adapters.OrderAdapter;
import com.example.foodapp.adapters.OrderDetailAdapter;
import com.example.foodapp.datas.OrdersDAO;
import com.example.foodapp.models.OrderDetails;
import com.example.foodapp.models.Orders;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class MyOrderFragment extends Fragment implements OrderAdapter.OnOrderDetailClickListener {

    private RecyclerView recyclerView;
    private TextView emptyOrderTextView;
    private OrdersDAO ordersDAO;
    private OrderAdapter orderAdapter;

    public MyOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.order_rec);
        emptyOrderTextView = view.findViewById(R.id.empty_order_text);
        ordersDAO = new OrdersDAO(requireActivity());

        SharedPreferences prefs = requireActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            recyclerView.setVisibility(View.GONE);
            emptyOrderTextView.setText("Please log in to see your orders");
            emptyOrderTextView.setVisibility(View.VISIBLE);
            return;
        }

        List<Orders> orderList = ordersDAO.getOrdersByUserId(userId);

        if (orderList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyOrderTextView.setText("You have no orders yet");
            emptyOrderTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyOrderTextView.setVisibility(View.GONE);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            orderAdapter = new OrderAdapter(orderList, this);
            recyclerView.setAdapter(orderAdapter);
        }
    }

    @Override
    public void onDetailClick(long orderId) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View sheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_order_details, null);

        TextView tvTitle = sheetView.findViewById(R.id.tv_order_detail_title);
        RecyclerView rvDetails = sheetView.findViewById(R.id.rv_order_details);
        Button btnClose = sheetView.findViewById(R.id.btn_close_sheet);

        tvTitle.setText("Order Details #" + orderId);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        List<OrderDetails> detailsList = ordersDAO.getOrderDetails(orderId);

        rvDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        OrderDetailAdapter detailAdapter = new OrderDetailAdapter(detailsList);
        rvDetails.setAdapter(detailAdapter);

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    @Override
    public void onDeleteClick(long orderId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        dialogBuilder.setTitle("Do you want to delete this order?");
        dialogBuilder.setMessage("Please confirm information again before deleting!");
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                boolean check = ordersDAO.deleteOrder(orderId);
                if(check) {
                    Toast.makeText(requireContext(), "Delete successfully!", Toast.LENGTH_SHORT).show();
                    loadOrdersAndUpdateView();
                } else {
                    Toast.makeText(requireContext(), "Can't delete this order!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.show();
    }

    private void loadOrdersAndUpdateView() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            recyclerView.setVisibility(View.GONE);
            emptyOrderTextView.setText("Please log in to see your orders");
            emptyOrderTextView.setVisibility(View.VISIBLE);
            if (orderAdapter != null) {
                orderAdapter.updateOrderList(new ArrayList<>());
            }
            return;
        }

        List<Orders> updatedOrderList = ordersDAO.getOrdersByUserId(userId);

        if (updatedOrderList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyOrderTextView.setText("You have no orders yet");
            emptyOrderTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyOrderTextView.setVisibility(View.GONE);
        }

        if (orderAdapter != null) {
            orderAdapter.updateOrderList(updatedOrderList);
        } else {
            orderAdapter = new OrderAdapter(updatedOrderList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(orderAdapter);
        }
    }

}