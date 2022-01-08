package com.huruwo.slidebyhand;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private ArrayList<ActionPoint> actionPoints = new ArrayList<>();
    private float start_x = 0,start_y =0 ;
    private View line1;
    private TextView textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG,dip2px(getApplicationContext(),200)+"");
        line1 = (View) findViewById(R.id.line1);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("本屏幕 200dp = "+dip2px(getApplicationContext(),200)+"px");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        long time = event.getEventTime()-event.getDownTime();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                actionPoints.clear();
                start_x = event.getX();
                start_y = event.getY();
                actionPoints.add(new ActionPoint(time,0.0f,0.0f,event.getAction()));
                break;
            case MotionEvent.ACTION_MOVE:
                actionPoints.add(new ActionPoint(time,event.getX()-start_x,event.getY()-start_y,event.getAction()));
                break;
            case MotionEvent.ACTION_UP:
                actionPoints.add(new ActionPoint(time,event.getX()-start_x,event.getY()-start_y,event.getAction()));
                Log.e(TAG, GsonUtils.toJson(actionPoints));
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}