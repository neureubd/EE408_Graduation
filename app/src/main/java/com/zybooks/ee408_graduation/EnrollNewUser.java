package com.zybooks.ee408_graduation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnrollNewUser extends AppCompatActivity {
    private Button enrollBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_new_user);

        enrollBack = (Button) findViewById(R.id.enrollBack);

        enrollBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToMainPage();
            }
        });
    }

    public void returnToMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}