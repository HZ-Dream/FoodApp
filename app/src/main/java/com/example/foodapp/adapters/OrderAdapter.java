package com.example.foodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodapp.R;
import com.example.foodapp.models.Orders;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    public interface OnOrderDetailClickListener {
        void onDetailClick(long orderId);

        void onDeleteClick(long orderId);
    }

    private final List<Orders> orderList;
    private final OnOrderDetailClickListener listener;

    public OrderAdapter(List<Orders> orderList, OnOrderDetailClickListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    public void updateOrderList(List<Orders> newOrderList) {
        this.orderList.clear();
        this.orderList.addAll(newOrderList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Orders order = orderList.get(position);

        // Binding data to views
        holder.tvOrderId.setText("Id: " + order.getId());
        holder.tvOrderDate.setText(" - " + order.getDateOrdered());
        holder.tvOrderStatus.setText(order.getStatus());
        holder.tvOrderPrice.setText("" + order.getSubTotal());

        holder.btnDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDetailClick(order.getId());
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(order.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvOrderStatus, tvOrderPrice;
        ImageView btnDetail, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.idOrderView);
            tvOrderDate = itemView.findViewById(R.id.dateOrderView);
            tvOrderStatus = itemView.findViewById(R.id.statusOrder);
            tvOrderPrice = itemView.findViewById(R.id.orderItemPrice);
            btnDetail = itemView.findViewById(R.id.btnDetailOrder);
            btnDelete = itemView.findViewById(R.id.btnDeleteOrder);
        }
    }
}