package com.zybooks.ee408_graduation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnrollNewUser extends AppCompatActivity {
    private Button enrollBack;
    private Button confirmEnroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_new_user);

        enrollBack = (Button) findViewById(R.id.enrollBack);
        confirmEnroll = (Button) findViewById(R.id.enrollConfirm);

        confirmEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEnroll();
            }
        });

        enrollBack.setOnClickListener(view -> returnToMainPage());
    }

    public void returnToMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void finishEnroll(){
        Intent intent = new Intent(this, GameView.class);
        startActivity(intent);
    }
}