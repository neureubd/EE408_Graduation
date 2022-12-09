package com.zybooks.ee408_graduation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeleteUser extends AppCompatActivity {
    private Button deleteBack;
    private Button deleteButton;
    private RadioGroup rg1;
    private RadioGroup rg2;
    String baseUrl = "http://10.0.2.2:5000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        rg1 = (RadioGroup) findViewById(R.id.radioGroup);
        rg2 = (RadioGroup) findViewById(R.id.radioGroup2);
        rg1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
        rg2.clearCheck();
        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);
        deleteBack = (Button) findViewById(R.id.deleteBack);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        getUserList();
        deleteBack.setOnClickListener(view -> returnToMainPage());
        deleteButton.setOnClickListener(view -> checkForUser());

    }

    public void returnToMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg2.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                rg2.clearCheck(); // clear the second RadioGroup!
                rg2.setOnCheckedChangeListener(listener2); //reset the listener
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

    public void getUserList(){ //rename
        String[] nameList = new String[6];
        new ApiRequest(DeleteUser.this, baseUrl + "get_usernames", new JSONObject()) {
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
        if (nameList[0] != "") {
            RadioButton b1 = (RadioButton) rg1.getChildAt(0);b1.setText(nameList[0]);}
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
            Toast.makeText(getApplicationContext(), "This is an inactive profile",Toast.LENGTH_LONG).show();
            return;
        } else {
            deleteProfile((String) rb.getText());
        }
    }

    public void deleteProfile(String newUserName){
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("username", newUserName);
            new ApiRequest(DeleteUser.this, baseUrl + "delete_user", jsonParams) {
                @Override
                public void PostCallback(JSONObject jsonObject, VolleyError volleyError) {
                    String errString = jsonObject.optString("error");
                    getUserList();
                }
            };
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}