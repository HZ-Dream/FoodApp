package com.example.foodapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.models.HomeHorModel;
import com.example.foodapp.models.HomeVerModel;

import java.util.ArrayList;
import java.util.List;

public class HomeHorAdapter extends RecyclerView.Adapter<HomeHorAdapter.ViewHolder> {
    UpdateVerticalRec updateVerticalRec;
    Activity activity;
    ArrayList<HomeHorModel> list;

    boolean check = true;
    boolean select = true;
    int row_index = -1;

    public HomeHorAdapter(UpdateVerticalRec updateVerticalRec, Activity activity, ArrayList<HomeHorModel> list) {
        this.updateVerticalRec = updateVerticalRec;
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeHorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHorAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.textView.setText(list.get(position).getName());

        if (select && position == 0) {
            holder.cardView.setBackgroundResource(R.drawable.change_bg);
            select = false;
        } else {
            if (row_index == position) {
                holder.cardView.setBackgroundResource(R.drawable.change_bg);
            } else {
                holder.cardView.setBackgroundResource(R.drawable.default_bg);
            }
        }

        if (check && position == 0) {
            ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
            homeVerModels.add(new HomeVerModel(R.drawable.pizza1, "Pizza 1", "4.9", "10:00 - 22:00", "$10"));
            homeVerModels.add(new HomeVerModel(R.drawable.pizza2, "Pizza 2", "4.5", "10:00 - 22:00", "$15"));
            homeVerModels.add(new HomeVerModel(R.drawable.pizza3, "Pizza 3", "4.0", "10:00 - 22:00", "$12"));
            homeVerModels.add(new HomeVerModel(R.drawable.pizza4, "Pizza 4", "3.9", "10:00 - 22:00", "$9"));

            updateVerticalRec.callBack(position, homeVerModels);
            check = false;
        }

        holder.cardView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            row_index = pos;
            notifyDataSetChanged();

            ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
            switch (pos) {
                case 0: // Pizza
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza1, "Pizza 1", "4.9", "10:00 - 22:00", "$10"));
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza2, "Pizza 2", "4.5", "10:00 - 22:00", "$15"));
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza3, "Pizza 3", "4.0", "10:00 - 22:00", "$12"));
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza4, "Pizza 4", "3.9", "10:00 - 22:00", "$9"));
                    break;

                case 1: // Burger
                    homeVerModels.add(new HomeVerModel(R.drawable.burger1, "Burger 1", "4.4", "10:00 - 22:00", "$15"));
                    homeVerModels.add(new HomeVerModel(R.drawable.burger2, "Burger 2", "3.5", "10:00 - 22:00", "$18"));
                    homeVerModels.add(new HomeVerModel(R.drawable.burger4, "Burger 4", "5.0", "10:00 - 22:00", "$22"));
                    break;

                case 2: // Fries
                    homeVerModels.add(new HomeVerModel(R.drawable.fries1, "Fries 1", "4.9", "10:00 - 22:00", "$10"));
                    homeVerModels.add(new HomeVerModel(R.drawable.fries2, "Fries 2", "4.5", "10:00 - 22:00", "$15"));
                    homeVerModels.add(new HomeVerModel(R.drawable.fries3, "Fries 3", "4.0", "10:00 - 22:00", "$12"));
                    homeVerModels.add(new HomeVerModel(R.drawable.fries4, "Fries 4", "3.9", "10:00 - 22:00", "$9"));
                    break;

                case 3: // Ice Cream
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream1, "Ice Cream 1", "4.9", "10:00 - 22:00", "$10"));
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream2, "Ice Cream 2", "4.5", "10:00 - 22:00", "$15"));
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream3, "Ice Cream 3", "4.0", "10:00 - 22:00", "$12"));
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream4, "Ice Cream 4", "3.9", "10:00 - 22:00", "$9"));
                    break;

                case 4: // Sandwich
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich1, "Sandwich 1", "4.9", "10:00 - 22:00", "$10"));
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich2, "Sandwich 2", "4.5", "10:00 - 22:00", "$15"));
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich3, "Sandwich 3", "4.0", "10:00 - 22:00", "$12"));
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich4, "Sandwich 4", "3.9", "10:00 - 22:00", "$9"));
                    break;
            }

            updateVerticalRec.callBack(pos, homeVerModels);
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
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
