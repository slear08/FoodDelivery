package com.activity.fooddelivery.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.activity.fooddelivery.R;

import com.activity.fooddelivery.User.Session.SessionManager;
import com.activity.fooddelivery.User.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseRef;

    private SignInButton signInButton;

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(getApplicationContext());

        // Configure Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Create a GoogleSignInClient with the options
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Set up the Google Sign-In button
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(v -> signIn());

    }


    @Override
    protected void onStart() {
        super.onStart();

        // Check if the user is already signed in
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Toast.makeText(this,"LOADING",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of the Google Sign-In activity
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign-In failed, handle the error
                Log.w(TAG, "Google sign-in failed", e);
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-In with Google and Firebase was successful
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // Sign-In with Google and Firebase failed, handle the error
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            // User is signed in, update your UI accordingly
            // You can access user information via user.getDisplayName(), user.getEmail(), etc.
            // For example, you can display a welcome message or navigate to another activity
            User user = new User();
            user.setUid(firebaseUser.getUid());
            user.setDisplayName(firebaseUser.getDisplayName());
            user.setEmail(firebaseUser.getEmail());


//            sessionManager.createLoginSession(user.getEmail(),user.getDisplayName());
            // Set additional properties as needed
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);

            // Add data to database from user
            databaseRef = FirebaseDatabase.getInstance().getReference("Users");

            databaseRef.push().setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Data inserted successfully
                            Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to insert the data
                            Toast.makeText(getApplicationContext(), "Failed to insert data", Toast.LENGTH_SHORT).show();
                        }
                    });



        } else {
            // User is signed out, update your UI accordingly
            // For example, you can show the Google Sign-In button
        }
    }
}