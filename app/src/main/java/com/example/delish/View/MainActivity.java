package com.example.delish.View;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.util.Base64;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.delish.R;
import com.example.delish.ViewModel.ShowDelishCamera;
import com.example.delish.Models.RecipeAlgo;
import com.example.delish.Models.CloudVision;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.example.delish.Models.RecipeAlgo.recipeMatches;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton takePictureButton;
    Camera camera;
    FrameLayout frameLayout;
    ShowDelishCamera showDelishCamera;
    private float x1,x2; //USED IN SWIPE DETECTION
    static final int MIN_DISTANCE = 150; //USED IN SWIPE DETECTION

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setTitle("Delish by NCR");
        //getSupportActionBar().set(Color.parseColor("#80000000"));
        takePictureButton = findViewById(R.id.take_picture_button);
        frameLayout = findViewById(R.id.camera_layout);

        camera = Camera.open();

        showDelishCamera = new ShowDelishCamera(this, camera);
        frameLayout.addView(showDelishCamera);

        /*frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // UNCOMMENT THE NEXT LINE TO MAKE THE API CALL
                captureImage(v);

//                Intent intent = new Intent(v.getContext(), CheckoutActivity.class);
//                startActivity(intent);
                BottomSheetDialogDetails bottomSheetDialogDetails = new BottomSheetDialogDetails();
                bottomSheetDialogDetails.show(getSupportFragmentManager(), bottomSheetDialogDetails.getTag());
            }
        });
    }



    public static String makeRequest(String itemName) {
        try {
            String NUTRITIONURL = "https://DelishAppNCR.pythonanywhere.com/recipe_query";
            URL url = new URL(NUTRITIONURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = String.format("{\"ingredients\": \"%s\", \"num_results\": 3}", itemName);

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
                System.out.println(response.toString());
                return response.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void queryandview(String resp) {
        try{
            JSONArray recipes = new JSONArray(resp);



            JSONObject recipe = recipes.getJSONObject(0);
            String name = recipe.getString("name");
            String imageUrl = recipe.getString("image_URL");
            String cookTime = recipe.getString("cook_time");
            String prep_time = recipe.getString("prep_time");
            String cuisine = recipe.getString("tags").trim();
//            JSONObject nutritionalContent = recipe.getJSONObject("nutritional_content");
//            JSONObject caloriesObj = nutritionalContent.getJSONObject("calories");
//            String cal = caloriesObj.getString("amount");

            setContentView(R.layout.bottom_sheet_dialog);
            MaterialCardView recipe1 = findViewById(R.id.recipe_1);
//            TextView recipe_name = recipe1.findViewById(R.id.recipe_name1);
//            ImageView imageView = (ImageView) recipe1.findViewById(R.id.imageView);
            TextView cus = recipe1.findViewById(R.id.cusisineType1);
            TextView calories = recipe1.findViewById(R.id.calories1);
            TextView prepTime = (TextView)recipe1.findViewById(R.id.prepTime1);
            TextView cookTimeView = (TextView)recipe1.findViewById(R.id.cookTime1);


//            recipe_name.setText(name);
//            Picasso.get().load(imageUrl).into(imageView);
            cus.setText(cuisine);
//            calories.setText(cal);
            prepTime.setText(prep_time);
            cookTimeView.setText(cookTime);






            JSONObject recipeT = recipes.getJSONObject(1);
            String name2 = recipeT.getString("name");
            String imageUrl2 = recipeT.getString("image_URL");
            String cookTime2 = recipeT.getString("cook_time");
            String prep_time2 = recipeT.getString("prep_time");
            String cuisine2 = recipeT.getString("tags").trim();
//            JSONObject nutritionalContent2 = recipeT.getJSONObject("nutritional_content");
//            JSONObject caloriesObj2 = nutritionalContent2.getJSONObject("calories");
//            String cal2 = caloriesObj2.getString("amount");


            MaterialCardView recipe2 = findViewById(R.id.recipe_2);
            TextView recipe_name2 = recipe2.findViewById(R.id.recipe_name1);
            ImageView imageView2 = (ImageView) recipe1.findViewById(R.id.imageView);
            TextView cus2 = recipe1.findViewById(R.id.cusisineType);
            TextView calories2 = recipe1.findViewById(R.id.calories);
            TextView prepTime2 = (TextView)recipe1.findViewById(R.id.prepTime1);
            TextView cookTimeView2 = (TextView)recipe1.findViewById(R.id.cookTime1);


            recipe_name2.setText(name2);
            Picasso.get().load(imageUrl2).into(imageView2);
            cus2.setText(cuisine2);
//            calories2.setText(cal2);
            prepTime2.setText(prep_time2);
            cookTimeView2.setText(cookTime2);



            JSONObject recipeZ = recipes.getJSONObject(1);
            String name3 = recipeZ.getString("name");
            String imageUrl3 = recipeZ.getString("image_URL");
            String cookTime3 = recipeZ.getString("cook_time");
            String prep_time3 = recipeZ.getString("prep_time");
            String cuisine3 = recipeZ.getString("tags").trim();
//            JSONObject nutritionalContent3 = recipeZ.getJSONObject("nutritional_content");
//            JSONObject caloriesObj3 = nutritionalContent3.getJSONObject("calories");
//            String cal3 = caloriesObj3.getString("amount");


            MaterialCardView recipe3 = findViewById(R.id.recipe_3);
            TextView recipe_name3 = recipe3.findViewById(R.id.recipe_name1);
            ImageView imageView3 = (ImageView) recipe3.findViewById(R.id.imageView);
            TextView cus3 = recipe3.findViewById(R.id.cusisineType);
            TextView calories3 = recipe3.findViewById(R.id.calories);
            TextView prepTime3 = (TextView)recipe3.findViewById(R.id.prepTime1);
            TextView cookTimeView3 = (TextView)recipe3.findViewById(R.id.cookTime1);


            recipe_name3.setText(name3);
            Picasso.get().load(imageUrl3).into(imageView3);
            cus3.setText(cuisine3);
//            calories3.setText(cal3);
            prepTime3.setText(prep_time3);
            cookTimeView3.setText(cookTime3);

//            JSONArray arr = json.getJSONArray("responses");
//            JSONObject obj = arr.getJSONObject(0);
//            JSONObject obj2 = obj.getJSONObject("webDetection");
//            JSONArray arr2 = obj2.getJSONArray("webEntities");

//            MaterialCardView recipe1 = findViewById(R.id.recipe_1);
//            TextView recipe_name = recipe1.findViewById(R.id.recipe_name1);
//            recipe_name.setText((String)(key1.child("name:").getValue()));
//            ImageView imageView = (ImageView) findViewById(R.id.imageView);
//            String imageUrl = (String)(key1.child("image_URL:").getValue());
//            Picasso.get().load(imageUrl).into(imageView);
//            TextView cus = recipe1.findViewById(R.id.cusisineType);
//            cus.setText((String)(recipeMatches.get(key1)));
//            TextView calories = recipe1.findViewById(R.id.calories);
//            calories.setText((String)(key1.child("nurtitional_content").child("calories").child("amount").getValue()));
//            TextView prepTime = (TextView)recipe1.findViewById(R.id.prepTime1);
//            prepTime.setText((String)(key1.child("prep_time:").getValue()));
//            TextView cookTime = (TextView)recipe1.findViewById(R.id.cookTime1);
//            cookTime.setText((String)(key1.child("cook_time:").getValue()));
        }
        catch(Exception e){

        }

    }

    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            takePictureButton.setEnabled(true);
        }
    };

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String base64 = processImage(data);
            new MyTask().execute(base64);





//            MaterialCardView recipe2 = findViewById(R.id.recipe_2);
//            MaterialCardView recipe3 = findViewById(R.id.recipe_3);
        }
    };

    public void fixThisIssue(Map<DataSnapshot, String> recipeMatches) {
        List<DataSnapshot> keysAsArray = new ArrayList<DataSnapshot>(recipeMatches.keySet());
        Random r = new Random();
        DataSnapshot key1 = keysAsArray.get(r.nextInt(keysAsArray.size()));
        MaterialCardView recipe1 = findViewById(R.id.recipe_1);
        TextView recipe_name = recipe1.findViewById(R.id.recipe_name1);
        recipe_name.setText((String)(key1.child("name:").getValue()));
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        String imageUrl = (String)(key1.child("image_URL:").getValue());
        Picasso.get().load(imageUrl).into(imageView);
        TextView cus = recipe1.findViewById(R.id.cusisineType);
        cus.setText((String)(recipeMatches.get(key1)));
        TextView calories = recipe1.findViewById(R.id.calories);
        calories.setText((String)(key1.child("nurtitional_content").child("calories").child("amount").getValue()));
        TextView prepTime = (TextView)recipe1.findViewById(R.id.prepTime1);
        prepTime.setText((String)(key1.child("prep_time:").getValue()));
        TextView cookTime = (TextView)recipe1.findViewById(R.id.cookTime1);
        cookTime.setText((String)(key1.child("cook_time:").getValue()));

        MaterialCardView recipe2 = findViewById(R.id.recipe_2);
        MaterialCardView recipe3 = findViewById(R.id.recipe_3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (deltaX > MIN_DISTANCE)
                {
                    Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Cart.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                else if ((-1 * deltaX) > MIN_DISTANCE)
                {
                    Toast.makeText(this, "r2l swipe", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, SavedRec.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else
                {
                    camera.autoFocus(autoFocusCallback);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void captureImage(View v){
        if (camera != null) {
            camera.takePicture(null, null, mPictureCallback);
        }
    }

    public String processImage(byte[] data) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String encoded = Base64.encodeToString(data, Base64.DEFAULT);
        return encoded;

    }

    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String resp = CloudVision.webDetectionResponse(params[0]);
                String response = makeRequest("chicken");
                queryandview(response);
                System.out.println("Works!!");

                return resp;

            } catch (Exception e) {
                System.out.println("Failllllll");
                return null;
            }

        }
    }

    public static class BottomSheetDialogDetails extends BottomSheetDialogFragment {
        @SuppressLint("RestrictedApi")
        @Override
        public void setupDialog(Dialog dialog, int style) {
            super.setupDialog(dialog, style);
            View contentView = View.inflate(getContext(), R.layout.bottom_sheet_dialog, null);

            dialog.setContentView(contentView);

        }
    }
}