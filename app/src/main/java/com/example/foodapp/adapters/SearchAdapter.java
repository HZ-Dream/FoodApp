package com.example.foodapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.datas.CartsDAO;
import com.example.foodapp.datas.UsersDAO;
import com.example.foodapp.models.Carts;
import com.example.foodapp.models.Products;
import java.io.File;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final Context context;
    private List<Products> list;
    private final UsersDAO usersDAO;
    private final CartsDAO cartsDAO;
    private final int userId;

    public SearchAdapter(Context context, List<Products> list) {
        this.context = context;
        this.list = list;
        this.usersDAO = new UsersDAO(context);
        this.cartsDAO = new CartsDAO(context);
        SharedPreferences prefs = context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        this.userId = prefs.getInt("userId", -1);
    }

    public void updateData(List<Products> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.search_ver_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products product = list.get(position);

        holder.name.setText(product.getName());
        holder.timing.setText(product.getTimeCook());
        holder.price.setText(String.valueOf(product.getPrice()));

        String imageName = product.getImage();
        File imageFile = new File(context.getFilesDir(), imageName);
        if (imageFile.exists()) {
            Glide.with(context).load(imageFile).into(holder.imageView);
        } else {
            int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            Glide.with(context).load(resId != 0 ? resId : R.drawable.placeholder_image).into(holder.imageView);
        }

        updateWishlistIcon(holder.addWishList, product.getId());

        holder.addWishList.setOnClickListener(v -> handleWishlistClick(holder.addWishList, product.getId()));
        holder.addCart.setOnClickListener(v -> handleCartClick(product));
    }

    private void updateWishlistIcon(ImageView icon, int productId) {
        if (usersDAO.isProductInWishList(userId, productId)) {
            icon.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            icon.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
    }

    private void handleWishlistClick(ImageView icon, int productId) {
        if (userId == -1) {
            Toast.makeText(context, "Please log in", Toast.LENGTH_SHORT).show();
            return;
        }
        if (usersDAO.isProductInWishList(userId, productId)) {
            usersDAO.removeProductFromWishList(userId, productId);
            icon.setImageResource(R.drawable.ic_baseline_favorite_24);
            Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
        } else {
            usersDAO.addProductToWishList(userId, productId);
            icon.setImageResource(R.drawable.baseline_favorite_24);
            Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCartClick(Products product) {
        if (userId == -1) {
            Toast.makeText(context, "Please log in", Toast.LENGTH_SHORT).show();
            return;
        }
        cartsDAO.addCart(new Carts(userId, product.getId(), product.getPrice(), 1, product.getPrice()));
        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, addWishList, addCart;
        TextView name, price, timing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_img);
            name = itemView.findViewById(R.id.search_name);
            price = itemView.findViewById(R.id.search_pricing);
            timing = itemView.findViewById(R.id.search_timing);
            addWishList = itemView.findViewById(R.id.addWishListSearch);
            addCart = itemView.findViewById(R.id.addCartSearch);
        }
    }
}