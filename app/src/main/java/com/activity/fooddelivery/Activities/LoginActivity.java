package com.activity.fooddelivery.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

            // Get the user's profile photo URL
            Uri photoUrl = firebaseUser.getPhotoUrl();
            if (photoUrl != null) {
                user.setImage(photoUrl.toString());
            }

            // Check if the user already exists in the database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
            Query query = userRef.orderByChild("uid").equalTo(user.getUid());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User already exists in the database, handle accordingly
                        Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_LONG).show();
                        // Add your logic here for an existing user
                    } else {
                        // User does not exist in the database, add the user
                        DatabaseReference newUserRef = userRef.push();
                        newUserRef.setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Data inserted successfully
                                        Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_LONG).show();
                                        // Proceed with your logic for a new user
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Failed to insert the data
                                        Toast.makeText(getApplicationContext(), "Failed to insert data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // Set additional properties as needed
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish(); // Finish the LoginActivity so that the user cannot go back to it after logging in
        } else {
            // User is signed out, update your UI accordingly
            // For example, you can show the Google Sign-In button

        }
    }

    private void signOut() {
        // Sign out from Firebase
        firebaseAuth.signOut();

        // Sign out from Google SignIn
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Google Sign-Out was successful
            Toast.makeText(getApplicationContext(), "Signed out successfully", Toast.LENGTH_SHORT).show();
        });

        // Clear the saved user data or session
        sessionManager.logoutUser();

        // Update your UI accordingly, e.g., show the Google Sign-In button again
    }

    // Call this method when you want to allow the user to choose another account to login
    public void chooseAnotherAccount() {
        // Sign out from Firebase
        firebaseAuth.signOut();

        // Sign out from Google SignIn without clearing the saved account
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Google Sign-Out was successful
            Toast.makeText(getApplicationContext(), "Signed out successfully", Toast.LENGTH_SHORT).show();

            // Start the login flow again
            signIn();
        });

        // Clear the saved user data or session if needed
        sessionManager.logoutUser();
    }

}
