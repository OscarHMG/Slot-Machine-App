package com.rayzem.slot_machine_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rayzem.slot_machine_app.CustomView.ImageViewScrolling;
import com.rayzem.slot_machine_app.CustomView.InterfaceEventEndListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements InterfaceEventEndListener {

    ImageView btnUp, btnDown;

    ImageViewScrolling image, image2, image3;

    TextView score;

    int count_done = 0;

    public static int GLOBAL_SCORE = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        btnDown = findViewById(R.id.btn_Down);
        btnUp = findViewById(R.id.btn_Up);

        image = findViewById(R.id.image_1);
        image2 = findViewById(R.id.image_2);
        image3 = findViewById(R.id.image_3);

        score = findViewById(R.id.score);

        image.setEventEndListener(this);
        image2.setEventEndListener(this);
        image3.setEventEndListener(this);

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GLOBAL_SCORE >= 50){
                    btnUp.setVisibility(View.GONE);
                    btnDown.setVisibility(View.VISIBLE);

                    image.setValueRandom(new Random().nextInt(6) , new Random().nextInt((15-5) +1) +5);
                    image2.setValueRandom(new Random().nextInt(6) , new Random().nextInt((15-5) +1) +5);
                    image3.setValueRandom(new Random().nextInt(6) , new Random().nextInt((15-5) +1) +5);
                    GLOBAL_SCORE -= 50;

                    score.setText(""+GLOBAL_SCORE);
                }else{
                    Toast.makeText(MainActivity.this, "Not enough money", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void endEventListener(int result, int cont) {
        if(count_done < 2){
            count_done ++;
        }else{
            btnDown.setVisibility(View.GONE);
            btnUp.setVisibility(View.VISIBLE);
            count_done = 0;

            if(image.getValue() == image2.getValue() && image2.getValue() == image3.getValue()){
                Toast.makeText(this, "YOU WIN BIG PRIZE!", Toast.LENGTH_LONG).show();
                GLOBAL_SCORE += 300;
                score.setText(""+GLOBAL_SCORE);
            }else if(image.getValue() == image2.getValue() || image2.getValue() == image3.getValue() || image.getValue() == image3.getValue()){
                Toast.makeText(this, "YOU WIN SMALL PRIZE!", Toast.LENGTH_LONG).show();
                GLOBAL_SCORE += 50;
                score.setText(""+GLOBAL_SCORE);
            }else{
                Toast.makeText(this, "YOU LOSE!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
