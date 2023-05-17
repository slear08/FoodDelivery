package com.activity.fooddelivery.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.activity.fooddelivery.Adapter.CartListAdapter;
import com.activity.fooddelivery.Helper.ManagementCart;
import com.activity.fooddelivery.Interface.ChangeNumberItemListener;
import com.activity.fooddelivery.R;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ManagementCart managementCart;
    private double tax;
    private TextView subTotalTxt,deliveryFeeTxt, taxTxt,totalAmountTxt,emptyTxt;
    private ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        managementCart = new ManagementCart(this);

        initView();
        calculateCard();
    }

    public void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberItemListener() {
            @Override
            public void changed() {
                calculateCard();
            }
        });

        recyclerView.setAdapter(adapter);

        if(managementCart.getListCart().isEmpty()){
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else{
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }


    private void calculateCard() {
        double TAX = 0.02;
        double delivery = 15;

        tax = Math.round((managementCart.getTotalFee()*TAX)*100.0)/100.0;

        double total = Math.round((managementCart.getTotalFee()+tax+delivery)*100.0)/100.0;

        double itemTotal =  Math.round((managementCart.getTotalFee()*100.0))/100;

        subTotalTxt.setText("$"+itemTotal);
        deliveryFeeTxt.setText("$"+delivery);
        taxTxt.setText("$"+tax);
        totalAmountTxt.setText("$"+total);

        initList();
    }

    private void initView() {
        subTotalTxt = findViewById(R.id.subtotalNumber);
        deliveryFeeTxt = findViewById(R.id.deliveryFeeNumber);
        taxTxt = findViewById(R.id.taxNumber);
        totalAmountTxt = findViewById(R.id.totalAmoutLabelNumber);
        recyclerView = findViewById(R.id.cartView);
        emptyTxt = findViewById(R.id.emptyText);
        scrollView = findViewById(R.id.scrollView);
    }
}