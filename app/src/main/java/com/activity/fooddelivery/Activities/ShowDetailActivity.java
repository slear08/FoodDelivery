package com.activity.fooddelivery.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.fooddelivery.Domain.FoodDomain;
import com.activity.fooddelivery.Helper.ManagementCart;
import com.activity.fooddelivery.R;
import com.bumptech.glide.Glide;

public class ShowDetailActivity extends AppCompatActivity {

    private TextView plusBtn,minusBtn,quantity,title,price,totalprice,description;

    private ImageView productPic;
    private Button addToCart;
    private FoodDomain object;
    private int numberOrder = 1;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        managementCart = new ManagementCart(this);
        
        initView();

        getBundle();
    }

    private void getBundle() {
        object = (FoodDomain)getIntent().getSerializableExtra("object");

        int drawableResourceId = this.getResources().getIdentifier(object.getImage(),"drawable",this.getPackageName());

        Glide.with(this).load(drawableResourceId).into(productPic);


        title.setText(object.getTitle());
        price.setText("$"+object.getPrice());
        description.setText(object.getDescription());
        quantity.setText(String.valueOf(numberOrder));
        totalprice.setText("$"+object.getPrice());

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOrder+=1;
                quantity.setText(String.valueOf(numberOrder));
                totalprice.setText(String.valueOf("$"+numberOrder*object.getPrice()));
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOrder>1){
                    numberOrder-=1;
                }
                quantity.setText(String.valueOf(numberOrder));
                totalprice.setText(String.valueOf("$"+numberOrder*object.getPrice()));
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setCartNumber(numberOrder);
                managementCart.insertFood(object);
            }
        });
    }

    private void initView() {


        title = findViewById(R.id.detailProdiuctName);
        price = findViewById(R.id.detailProdiuctPrice);
        description = findViewById(R.id.detailProdiuctDescription);
        quantity = findViewById(R.id.detailProdiuctQuantity);
        totalprice = findViewById(R.id.detailProdiuctTotal);
        productPic = findViewById(R.id.detailProdiuctImage);

        //BUTTONS
        addToCart = findViewById(R.id.detailProdiuctBtn);
        plusBtn = findViewById(R.id.detailProdiuctAddBtn);
        minusBtn = findViewById(R.id.detailProdiuctMinusBtn);



    }
}