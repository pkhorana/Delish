package com.example.delish.View;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.delish.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Cart extends AppCompatActivity {
    private float x1,x2; //USED IN SWIPE DETECTION
    static final int MIN_DISTANCE = 150; //USED IN SWIPE DETECTION
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
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
                /*if (deltaX > MIN_DISTANCE)
                {
                    Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Cart.this, Cart.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }*/
                if ((-1 * deltaX) > MIN_DISTANCE)
                {
                    Toast.makeText(this, "r2l swipe", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Cart.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
        }
        return super.onTouchEvent(event);
    }


}
