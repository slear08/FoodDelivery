package com.activity.fooddelivery.Helper;

import android.content.Context;
import android.widget.Toast;

import com.activity.fooddelivery.Domain.FoodDomain;
import com.activity.fooddelivery.Interface.ChangeNumberItemListener;

import java.util.ArrayList;

public class ManagementCart {

    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(FoodDomain item){
        ArrayList<FoodDomain> foodList = getListCart();

        boolean isExist = false;
        int n = 0;
        for(int i =0; i < foodList.size(); i++){
            if(foodList.get(i).getTitle().equals(item.getTitle())){
                isExist = true;
                n = i;
                break;
            }
        }
        if(isExist){
            foodList.get(n).setCartNumber(item.getCartNumber());
        }else {
            foodList.add(item);
        }

        tinyDB.putListObject("CardList",foodList);

        Toast.makeText(context,"Added to your cart",Toast.LENGTH_LONG).show();
    }

    public ArrayList<FoodDomain> getListCart() {
        return tinyDB.getListObject("CardList");
    }
    public void decreaseNumberFood(ArrayList<FoodDomain> foodList, int position, ChangeNumberItemListener changeNumberItemListener){
        if(foodList.get(position).getCartNumber()==1){
            foodList.remove(position);
        }else{
            foodList.get(position).setCartNumber(foodList.get(position).getCartNumber()-1);
        }
        tinyDB.putListObject("CardList",foodList);
        changeNumberItemListener.changed();
    }
    public void increaseNumberFood(ArrayList<FoodDomain> foodList, int position, ChangeNumberItemListener changeNumberItemListener){
        foodList.get(position).setCartNumber(foodList.get(position).getCartNumber() + 1);
        tinyDB.putListObject("CardList",foodList);
        changeNumberItemListener.changed();
    }

    public Double getTotalFee(){
        ArrayList<FoodDomain> foodList2 = getListCart();

        double fee = 0;
        for (int i = 0; i < foodList2.size(); i++){
            fee = fee + (foodList2.get(i).getPrice() * foodList2.get(i).getCartNumber());
        }

        return fee;
    }
}
