package com.activity.fooddelivery.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.fooddelivery.Adapter.FoodAdapter;
import com.activity.fooddelivery.Domain.FoodDomain;
import com.activity.fooddelivery.R;
import com.activity.fooddelivery.User.Session.SessionManager;
import com.activity.fooddelivery.User.User;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView productViewRecycler;

    TextView userName,info;

    SessionManager sessionManager;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = (User) getIntent().getSerializableExtra("user");

//        sessionManager = new SessionManager(getApplicationContext());
//        sessionManager.checkLogin();

        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        userName = findViewById(R.id.dashboardName);
        info = findViewById(R.id.info);



        if (user != null) {
            userName.setText(user.getDisplayName());
            info.setText(user.getEmail());
        } else {
            // Handle the case when the user object is null
            Toast.makeText(this,"object is null",Toast.LENGTH_LONG).show();
        }



        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.burger, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.fries, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.coke, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.chicken, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        productViewRecycler();



    }


    public void navigation(View view){
        startActivity(new Intent(MainActivity.this, CartActivity.class));

    }
    private void productViewRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        productViewRecycler = findViewById(R.id.productView);
        productViewRecycler.setLayoutManager(linearLayoutManager);

        ArrayList<FoodDomain> foodList = new ArrayList<>();
        foodList.add(new FoodDomain("HAMBURGER","Burger kayo sakin","burger",45.35));
        foodList.add(new FoodDomain("CHICKEN","Chicken kayo sakin","chicken",45.35));
        foodList.add(new FoodDomain("FRIES","Fries kayo sakin","fries",45.35));
        foodList.add(new FoodDomain("SPAGHETTI","Spaghetti kayo sakin","spaghetti",45.35));
        foodList.add(new FoodDomain("COKE","Coke kayo sakin","coke",45.35));
        foodList.add(new FoodDomain("ICE CREAM","Ice Cream kayo sakin","ice_cream",45.35));

        adapter = new FoodAdapter(foodList);

        productViewRecycler.setAdapter(adapter);

    }
}