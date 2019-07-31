package com.example.delish.Models;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class NutritionInsights {
    public static final String NUTRITIONURL = "http://DelishAppNCR.pythonanywhere.com/nutrition_facts";
    private NutritionValue servingSize;
    private NutritionValue calories;
    private Map<String, NutritionValue> nutrition;
    private JSONObject responseJSON;

    public NutritionValue getServingSize() {
        return this.servingSize;
    }

    public NutritionValue getCalories() {
        return this.calories;
    }

    public Map<String, NutritionValue> getNutrition() {
        return this.nutrition;
    }

    public void makeRequest(String itemName) {
        try {
            URL url = new URL(NUTRITIONURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = String.format("{\"item_name\": \"%s\"}", itemName);

            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                responseJSON = new JSONObject(responseJSON.toString());
                parseResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseResponse() throws Exception {
        this.calories = new NutritionValue((Double)responseJSON.get("calories"), "cal");
        this.servingSize = new NutritionValue(getValue((String)responseJSON.get("serving_size")), "g");
        this.nutrition.put("0", new NutritionValue(0.0, "g"));
        Log.d("test", (String) responseJSON.get("nutrition"));
    }

    public Double getValue(String str) {

        int c = str.length() - 1;

        while(Character.isAlphabetic(str.charAt(c))) {
            c--;
        }

        return Double.parseDouble(str.substring(0, c+1));
    }
    // function to get the json response
    // process the json response
    // function to populate nutrition
    // function to populate serving size
    // function to populate calories

    // function to get a list of per cent insights
    // function to get a list of activity needed insights
}

//enum Activity {
//    WALKING,
//    RUNNING,
//    BICYCLING
//}
//
//enum Nutrition {
//    FAT,
//    CHOLESTEROL,
//    SODIUM,
//    POTASSIUM,
//    CARBOHYDRATES,
//    PROTEIN
//}
//
//
//enum RecommendedValues {
//    SODIUM(new NutritionValue(1.5f*1000f, "mg")),
//    POTASSIUM(new NutritionValue(4.7f*1000f, "mg")),
//    CARBOHYDRATES(new NutritionValue(130,"g")),
//    PROTEIN(new NutritionValue(56, "g"));
//
//    private final NutritionValue nutritionValue;
//
//    private RecommendedValues(NutritionValue nutritionValue) {
//        this.nutritionValue = nutritionValue;
//    }
//
//    public NutritionValue getNutritionValue() {
//        return this.nutritionValue;
//    }
//
//}