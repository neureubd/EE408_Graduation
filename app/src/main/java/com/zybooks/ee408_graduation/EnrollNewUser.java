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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EnrollNewUser extends AppCompatActivity {
    private Button enrollBack;
    private Button confirmEnroll;
    private EditText newUserName;
    private boolean profileSpaceAvailable = false;
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
                onConfirm(newUserName.getText().toString());
            }
        });

        enrollBack.setOnClickListener(view -> returnToMainPage());
    }

    public void returnToMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onConfirm(String newUser){ //rename
        String[] nameList = new String[6];
        String baseUrl = "http://10.0.2.2:5000/";
        new ApiRequest(EnrollNewUser.this, baseUrl + "get_usernames", new JSONObject()) {
            @Override
            public void PostCallback(JSONObject jsonObject, VolleyError volleyError) {
                JSONArray names = jsonObject.optJSONArray("users");
                for(int i=0;i < 6; i++) {
                    if(i < names.length()) {nameList[i] = names.optString(i);}
                    else{nameList[i] = ""; profileSpaceAvailable = true;}
                }
                if(profileSpaceAvailable == false){
                    Toast.makeText(EnrollNewUser.this.getApplicationContext(), "There are already 6 user profiles. Please delete an existing profile inorder to create a new one.", Toast.LENGTH_LONG).show();
                }
                else{
                    finishEnroll(newUser);
                }
            }
        };
    }

    public void finishEnroll(String newUserName){

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("username", newUserName);
            new ApiRequest(EnrollNewUser.this, baseUrl + "create_user", jsonParams) {
                @Override
                public void PostCallback(JSONObject jsonObject, VolleyError volleyError) {
                    if(volleyError == null)
                    {
                        String errString = jsonObject.optString("error");
                        if(errString.isEmpty()){}
                        else{
                            Toast.makeText(EnrollNewUser.this.getApplicationContext(), "This user already exists", Toast.LENGTH_LONG).show();
                            returnToMainPage();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "ERROR: " + volleyError.getMessage(), Toast.LENGTH_LONG).show();
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