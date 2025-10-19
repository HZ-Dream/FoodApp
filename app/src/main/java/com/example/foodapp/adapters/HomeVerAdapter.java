package com.example.foodapp.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.io.File;
import com.example.foodapp.R;
import com.example.foodapp.datas.CartsDAO;
import com.example.foodapp.datas.UsersDAO;
import com.example.foodapp.models.Carts;
import com.example.foodapp.models.Products;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;


public class HomeVerAdapter extends RecyclerView.Adapter<HomeVerAdapter.ViewHolder> {
    private final CartsDAO cartsDAO;
    private final UsersDAO usersDAO;
    private final Context context;
    private final List<Products> list;
    private final int userId;

    public HomeVerAdapter(Context context, List<Products> list, UsersDAO usersDAO, CartsDAO cartsDAO) {
        this.context = context;
        this.list = list;

        this.cartsDAO = cartsDAO;
        this.usersDAO = usersDAO;

        SharedPreferences prefs = context.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        this.userId = prefs.getInt("userId", -1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_vertical_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products product = list.get(position);

        holder.name.setText(product.getName());
        holder.timing.setText(product.getTimeCook());
        holder.price.setText(String.valueOf(product.getPrice()));

        try {
            String imagePath = product.getImage();
            File imageFile = new File(context.getFilesDir(), imagePath);

            if (imageFile.exists()) {
                Glide.with(context)
                        .load(Uri.fromFile(imageFile))
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(holder.imageView);
            } else {
                int imageId = context.getResources().getIdentifier(
                        imagePath.replace(".png", "").replace(".jpg", ""),
                        "drawable",
                        context.getPackageName()
                );
                if (imageId != 0) {
                    holder.imageView.setImageResource(imageId);
                } else {
                    holder.imageView.setImageResource(R.drawable.error_image);
                }
            }
        } catch (Exception e) {
            holder.imageView.setImageResource(R.drawable.error_image);
        }


        int proId = product.getId();
        double price = product.getPrice();
        int quantity = 1;
        double subTotal = price * quantity;

        holder.itemView.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
            View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null);

            ImageView bottomImg = sheetView.findViewById(R.id.bottom_img);
            TextView bottomName = sheetView.findViewById(R.id.bottom_name);
            TextView bottomPrice = sheetView.findViewById(R.id.bottom_price);
            TextView bottomTiming = sheetView.findViewById(R.id.bottom_timing);
            Button addToCart = sheetView.findViewById(R.id.add_to_cart);
            ImageView addToWishList = sheetView.findViewById(R.id.add_to_wishList);

            bottomName.setText(product.getName());
            bottomPrice.setText(String.valueOf(product.getPrice()));
            bottomTiming.setText(product.getTimeCook());

            try {
                String imagePath = product.getImage();
                File imageFile = new File(context.getFilesDir(), imagePath);

                if (imageFile.exists()) {
                    Glide.with(context)
                            .load(Uri.fromFile(imageFile))
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.error_image)
                            .into(bottomImg);
                } else {
                    int imageId = context.getResources().getIdentifier(
                            imagePath.replace(".png", "").replace(".jpg", ""),
                            "drawable",
                            context.getPackageName()
                    );
                    if (imageId != 0) {
                        bottomImg.setImageResource(imageId);
                    } else {
                        bottomImg.setImageResource(R.drawable.error_image);
                    }
                }
            } catch (Exception e) {
                bottomImg.setImageResource(R.drawable.error_image);
            }

            final boolean isFavorite = usersDAO.isProductInWishList(userId, proId);
            if (isFavorite) {
                addToWishList.setImageResource(R.drawable.baseline_favorite_24);
            } else {
                addToWishList.setImageResource(R.drawable.ic_baseline_favorite_24);
            }

            addToWishList.setOnClickListener(view -> {
                if (userId == -1) {
                    Toast.makeText(context, "Please log in to use wish list", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (usersDAO.isProductInWishList(userId, proId)) {
                    usersDAO.removeProductFromWishList(userId, proId);
                    addToWishList.setImageResource(R.drawable.ic_baseline_favorite_24);
                    Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
                } else {
                    usersDAO.addProductToWishList(userId, proId);
                    addToWishList.setImageResource(R.drawable.baseline_favorite_24);
                    Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show();
                }
            });

            addToCart.setOnClickListener(view -> {
                if (userId == -1) {
                    Toast.makeText(context, "Please log in to add items to cart", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (cartsDAO.isProductInCart(userId, proId)) {
                    Toast.makeText(context, "Product already in cart", Toast.LENGTH_SHORT).show();
                    return;
                }

                cartsDAO.addCart(new Carts(userId, proId, price, quantity, subTotal));
                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();
        });
    }



    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void updateData(List<Products> newList) {
        this.list.clear();
        this.list.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, timing, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ver_img);
            name = itemView.findViewById(R.id.name);
            timing = itemView.findViewById(R.id.timeCook);
            price = itemView.findViewById(R.id.price);
        }
    }
}