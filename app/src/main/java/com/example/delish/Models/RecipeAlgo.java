package com.example.delish.Models;

import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.delish.R;
import com.example.delish.View.MainActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class RecipeAlgo {

    public static String cameraItem;
    public static boolean bool =false;

    public static List<DataSnapshot> recipeMatches = new ArrayList<DataSnapshot>();
    public static List<String> cuisineList = new ArrayList<String>();
    public static CountDownLatch done = new CountDownLatch(1);
    public static List<String> itemList = new ArrayList<String>();
    public static final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");

    public static void queryforRecipes() {


        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot cuisines : dataSnapshot.getChildren()){
                    String cuisine = cuisines.getKey();
                    for(DataSnapshot recipe : cuisines.getChildren()){
                        for(DataSnapshot ingredient : recipe.child("ingredients").getChildren()){
                            String ingred = (String)ingredient.child("name").getValue();
                            String lower = "something";
                            if (ingred != null) {
                                lower = ingred.toLowerCase();
                            }
//                            itemList.add(ingred);
                            if(lower.contains("chicken")){
                                recipeMatches.add(recipe);
                                if (cuisine.equals("American Cuisines")) {
                                    cuisineList.add("American");
                                } else {
                                    cuisineList.add(cuisine);
                                }
                            }

                        }
                    }
                }
                String val = "done";
                bool = true;




            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    public static void setItem(String item){
        cameraItem = item;
    }

    public static String getItem(){
        return cameraItem;
    }

}
