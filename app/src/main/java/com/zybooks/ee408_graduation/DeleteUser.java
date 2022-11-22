package com.zybooks.ee408_graduation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DeleteUser extends AppCompatActivity {
    private Button deleteBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        deleteBack = findViewById(R.id.deleteBack);

        deleteBack.setOnClickListener(view -> returnToMainPage());
    }

    public void returnToMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}