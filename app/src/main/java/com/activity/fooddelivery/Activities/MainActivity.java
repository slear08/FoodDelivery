package com.activity.fooddelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.fooddelivery.Adapter.FoodAdapter;
import com.activity.fooddelivery.Domain.FoodDomain;
import com.activity.fooddelivery.R;
import com.activity.fooddelivery.User.Session.SessionManager;
import com.activity.fooddelivery.User.User;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView productViewRecycler;

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private boolean isBackPressedOnce = false;

    TextView userName,info;

    ImageView profile;

    SessionManager sessionManager;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = (User) getIntent().getSerializableExtra("user");


        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        userName = findViewById(R.id.dashboardName);
        info = findViewById(R.id.info);
        profile = findViewById(R.id.userImage);

        Button signOutButton = findViewById(R.id.logOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                // Create a new instance of the GoogleSignInClient to allow choosing another account
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient signInClient = GoogleSignIn.getClient(MainActivity.this, gso);

                signInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Google Sign-Out was successful
                        Toast.makeText(getApplicationContext(), "Signed out successfully", Toast.LENGTH_SHORT).show();

                        // Start the login flow again
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });


            }
        });



        if (user != null) {
            userName.setText(user.getDisplayName());
            info.setText(user.getEmail());
            Glide.with(this)
                    .load(user.getImage())
                    .into(profile);
        } else {
            // Handle the case when the user object is null
            Toast.makeText(this,"Something error",Toast.LENGTH_LONG).show();
        }



        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.burger, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.fries, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.coke, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.chicken, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        productViewRecycler();



    }



    @Override
    public void onBackPressed() {
        // Clear the activity stack and exit the app
        if (isBackPressedOnce) {
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return;
        }

        Toast.makeText(this, "Press again to exit!!", Toast.LENGTH_SHORT).show();
        isBackPressedOnce = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressedOnce = false;
            }
        }, 2000);


    }


    public void navigation(View view){
        startActivity(new Intent(MainActivity.this, CartActivity.class));

    }
    private void productViewRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        productViewRecycler = findViewById(R.id.productView);
        productViewRecycler.setLayoutManager(linearLayoutManager);

        ArrayList<FoodDomain> foodList = new ArrayList<>();
        foodList.add(new FoodDomain("HAMBURGER","A mouthwatering, grilled patty topped with melted cheese, fresh veggies, and served on a toasted bun for the ultimate burger bliss.","burger",20.25));
        foodList.add(new FoodDomain("CHICKEN","Juicy and tender, this perfectly seasoned chicken is cooked to perfection, offering a burst of flavor in every bite.","chicken",45.50));
        foodList.add(new FoodDomain("FRIES"," Crispy and golden on the outside, fluffy on the inside, these addictive fries are the perfect combination of saltiness and crunch.","fries",30.15));
        foodList.add(new FoodDomain("SPAGHETTI","Pasta coated in a rich, aromatic sauce, creating a delightful symphony of flavors that will transport you to Italy with each twirl of your fork.","spaghetti",45.35));
        foodList.add(new FoodDomain("COKE","An effervescent, fizzy delight that delivers a refreshing burst of sweetness, tantalizing your taste buds with every sip.","coke",20.15));
        foodList.add(new FoodDomain("ICE CREAM","Smooth and creamy, this delectable frozen treat offers a heavenly experience with its wide range of flavors, from decadent chocolates to fruity bursts of delight.","ice_cream",20.15));

        adapter = new FoodAdapter(foodList);

        productViewRecycler.setAdapter(adapter);

    }
}