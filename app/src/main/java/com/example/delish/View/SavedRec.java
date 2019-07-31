package com.example.delish.View;
import android.content.Intent;
import android.os.Bundle;

import com.example.delish.R;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;
import android.widget.Toast;
public class SavedRec extends AppCompatActivity {
    private float x1,x2; //USED IN SWIPE DETECTION
    static final int MIN_DISTANCE = 150; //USED IN SWIPE DETECTION
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_rec);


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
                    startActivity(new Intent(SavedRec.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                /*else if ((-1 * deltaX) > MIN_DISTANCE)
                {
                    Toast.makeText(this, "r2l swipe", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SavedRec.this, SavedRec.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }*/
                break;
        }
        return super.onTouchEvent(event);
    }
}