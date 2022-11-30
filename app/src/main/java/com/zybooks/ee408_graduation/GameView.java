package com.zybooks.ee408_graduation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameView extends AppCompatActivity {
    private Button gameBack;
    private Button gameStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);

        gameBack = (Button) findViewById(R.id.gameBack);
        gameStart = (Button) findViewById(R.id.gameStart);

        gameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        gameBack.setOnClickListener(new View.OnClickListener() {
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
//Test. - Dalton
/*
* Notes for making the game
* on start button summon book and arrow
* when swipe book correct arrow gone
* summon broken book
* summon book and new arrow
* new book appear
* repeat...
* when final number == goal number end game
* */