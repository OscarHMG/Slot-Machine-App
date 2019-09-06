package com.rayzem.slot_machine_app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rayzem.slot_machine_app.CustomView.ImageViewScrolling;
import com.rayzem.slot_machine_app.CustomView.InterfaceEventEndListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView slot1, slot2 , slot3;
    private Wheel wheel1, wheel2, wheel3;

    private Button btn;

    private boolean isStarted;

    public static int GLOBAL_SCORE = 5000;

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;

    private LinearLayout container_logo;
    private FrameLayout container_slot_machine;

    private Button btn_try_again;

    private long mRotationTime = 0;
    private static final int ROTATION_WAIT_TIME_MS = 100;


    TextView score;

    static Random RANDOM = new Random();

    public static long randomLong(long lower, long upper) {
        return lower + (long) (RANDOM.nextDouble() * (upper - lower));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        initView();

        btn_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorManager.registerListener(MainActivity.this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
                btn_try_again.setVisibility(View.GONE);
                container_slot_machine.setVisibility(View.GONE);
                container_logo.setVisibility(View.VISIBLE);


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Toast.makeText(this, "Spin your phone", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }


    public Wheel wheelInit(final ImageView slot, long init, long end){
        Wheel wheel = new Wheel(new Wheel.WheelListener() {
            @Override
            public void newItem(final int img) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        slot.setImageResource(img);
                    }
                });

            }
        }, 200, randomLong(init, end));

        wheel.start();

        return wheel;
    }

    public void initView(){
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        slot1 = findViewById(R.id.img1);
        slot2 = findViewById(R.id.img2);
        slot3 = findViewById(R.id.img3);

        score = findViewById(R.id.score);

        btn_try_again = findViewById(R.id.btnTryAgain);



        container_logo = findViewById(R.id.container_logo);
        container_slot_machine = findViewById(R.id.frame_bar);

        score.setText(getResources().getString(R.string.amount_text) +": $"+ GLOBAL_SCORE);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        long now = System.currentTimeMillis();

        if((now - mRotationTime) > ROTATION_WAIT_TIME_MS){
            if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){

                if(Math.abs(event.values[2]) > 2){
                    btn_try_again.setVisibility(View.GONE);
                    container_slot_machine.setVisibility(View.GONE);
                    container_logo.setVisibility(View.VISIBLE);

                }else if(Math.abs(event.values[2])>= 1.5 && Math.abs(event.values[2]) <= 2){
                    if(wheel1 == null || wheel2 == null || wheel3 == null) {
                        btn_try_again.setVisibility(View.GONE);
                        container_slot_machine.setVisibility(View.VISIBLE);
                        container_logo.setVisibility(View.GONE);

                        wheel1 = wheelInit(slot1, 0, 200);
                        wheel2 = wheelInit(slot2, 150, 400);
                        wheel3 = wheelInit(slot3, 150, 400);
                        isStarted = true;
                    }
                }else if(Math.abs(event.values[2]) < 1.5) {

                    if(wheel1 != null && wheel2 != null && wheel3 != null) {
                        wheel1.stopWheel();
                        wheel2.stopWheel();
                        wheel3.stopWheel();

                        //Stop
                        sensorManager.unregisterListener(this);

                        if (wheel1.currentIndex == wheel2.currentIndex && wheel2.currentIndex == wheel3.currentIndex) {
                            Toast.makeText(MainActivity.this, R.string.win_big, Toast.LENGTH_LONG).show();
                            GLOBAL_SCORE += 300;
                            score.setText(getResources().getString(R.string.amount_text) + ": $" + GLOBAL_SCORE);
                        } else if (wheel1.currentIndex == wheel2.currentIndex || wheel2.currentIndex == wheel3.currentIndex || wheel1.currentIndex == wheel3.currentIndex) {
                            Toast.makeText(MainActivity.this, R.string.win_small, Toast.LENGTH_LONG).show();
                            GLOBAL_SCORE += 50;
                            score.setText(getResources().getString(R.string.amount_text) + ": $" + GLOBAL_SCORE);
                        } else {
                            Toast.makeText(MainActivity.this, R.string.lose, Toast.LENGTH_LONG).show();
                            GLOBAL_SCORE -= 50;
                        }


                        btn_try_again.setVisibility(View.VISIBLE);

                        wheel1 = null;
                        wheel2 = null;
                        wheel3 = null;
                    }


                    isStarted = false;


                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}






