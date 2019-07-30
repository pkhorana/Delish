package com.example.delish;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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

        }
    };

    public void captureImage(View v) {
        if (camera != null) {
            camera.takePicture(null, null, mPictureCallback);
        }
    }
}
