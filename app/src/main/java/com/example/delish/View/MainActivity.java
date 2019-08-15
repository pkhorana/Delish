package com.example.delish.View;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.util.Base64;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.delish.R;
import com.example.delish.ViewModel.ShowDelishCamera;
import com.example.delish.Models.RecipeAlgo;
import com.example.delish.Models.CloudVision;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static com.example.delish.Models.RecipeAlgo.cuisineList;
import static com.example.delish.Models.RecipeAlgo.recipeMatches;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton takePictureButton;
    Camera camera;
    FrameLayout frameLayout;
    ShowDelishCamera showDelishCamera;
    private float x1,x2; //USED IN SWIPE DETECTION
    static final int MIN_DISTANCE = 150; //USED IN SWIPE DETECTION
    public static List<DataSnapshot> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
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
            RecipeAlgo.queryforRecipes(new RecipeAlgo.MyCallback() {
                @Override
                public void onCallback(List<DataSnapshot> list) {
                    recipes = list;
                }
            });
            //recipes = RecipeAlgo.recipeMatches;
//            final Thread thread = new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        sleep(1000);
//                        recipes =  RecipeAlgo.recipeMatches;
//                        System.out.println("wait");
//                        return;
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//
////                        finish();
//                    }
//                }
//            };
//
//            thread.start();
            try {
                Thread.sleep(200);

            } catch (Exception e) {
                e.printStackTrace();
            }
            

            BottomSheetDialogDetails bottomSheetDialogDetails = new BottomSheetDialogDetails();
            bottomSheetDialogDetails.show(getSupportFragmentManager(), bottomSheetDialogDetails.getTag());












//            MaterialCardView recipe2 = findViewById(R.id.recipe_2);
//            MaterialCardView recipe3 = findViewById(R.id.recipe_3);
        }
    };

    public void fixThisIssue(Map<DataSnapshot, String> recipeMatches) {

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
                //RecipeAlgo.queryforRecipes();
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

            int[] arr = new int[3];
            boolean same = false;
            int i = 0;
            int rand= 0;
            while (i < 3) {
                rand = (int)(Math.random()*recipes.size());
                arr[i] = rand;
                int check = i - 1;
                while (check > -1 && same == false) {
                    if (arr[i] == arr[check]) {
                        i--;
                        same = true;
                    }
                    check--;
                }
                i++;

            }

            dialog.setContentView(contentView);
            MaterialCardView recipe1 = contentView.findViewById(R.id.recipe_1);
            TextView recipe_name1 = recipe1.findViewById(R.id.recipe_name1);
            TextView cus1 = recipe1.findViewById(R.id.cusisineType1);
            TextView calories1 = recipe1.findViewById(R.id.calories1);
            TextView prepTime1 = (TextView)recipe1.findViewById(R.id.prepTime1);
            TextView cookTimeView1 = (TextView)recipe1.findViewById(R.id.cookTime1);
            ImageView imageView1 = (ImageView) recipe1.findViewById(R.id.imageView1);

            int first = arr[0];
            recipe_name1.setText((String)recipes.get(first).child("name").getValue());
            cus1.setText(cuisineList.get(first));
            calories1.setText((String)recipes.get(first).child("nurtitional_content").
                    child("calories").child("amount").getValue() + " cal");
            prepTime1.setText((String)recipes.get(first).child("prep_time").getValue());
            cookTimeView1.setText((String)recipes.get(first).child("cook_time").getValue());
            Picasso.get().load((String)recipes.get(first).child("image_URL").getValue()).into(imageView1);

            System.out.println("efd");


            MaterialCardView recipe2 = contentView.findViewById(R.id.recipe_2);
            TextView recipe_name2 = recipe2.findViewById(R.id.recipe_name2);
            TextView cus2 = recipe2.findViewById(R.id.cusisineType2);
            TextView calories2 = recipe2.findViewById(R.id.calories2);
            TextView prepTime2 = (TextView)recipe2.findViewById(R.id.prepTime2);
            TextView cookTimeView2 = (TextView)recipe2.findViewById(R.id.cookTime2);
            ImageView imageView2 = (ImageView) recipe2.findViewById(R.id.imageView2);

            int sec = arr[1];
            recipe_name2.setText((String)recipes.get(sec).child("name").getValue());
            cus2.setText(cuisineList.get(sec));
            calories2.setText((String)recipes.get(sec).child("nurtitional_content").
                    child("calories").child("amount").getValue() + " cal");
            prepTime2.setText((String)recipes.get(sec).child("prep_time").getValue());
            cookTimeView2.setText((String)recipes.get(sec).child("cook_time").getValue());
            Picasso.get().load((String)recipes.get(sec).child("image_URL").getValue()).into(imageView2);
            System.out.println("efd");




            MaterialCardView recipe3 = contentView.findViewById(R.id.recipe_3);
            TextView recipe_name3 = recipe3.findViewById(R.id.recipe_name3);
            TextView cus3 = recipe3.findViewById(R.id.cusisineType3);
            TextView calories3 = recipe3.findViewById(R.id.calories3);
            TextView prepTime3 = (TextView)recipe3.findViewById(R.id.prepTime3);
            TextView cookTimeView3 = (TextView)recipe3.findViewById(R.id.cookTime3);
            ImageView imageView3 = (ImageView) recipe3.findViewById(R.id.imageView3);

            int third = arr[2];
            recipe_name3.setText((String)recipes.get(third).child("name").getValue());
            cus3.setText(cuisineList.get(third));
            calories3.setText((String)recipes.get(third).child("nurtitional_content").
                    child("calories").child("amount").getValue() + " cal");
            prepTime3.setText((String)recipes.get(third).child("prep_time").getValue());
            cookTimeView3.setText((String)recipes.get(third).child("cook_time").getValue());
            Picasso.get().load((String)recipes.get(third).child("image_URL").getValue()).into(imageView3);

        }
    }
}