package com.huruwo.slidebyhand;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.GsonUtils;
import com.luozm.captcha.Captcha;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private ArrayList<ActionPoint> actionPoints = new ArrayList<>();
    private float start_x = 0,start_y =0,end_x = 0,end_y=0 ;
    private String path = "{}";
    private Captcha captCha;
    private EditText edit0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG,dip2px(getApplicationContext(),200)+"");
        captCha = (Captcha) findViewById(R.id.captCha);
        edit0 = (EditText) findViewById(R.id.edit_0);



        captCha.setCaptchaListener(new Captcha.CaptchaListener() {
            @Override
            public String onAccess(long time) {
                Toast.makeText(MainActivity.this,"验证成功,上传path",Toast.LENGTH_SHORT).show();
                captCha.reset(true);

                uploadPath(end_x,end_y,path);

                edit0.setText(path);

                return "验证通过,耗时"+time+"毫秒";
            }

            @Override
            public String onFailed(int failedCount) {
                Toast.makeText(MainActivity.this,"验证失败",Toast.LENGTH_SHORT).show();
                captCha.reset(true);
                return "验证失败,已失败"+failedCount+"次";
            }

            @Override
            public String onMaxFailed() {
                Toast.makeText(MainActivity.this,"验证超过次数，你的帐号被封锁",Toast.LENGTH_SHORT).show();
                return "验证失败,帐号已封锁";
            }
        });
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
                 end_x = event.getX()-start_x;
                 end_y = event.getY()-start_y;
                 actionPoints.add(new ActionPoint(time,end_x,end_y,event.getAction()));
                 path = GsonUtils.toJson(actionPoints);
                Log.e(TAG, path);
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

    private void uploadPath(float end_x,float end_y,String path_json){
        String url = "http://192.168.100.10:8001/slide_path?end_x="+end_x+"&end_y="+end_y+"&path_json="+path_json;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.body().string());
            }
        });
    }

}