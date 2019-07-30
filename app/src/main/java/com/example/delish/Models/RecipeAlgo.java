package com.example.delish.Models;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeAlgo {

    public static String cameraItem;

    public Map<String,DataSnapshot> recipeMatches;


    public void queryforRecipes() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipe/");
        List<String> ingredients = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot cuisines : dataSnapshot.getChildren()){
                    String cuisine = cuisines.getKey();
                    for(DataSnapshot recipes : cuisines.getChildren()){
                        for(DataSnapshot ingredients : recipes.child("ingredients").getChildren()){
                            for(DataSnapshot finaling : ingredients.getChildren()){
                                String ingredient = finaling.child("name").getValue().toString();
                                if(ingredient.contains(cameraItem)){
                                    recipeMatches.put(cuisine,recipes);
                                }
                            }
                        }
                    }
                }
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