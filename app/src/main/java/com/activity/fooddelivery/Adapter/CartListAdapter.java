package com.activity.fooddelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.activity.fooddelivery.Domain.FoodDomain;
import com.activity.fooddelivery.Helper.ManagementCart;
import com.activity.fooddelivery.Interface.ChangeNumberItemListener;
import com.activity.fooddelivery.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    ArrayList<FoodDomain> foodListSelected;
    private ManagementCart managementCart;
    ChangeNumberItemListener changeNumberItemListener;

    public CartListAdapter(ArrayList<FoodDomain> foodList, Context context, ChangeNumberItemListener changeNumberItemListener) {
        this.foodListSelected = foodList;
        managementCart = new ManagementCart(context);
        this.changeNumberItemListener = changeNumberItemListener;
    }

    @NonNull
    @Override
    public CartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cart_holder,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        holder.productName.setText(foodListSelected.get(position).getImage());
        holder.productPriceEachItem.setText("$ "+foodListSelected.get(position).getPrice());
        holder.totalEachItem.setText("$ "+Math.round((foodListSelected.get(position).getCartNumber()*foodListSelected.get(position).getPrice())));
        holder.quantity.setText(String.valueOf(foodListSelected.get(position).getCartNumber()));

        int drawableResourceID = holder.itemView
                .getContext().getResources().
                    getIdentifier(foodListSelected.get(position).getImage(),"drawable",
                        holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext()).load(drawableResourceID).into(holder.productImage);


        int currentPosition = holder.getAdapterPosition();

        holder.addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                managementCart.increaseNumberFood(foodListSelected, currentPosition, new ChangeNumberItemListener() {
                    @Override
                    public void changed() {
                            notifyDataSetChanged();
                            changeNumberItemListener.changed();
                    }
                });

            }
        });

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                managementCart.decreaseNumberFood(foodListSelected, currentPosition, new ChangeNumberItemListener() {
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeNumberItemListener.changed();

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return foodListSelected.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productPriceEachItem,addBtn,minusBtn;
        ImageView productImage;
        TextView totalEachItem,quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.cartTitle);
            productPriceEachItem = itemView.findViewById(R.id.cartItemPrice);
            productImage = itemView.findViewById(R.id.cartImage);
            totalEachItem = itemView.findViewById(R.id.cartItemTotal);
            addBtn = itemView.findViewById(R.id.cartProductAddBtn);
            minusBtn = itemView.findViewById(R.id.cartProductMinusBtn);
            quantity = itemView.findViewById(R.id.cartProductQuantity);


        }
    }
}
