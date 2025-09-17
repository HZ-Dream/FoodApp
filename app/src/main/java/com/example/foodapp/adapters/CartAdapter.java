package com.example.foodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.datas.CartsDAO;
import com.example.foodapp.models.CartItem;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    public interface OnCartUpdatedListener {
        void onCartUpdated();
    }
    private final OnCartUpdatedListener listener;

    private final Context context;
    private final List<CartItem> list;
    private final CartsDAO cartsDAO;

    public CartAdapter(Context context, List<CartItem> list, OnCartUpdatedListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.cartsDAO = new CartsDAO(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mycart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = list.get(position);
        int imageId = context.getResources().getIdentifier(item.getProductImage(), "drawable", context.getPackageName());

        holder.imageView.setImageResource(imageId);
        holder.name.setText(item.getProductName());
        holder.quantity.setText("" + item.getQuantity());
        holder.price.setText("" + item.getProductPrice());

        holder.addQuantity.setOnClickListener(v -> {
            cartsDAO.changeQuantityCart(item.getCartId(), "plus");
            if (listener != null) listener.onCartUpdated();
        });

        holder.minusQuantity.setOnClickListener(v -> {
            cartsDAO.changeQuantityCart(item.getCartId(), "minus");
            if (listener != null) listener.onCartUpdated();
        });

        holder.btnRemove.setOnClickListener(v -> {
            cartsDAO.removeCart(item.getCartId());
            Toast.makeText(context, "Removed " + item.getProductName(), Toast.LENGTH_SHORT).show();
            if (listener != null) listener.onCartUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, addQuantity, minusQuantity;
        Button btnRemove;
        TextView name, quantity, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.detailed_img);
            name = itemView.findViewById(R.id.detailed_name);
            quantity = itemView.findViewById(R.id.detailed_quantity);
            price = itemView.findViewById(R.id.cartItemPrice);
            btnRemove = itemView.findViewById(R.id.btnRemoveCart);
            addQuantity = itemView.findViewById(R.id.btnAddQuantity);
            minusQuantity = itemView.findViewById(R.id.btnMinusQuantity);
        }
    }
}