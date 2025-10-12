package com.example.foodapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.datas.CartsDAO;
import com.example.foodapp.datas.UsersDAO;
import com.example.foodapp.models.Carts;
import com.example.foodapp.models.Products;
import java.io.File;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private final Context context;
    private final List<Products> list;
    private final UsersDAO usersDAO;
    private final CartsDAO cartsDAO;
    private final int userId;

    public FavouriteAdapter(Context context, List<Products> list) {
        this.context = context;
        this.list = list;

        this.usersDAO = new UsersDAO(context);
        this.cartsDAO = new CartsDAO(context);
        SharedPreferences prefs = context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        this.userId = prefs.getInt("userId", -1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_ver_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products product = list.get(position);

        holder.name.setText(product.getName());
        holder.timing.setText(product.getTimeCook());
        holder.pricing.setText("" + product.getPrice());

        String imageName = product.getImage();
        File imageFile = new File(context.getFilesDir(), imageName);

        if (imageFile.exists()) {
            Glide.with(context).load(imageFile).into(holder.imageView);
        } else {
            int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            Glide.with(context).load(resId != 0 ? resId : R.drawable.placeholder_image).into(holder.imageView);
        }

        holder.addWishListButton.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(context, "Please log in to modify your wishlist", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(context)
                    .setTitle("Remove from Wishlist")
                    .setMessage("Are you sure you want to remove '" + product.getName() + "' from your wishlist?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        usersDAO.removeProductFromWishList(userId, product.getId());

                        int currentPosition = holder.getAdapterPosition();
                        list.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                        notifyItemRangeChanged(currentPosition, list.size());

                        Toast.makeText(context, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        holder.addCartButton.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(context, "Please log in to add items to cart", Toast.LENGTH_SHORT).show();
                return;
            }

            int proId = product.getId();
            double price = product.getPrice();
            int quantity = 1;
            double subTotal = price * quantity;

            if (cartsDAO.isProductInCart(userId, proId)) {
                Toast.makeText(context, "Product already in cart", Toast.LENGTH_SHORT).show();
                return;
            }

            cartsDAO.addCart(new Carts(userId, proId, price, quantity, subTotal));
            Toast.makeText(context, "Added '" + product.getName() + "' to cart", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, description, pricing, timing;
        ImageView addWishListButton, addCartButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.detailed_img);
            name = itemView.findViewById(R.id.detailed_name);
            description = itemView.findViewById(R.id.detailed_des);
            pricing = itemView.findViewById(R.id.detailed_pricing);
            timing = itemView.findViewById(R.id.detailed_timing);
            addWishListButton = itemView.findViewById(R.id.addWishList);
            addCartButton = itemView.findViewById(R.id.addCart);
        }
    }
}