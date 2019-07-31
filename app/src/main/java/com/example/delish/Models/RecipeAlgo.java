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

public class RecipeAlgo {

    public static String cameraItem;

    public static Map<DataSnapshot,String> recipeMatches = new HashMap<DataSnapshot, String>();








//        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot cuisines : dataSnapshot.getChildren()){
//                    String cuisine = cuisines.getKey();
//                    for(DataSnapshot recipes : cuisines.getChildren()){
//                        for(DataSnapshot ingredients : recipes.child("ingredients").getChildren()){
//                            String ingredient = ((String)ingredients.child("name").getValue()).toLowerCase();
//                            if(ingredient.contains(cameraItem)){
//                                recipeMatches.put(recipes,cuisine);
//                            }
//
//                        }
//                    }
//                }
////                MainActivity yolo = new MainActivity();
////                yolo.fixThisIssue(recipeMatches);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });


    public static void setItem(String item){
        cameraItem = item;
    }

    public static String getItem(){
        return cameraItem;
    }

}
