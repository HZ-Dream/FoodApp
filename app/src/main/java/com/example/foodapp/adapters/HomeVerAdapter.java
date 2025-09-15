package com.example.foodapp.adapters;

import static android.content.Context.MODE_PRIVATE;

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

import com.example.foodapp.R;
import com.example.foodapp.datas.CartsDAO;
import com.example.foodapp.models.Carts;
import com.example.foodapp.models.Products;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class HomeVerAdapter extends RecyclerView.Adapter<HomeVerAdapter.ViewHolder> {
    private final CartsDAO cartsDAO;
    private final Context context;
    private final List<Products> list;
    private final int userId;

    public HomeVerAdapter(Context context, List<Products> list) {
        this.context = context;
        this.list = list;

        this.cartsDAO = new CartsDAO(context);
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

        // View product
        int imageId = context.getResources().getIdentifier(product.getImage(), "drawable", context.getPackageName());
        holder.imageView.setImageResource(imageId);
        holder.name.setText(product.getName());
        holder.timing.setText(product.getTimeCook());
        holder.price.setText(String.valueOf(product.getPrice()));

        // Detail product
        int proId = product.getId();
        double price = product.getPrice();
        int quantity = 1;
        double subTotal = price * quantity;

        holder.itemView.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
            View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null);

            sheetView.findViewById(R.id.add_to_cart).setOnClickListener(view -> {
                if (userId == -1) {
                    Toast.makeText(context, "Please log in to add items to cart", Toast.LENGTH_SHORT).show();
                    return;
                }

                cartsDAO.addCart(new Carts(userId, proId, price, quantity, subTotal));

                Toast.makeText(context, "Added to a Cart", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });

            ImageView bottomImg = sheetView.findViewById(R.id.bottom_img);
            TextView bottomName = sheetView.findViewById(R.id.bottom_name);
            TextView bottomPrice = sheetView.findViewById(R.id.bottom_price);
            TextView bottomTiming = sheetView.findViewById(R.id.bottom_timing);

            bottomName.setText(product.getName());
            bottomPrice.setText(String.valueOf(product.getPrice()));
            bottomImg.setImageResource(imageId);
            bottomTiming.setText(product.getTimeCook());

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