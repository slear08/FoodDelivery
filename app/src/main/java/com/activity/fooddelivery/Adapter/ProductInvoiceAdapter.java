package com.activity.fooddelivery.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.activity.fooddelivery.Domain.FoodDomain;
import com.activity.fooddelivery.R;

import java.util.ArrayList;

public class ProductInvoiceAdapter extends RecyclerView.Adapter<ProductInvoiceAdapter.ViewHolder> {

    private ArrayList<FoodDomain> productList;

    public ProductInvoiceAdapter(ArrayList<FoodDomain> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodDomain product = productList.get(position);

        holder.tvProductTitle.setText(product.getTitle());
        holder.tvProductPrice.setText("$" + product.getPrice());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductTitle;
        public TextView tvProductPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductTitle = itemView.findViewById(R.id.tvProductTitle);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }
}
