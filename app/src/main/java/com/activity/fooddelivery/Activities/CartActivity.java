package com.activity.fooddelivery.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.activity.fooddelivery.Adapter.CartListAdapter;
import com.activity.fooddelivery.Domain.FoodDomain;
import com.activity.fooddelivery.Helper.ManagementCart;
import com.activity.fooddelivery.Interface.ChangeNumberItemListener;
import com.activity.fooddelivery.Models.Cart;
import com.activity.fooddelivery.Models.Product;
import com.activity.fooddelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ManagementCart managementCart;
    private double tax;
    private TextView subTotalTxt, deliveryFeeTxt, taxTxt, totalAmountTxt, emptyTxt,back;
    private ScrollView scrollView;
    private double total;

    // Firebase
    private DatabaseReference databaseReference;

    Button checkOut;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Carts").child(firebaseAuth.getCurrentUser().getUid());

        managementCart = new ManagementCart(this);

        initView();
        calculateCart();

        checkOut = findViewById(R.id.checkOutBtn);

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    String email = user.getEmail();
                    String username = user.getDisplayName();

                    saveCartToFirebase(username,email);
                    // Get the cart items
                    ArrayList<FoodDomain> productList = managementCart.getListCart();
//
                    // Start the InvoiceActivity and pass the cart items as extras
                    Intent intent = new Intent(CartActivity.this, InvoiceActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    intent.putExtra("productList", productList);
                    managementCart.clearCart();
                    startActivity(intent);
                    finish();
                } else {
                    // User is not logged in or authentication failed
                    // Handle this case accordingly
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberItemListener() {
            @Override
            public void changed() {
                calculateCart();
            }
        });

        recyclerView.setAdapter(adapter);

        if (managementCart.getListCart().isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            emptyTxt.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void calculateCart() {
        double TAX = 0.02;
        double delivery = 15;

        tax = Math.round((managementCart.getTotalFee() * TAX) * 100.0) / 100.0;

        double total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100.0) / 100.0;

        double itemTotal = Math.round((managementCart.getTotalFee() * 100.0)) / 100;

        subTotalTxt.setText("$" + itemTotal);
        deliveryFeeTxt.setText("$" + delivery);
        taxTxt.setText("$" + tax);
        totalAmountTxt.setText("$" + total);

        initList();
    }


    private void initView() {
        subTotalTxt = findViewById(R.id.subtotalNumber);
        deliveryFeeTxt = findViewById(R.id.deliveryFeeNumber);
        taxTxt = findViewById(R.id.taxNumber);
        totalAmountTxt = findViewById(R.id.totalAmoutLabelNumber);
        recyclerView = findViewById(R.id.cartView);
        emptyTxt = findViewById(R.id.emptyText);
        back = findViewById(R.id.emptyBackBtn);
        scrollView = findViewById(R.id.scrollView);
    }

    // Method to save cart data to Firebase
    private void saveCartToFirebase(String username, String email) {
        ArrayList<FoodDomain> productList = managementCart.getListCart();
        Log.d("CartActivity", "Product List: " + productList); // Debugging statement

        // Ensure that productList is not null or empty
        if (productList != null && !productList.isEmpty()) {
            // Create a new Cart object
            Cart cart = new Cart(username, email, productList);

            // Push the cart object to Firebase database
            String cartId = databaseReference.push().getKey();
            databaseReference.child(cartId).setValue(cart);
        } else {
            Log.d("CartActivity", "Product List is null or empty"); // Debugging statement
        }
    }

}
