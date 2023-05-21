package com.activity.fooddelivery.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.activity.fooddelivery.Activities.ShowDetailActivity;
import com.activity.fooddelivery.Domain.FoodDomain;
import com.activity.fooddelivery.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    ArrayList<FoodDomain> foodList;



    public FoodAdapter(ArrayList<FoodDomain> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_product_holder,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {
        holder.productName.setText(foodList.get(position).getImage());
        holder.productPrice.setText("PHP " + String.valueOf(foodList.get(position).getPrice()));


        int drawableResourceID = holder.itemView
                .getContext().getResources().
                    getIdentifier(foodList.get(position).getImage(),"drawable",
                        holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext()).load(drawableResourceID).into(holder.productImage);

        int currentPosition = holder.getAdapterPosition();
        holder.productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ShowDetailActivity.class);
                intent.putExtra("object", foodList.get(currentPosition));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productPrice;
        ImageView productImage;
        Button productBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            productBtn  = itemView.findViewById(R.id.productBtn);
        }
    }
}
