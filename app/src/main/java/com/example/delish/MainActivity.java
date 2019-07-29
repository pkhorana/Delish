package com.example.delish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;


import android.util.Base64;
import java.io.ByteArrayOutputStream;
import android.os.AsyncTask;


import androidx.appcompat.app.AppCompatActivity;

import com.example.delish.Models.CloudVision;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button takeAPicture = findViewById(R.id.take_picture_button);
        takeAPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takeAPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takeAPicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeAPicture, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String base64 = processImage(imageBitmap);
            new MyTask().execute(base64);
//            try {
//                CloudVision.webDetectionResponse(base64);
//                System.out.println("Works!!");
//            } catch (Exception e) {
//                System.out.println("Failllllll");
//            }

        }
    }

    public String processImage(Bitmap bitmap) {
        // code that in
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
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
