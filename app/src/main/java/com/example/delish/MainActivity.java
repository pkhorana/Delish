package com.example.delish;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.util.Base64;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.example.delish.Models.RecipeAlgo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.example.delish.Models.CloudVision;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


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
        getSupportActionBar().setTitle("Delish by NCR");
        //getSupportActionBar().set(Color.parseColor("#80000000"));
        takePictureButton = findViewById(R.id.take_picture_button);
        frameLayout = findViewById(R.id.camera_layout);

        camera = Camera.open();

        showDelishCamera = new ShowDelishCamera(this, camera);
        frameLayout.addView(showDelishCamera);

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // UNCOMMENT THE NEXT LINE TO MAKE THE API CALL
                // captureImage(v);
                BottomSheetDialogDetails bottomSheetDialogDetails = new BottomSheetDialogDetails();
                bottomSheetDialogDetails.show(getSupportFragmentManager(), bottomSheetDialogDetails.getTag());
            }
        });
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String base64 = processImage(data);
            new MyTask().execute(base64);
            RecipeAlgo.queryforRecipes();

//            try {
//                CloudVision.webDetectionResponse(base64);
//                System.out.println("Works!!");
//            } catch (Exception e) {
//                System.out.println("Failllllll");
//            }
        }
    };

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
                System.out.println("Works!!");
                return resp;

            } catch (Exception e) {
                System.out.println("Failllllll");
                return null;
            }

        }
    }
}