package com.zybooks.ee408_graduation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.lights.LightState;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectUser extends AppCompatActivity {
    private Button selectBack;
    private Button selectPlay;
    private RadioGroup rg1;
    private RadioGroup rg2;
    //public String[] nameList = new String[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        rg1 = (RadioGroup) findViewById(R.id.radioGroup);
        rg2 = (RadioGroup) findViewById(R.id.radioGroup2);
        rg1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
        rg2.clearCheck();
        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);
        selectBack = (Button) findViewById(R.id.selectBack);
        selectPlay = (Button) findViewById(R.id.selectPlay);
        getUserList();
        selectBack.setOnClickListener(view -> returnToMainPage());
        selectPlay.setOnClickListener(view ->checkForUser());

    }

    public void returnToMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void playGame(String userName){
        Intent intent = new Intent(this, GameView.class);
        intent.putExtra("Username",userName);
        startActivity(intent);
    }

    public void enrollNewUser(){
        Intent intent = new Intent(this, EnrollNewUser.class);
        startActivity(intent);
    }

    public void checkForUser(){
        RadioButton rb = null;

        if(rg1.getCheckedRadioButtonId() != -1){rb = (RadioButton) findViewById(rg1.getCheckedRadioButtonId());}
        else if(rg2.getCheckedRadioButtonId() != -1){rb = (RadioButton) findViewById(rg2.getCheckedRadioButtonId());}
        else{
            Toast.makeText(getApplicationContext(), "No User Selected",Toast.LENGTH_LONG).show();
            return;
        }
        // Check for user
        if (rb.getText().toString().contains("Profile")) {
            enrollNewUser();
        } else {
            playGame((String) rb.getText());
        }
    }

    public void getUserList(){ //rename
        String[] nameList = new String[6];
        String baseUrl = "http://10.0.2.2:5000/";
        new ApiRequest(SelectUser.this, baseUrl + "get_usernames", new JSONObject()) {
            @Override
            public void PostCallback(JSONObject jsonObject, VolleyError volleyError) {
                JSONArray names = jsonObject.optJSONArray("users");
                for(int i=0;i < 6; i++) {
                    if(i < names.length()) {nameList[i] = names.optString(i);}
                    else{nameList[i] = "";}
                }
                setRadioButtonText(nameList);
            }
        };
    }
    public void setRadioButtonText(String[] nameList){
        // Profile 1
        if (nameList[0] != "") {RadioButton b1 = (RadioButton) rg1.getChildAt(0);b1.setText(nameList[0]);}
        else{((RadioButton) rg1.getChildAt(0)).setText("Profile 1");}
        //Profile 2
        if (nameList[1] != "") {((RadioButton) rg2.getChildAt(0)).setText(nameList[1]);}
        else{((RadioButton) rg2.getChildAt(0)).setText("Profile 2");}
        // Profile 3
        if (nameList[2] != "") {((RadioButton) rg1.getChildAt(1)).setText(nameList[2]);}
        else{((RadioButton) rg1.getChildAt(1)).setText("Profile 3");}
        // Profile 4
        if (nameList[3] != "") {((RadioButton) rg2.getChildAt(1)).setText(nameList[3]);}
        else{((RadioButton) rg2.getChildAt(1)).setText("Profile 4");}
        // Profile 5
        if (nameList[4] != "") {((RadioButton) rg1.getChildAt(2)).setText(nameList[4]);}
        else{((RadioButton) rg1.getChildAt(2)).setText("Profile 5");}
        // Profile 6
        if (nameList[5] != "") {((RadioButton) rg2.getChildAt(2)).setText(nameList[5]);}
        else{((RadioButton) rg2.getChildAt(2)).setText("Profile 6");}

    }

    // The following section of code was taken from https://stackoverflow.com/questions/10425569/radiogroup-with-two-columns-which-have-ten-radiobuttons
    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg2.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                rg2.clearCheck(); // clear the second RadioGroup!
                rg2.setOnCheckedChangeListener(listener2); //reset the listener
                Log.e("XXX2", "do the work");
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg1.setOnCheckedChangeListener(null);
                rg1.clearCheck();
                rg1.setOnCheckedChangeListener(listener1);
                Log.e("XXX2", "do the work");
            }
        }
    };
}