package com.zybooks.ee408_graduation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameView extends AppCompatActivity {
    private Button gameBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);

        gameBack = (Button) findViewById(R.id.gameBack);

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
