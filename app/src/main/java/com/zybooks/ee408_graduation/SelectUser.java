package com.zybooks.ee408_graduation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectUser extends AppCompatActivity {
    private Button selectBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        selectBack = (Button) findViewById(R.id.selectBack);
        selectBack.setOnClickListener(view -> returnToMainPage());
    }

    public void returnToMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}