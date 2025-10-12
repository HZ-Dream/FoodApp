package com.example.foodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.foodapp.R;
import com.example.foodapp.models.Products;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Products> {

    public ProductAdapter(@NonNull Context context, @NonNull List<Products> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_custom_listview, parent, false);
        }

        Products product = getItem(position);
        TextView tvName = convertView.findViewById(R.id.tvCustomItem);

        if (product != null) {
            tvName.setText(product.toString());
        }

        return convertView;
    }
}