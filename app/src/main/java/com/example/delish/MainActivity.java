package com.example.delish;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.util.Base64;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.example.delish.Models.CloudVision;


public class MainActivity extends AppCompatActivity {
    private Button takePictureButton;
    Camera camera;
    FrameLayout frameLayout;
    ShowDelishCamera showDelishCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePictureButton = findViewById(R.id.take_picture_button);
        frameLayout = findViewById(R.id.camera_layout);

        camera = Camera.open();

        showDelishCamera = new ShowDelishCamera(this, camera);
        frameLayout.addView(showDelishCamera);

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage(v);
                new MaterialAlertDialogBuilder(v.getContext())
                        .setTitle("Title")
                        .setMessage("Message")
                        .setPositiveButton("Ok", null)
                        .show();
            }
        });
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String base64 = processImage(data);
            new MyTask().execute(base64);
//            try {
//                CloudVision.webDetectionResponse(base64);
//                System.out.println("Works!!");
//            } catch (Exception e) {
//                System.out.println("Failllllll");
//            }
        }
    };

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