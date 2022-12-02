package com.zybooks.ee408_graduation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class EnrollNewUser extends AppCompatActivity {
    private Button enrollBack;
    private Button confirmEnroll;
    private EditText newUserName;
    String baseUrl = "http://10.0.2.2:5000/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_new_user);

        enrollBack = (Button) findViewById(R.id.enrollBack);
        confirmEnroll = (Button) findViewById(R.id.enrollConfirm);
        newUserName = (EditText) findViewById(R.id.newUserName);

        confirmEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEnroll(newUserName.getText().toString());
            }
        });

        enrollBack.setOnClickListener(view -> returnToMainPage());
    }

    public void returnToMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void finishEnroll(String newUserName){
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("username", newUserName);
            new ApiRequest(EnrollNewUser.this, baseUrl + "create_user", jsonParams) {
                @Override
                public void PostCallback(JSONObject jsonObject, VolleyError volleyError) {
                    String errString = jsonObject.optString("error");
                    if(errString.isEmpty()){}
                    else{
                        Toast.makeText(EnrollNewUser.this.getApplicationContext(), "This user already exists", Toast.LENGTH_LONG).show();
                        returnToMainPage();
                    }
                }
            };
        }catch (JSONException e){
            e.printStackTrace();
        }
        Intent intent = new Intent(this, GameView.class);
        intent.putExtra("Username",newUserName);
        intent.putExtra("gameType", "enroll");
        startActivity(intent);
    }
}