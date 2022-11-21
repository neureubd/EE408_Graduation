package com.zybooks.ee408_graduation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button newUserView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newUserView = (Button) findViewById(R.id.new_user_view);

        newUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newUserView();
            }
        });
    }

    private void newUserView (){
        Intent intent = new Intent(this, EnrollNewUser.class);
        startActivity(intent);
    }
}