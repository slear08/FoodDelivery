# Food Delivery App Documentation

## Overview
The **Food Delivery App** is a mobile application that allows users to browse and order various food items for delivery. The app provides a user-friendly interface for selecting food items, adding them to the cart, and placing an order. It also integrates with **Google Sign-In** for user authentication and **Firebase** for storing user information and order details.

## Features
**User Registration and Login:**

Users can create an account or log in using their Google account.
**Firebase Authentication** is used for secure user authentication.

**Home Page:**

Displays a slideshow of featured food items using the ImageSlider library.
Shows the user's name and email in the dashboard section.
Provides a sign-out button to log out of the app.

**Food Listing:**

Presents a list of available food items with details such as name, description, image, and price.
Uses a RecyclerView with a vertical LinearLayoutManager to display the food items.
Each food item is represented by a card view in the list.

**Cart Functionality:**

Allows users to add food items to the cart.
Updates the cart dynamically with the added items.
Provides a button to navigate to the cart activity to view and place orders.

**Order Placement:**

Users can view the selected food items and their total price in the cart activity.
After reviewing the order, users can proceed to place the order.
Order details are stored in Firebase Realtime Database for order management and tracking.

**Google Sign-In Integration:**

Utilizes the Google Sign-In API for seamless user authentication.
Allows users to log in with their Google accounts, providing a convenient and secure login experience.

## Technology Stack
**Java:** Programming language used for Android app development.
**Android SDK:** Provides the necessary tools and libraries for Android app development.
**Firebase:** A comprehensive backend platform for building mobile and web applications. Used for authentication and realtime database.
**Google Sign-In API:** Enables Google account authentication in the app.
**ImageSlider library:** Used to create a slideshow of food item images on the home page.
**RecyclerView:** Android UI component for displaying lists of items efficiently.
**Glide:** Image loading and caching library for efficient image loading.

## Installation and Setup
1. Clone or download the project from the repository.
2. Open the project in Android Studio.
3. Configure Firebase project and add the google-services.json file to the app module.
4. Build and run the app on an Android emulator or physical device.

## Dependencies
- implementation 'com.google.firebase:firebase-auth:20.0.0'
- implementation 'com.google.firebase:firebase-database:20.0.0'
- implementation 'com.google.android.gms:play-services-auth:19.0.0'
- implementation 'com.github.denzcoskun:ImageSlideshow:0.1.0'
- implementation 'com.github.bumptech.glide:glide:4.12.0'

## Conclusion
The **Food Delivery App** provides a convenient and user-friendly platform for browsing and ordering food items. With its integration of Google Sign-In and Firebase, it offers secure user authentication and efficient storage of user information and order details. The app can be easily customized and extended to meet specific business requirements and can serve as a foundation for building a full-fledged food delivery service.
