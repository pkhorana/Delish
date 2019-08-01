package com.example.delish.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.delish.Models.NutritionInsights;
import com.example.delish.R;

public class NutritionActivity extends AppCompatActivity {

    private String itemName;
    private TextView name;
    private TextView ss;
    private TextView cal;
    private TextView nutr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrient_insights_page);


        Bundle bundle = getIntent().getExtras();

        itemName = bundle.getString("FoodName");

        name = findViewById(R.id.item_name);
        ss = findViewById(R.id.ser_num);
        cal = findViewById(R.id.cal_num);
        nutr = findViewById(R.id.nutrition);

        name.setText(itemName);

        Button exit = findViewById(R.id.exit);
        exit.setOnClickListener((view) -> {
            startActivity(new Intent(NutritionActivity.this, Cart.class));
        });

        NutritionInsights n = new NutritionInsights();

//        n.makeRequest(itemName);
//
//        ss.setText(n.getServingSize().getValue());
//        cal.setText(n.getCalories().getValue());
//
//        String com = "";
//        for(String e : n.getNutrition().keySet()) {
//            com += e + n.getNutrition().get(e).getValue() + "\n";
//        }
//
//        nutr.setText(com);
    }
}
