package com.example.foodapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.adapters.CartAdapter;
import com.example.foodapp.datas.CartsDAO;
import com.example.foodapp.datas.OrdersDAO;
import com.example.foodapp.models.CartItem;
import com.example.foodapp.models.OrderDetails;
import com.example.foodapp.models.Orders;

import java.util.List;
import java.util.Locale;

public class MyCartFragment extends Fragment implements CartAdapter.OnCartUpdatedListener {

    private RecyclerView recyclerView;
    private TextView emptyCartTextView;
    private TextView totalPriceTextView;
    private Button btnOrder;
    private Group groupCheckout;

    private OrdersDAO ordersDAO;
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
        btnOrder = view.findViewById(R.id.btnOrder);
        groupCheckout = view.findViewById(R.id.group_checkout);

        cartsDAO = new CartsDAO(requireActivity());
        ordersDAO = new OrdersDAO(requireActivity());

        SharedPreferences prefs = requireActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        this.userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            showEmptyView("Vui lòng đăng nhập để xem giỏ hàng");
            return;
        }

        btnOrder.setOnClickListener(v -> showAddressDialog());

        updateCartView();
    }

    private void showAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Nhập địa chỉ giao hàng");

        final EditText edtAddress = new EditText(requireActivity());
        edtAddress.setHint("Nhập địa chỉ của bạn...");
        edtAddress.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        edtAddress.setPadding(50, 40, 50, 40);

        builder.setView(edtAddress);

        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String address = edtAddress.getText().toString().trim();

            if (address.isEmpty()) {
                Toast.makeText(requireActivity(), "Vui lòng nhập địa chỉ!", Toast.LENGTH_SHORT).show();
            } else {
                placeOrder(address);
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void placeOrder(String address) {
        double subTotal = cartsDAO.getTotalPrice(userId);
        String status = "Chưa xác nhận";
        String dateOrdered = new java.text.SimpleDateFormat("HH:mm dd/MM/yyyy", java.util.Locale.getDefault()).format(new java.util.Date());

        Orders orders = new Orders(userId, subTotal, status, dateOrdered, address);
        long orderId = ordersDAO.addOrder(orders);

        if (orderId > 0) {
            List<CartItem> cartItemList = cartsDAO.getCartItemsForUser(userId);
            for (CartItem cartItem : cartItemList) {
                ordersDAO.addOrderDetail(new OrderDetails(orderId, cartItem.getProductName(), cartItem.getQuantity(), cartItem.getProductPrice()));
            }

            cartsDAO.removeCart(userId);

            updateCartView();
            Toast.makeText(requireActivity(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), "Đặt hàng thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCartView() {
        if (userId == -1) {
            showEmptyView("Vui lòng đăng nhập để xem giỏ hàng");
            return;
        }

        List<CartItem> cartItemList = cartsDAO.getCartItemsForUser(userId);

        if (cartItemList.isEmpty()) {
            showEmptyView("Giỏ hàng trống");
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
