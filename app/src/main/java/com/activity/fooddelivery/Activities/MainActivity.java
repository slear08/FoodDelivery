package com.activity.fooddelivery.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.activity.fooddelivery.Adapter.FoodAdapter;
import com.activity.fooddelivery.Domain.FoodDomain;
import com.activity.fooddelivery.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView productViewRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageSlider imageSlider = findViewById(R.id.imageSlider);
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
        foodList.add(new FoodDomain("Hamburger","Burger kayo sakin","burger",45.35));
        foodList.add(new FoodDomain("Chicken","Chicken kayo sakin","chicken",45.35));
        foodList.add(new FoodDomain("Fries","Fries kayo sakin","fries",45.35));
        foodList.add(new FoodDomain("Spaghetti","Spaghetti kayo sakin","spaghetti",45.35));
        foodList.add(new FoodDomain("Coke","Coke kayo sakin","coke",45.35));
        foodList.add(new FoodDomain("Ice Cream","Ice Cream kayo sakin","ice_cream",45.35));

        adapter = new FoodAdapter(foodList);

        productViewRecycler.setAdapter(adapter);

    }
}