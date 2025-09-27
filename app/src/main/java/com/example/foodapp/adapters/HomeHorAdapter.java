package com.example.foodapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodapp.R;
import com.example.foodapp.datas.ProductsDAO;
import com.example.foodapp.models.Categories;
import com.example.foodapp.models.Products;
import java.util.List;

import com.bumptech.glide.Glide;
import java.io.File;

public class HomeHorAdapter extends RecyclerView.Adapter<HomeHorAdapter.ViewHolder> {
    UpdateVerticalRec updateVerticalRec;
    Activity activity;
    List<Categories> categoriesList;
    ProductsDAO productsDAO;

    boolean isFirstLoad = true;
    int rowIndex = -1;

    public HomeHorAdapter(UpdateVerticalRec updateVerticalRec, Activity activity, List<Categories> list) {
        this.updateVerticalRec = updateVerticalRec;
        this.activity = activity;
        this.categoriesList = list;
        this.productsDAO = new ProductsDAO(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categories category = categoriesList.get(position);
        holder.textView.setText(category.getName());

        String imageName = category.getImage();

        File imageFile = new File(activity.getFilesDir(), imageName);

        if (imageFile.exists()) {
            Glide.with(activity)
                    .load(imageFile)
                    .into(holder.imageView);
        } else {
            int resId = activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName());
            if (resId != 0) {
                Glide.with(activity)
                        .load(resId)
                        .into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.placeholder_image);
            }
        }

        if (rowIndex == position) {
            holder.cardView.setBackgroundResource(R.drawable.change_bg);
        } else {
            holder.cardView.setBackgroundResource(R.drawable.default_bg);
        }

        if (isFirstLoad && position == 0) {
            rowIndex = 0;
            holder.cardView.setBackgroundResource(R.drawable.change_bg);

            int firstCategoryId = categoriesList.get(0).getId();

            List<Products> initialProducts = productsDAO.getProductsByCategoryId(firstCategoryId);
            updateVerticalRec.callBack(0, initialProducts);
            isFirstLoad = false;
        }

        holder.cardView.setOnClickListener(v -> {
            int previousIndex = rowIndex;
            rowIndex = holder.getAdapterPosition();
            if (rowIndex == RecyclerView.NO_POSITION) return;

            int categoryId = categoriesList.get(rowIndex).getId();

            List<Products> productsList = productsDAO.getProductsByCategoryId(categoryId);

            updateVerticalRec.callBack(rowIndex, productsList);

            notifyItemChanged(previousIndex);
            notifyItemChanged(rowIndex);
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.hor_img);
            textView = itemView.findViewById(R.id.hor_text);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}