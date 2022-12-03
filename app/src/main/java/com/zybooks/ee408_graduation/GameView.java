package com.zybooks.ee408_graduation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

public class GameView extends AppCompatActivity {
    private Button gameBack;
    private Button gameStart;
    final Handler handler = new Handler();
    private ImageView backGround;
    private boolean bookReady;
    private int count;
    private int direction;
    Random random = new Random();
    private int targetCount;
    private int cutDir;
    private TextView swipeCount, goalCount;
    private String swipeStr, targetStr;
    private boolean swipeStarted;
    private ImageView book1;
    private Animation bookUp;
    private Animation bookDown;
    private Animation arrowDown;
    private ImageView arrow;
    private Animation arrowUp;

    //Variables for collecting swipe data:
    float x = 0f;
    float y = 0f;
    Swipe<Float,Float,Long,Float,Float,Float> swipe = new Swipe(0,0,0,0,0,0);
    List<Swipe<Float,Float,Long,Float,Float,Float>> swipeData = new ArrayList<Swipe<Float,Float,Long,Float,Float,Float>>();
    List<Swipe<Float,Float,Long,Float,Float,Float>> preSwipe = new ArrayList<Swipe<Float,Float,Long,Float,Float,Float>>();
    String[] data = new String [31];

    String baseUrl = "http://10.0.2.2:5000/";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);

        gameBack = (Button) findViewById(R.id.gameBack);
        gameStart = (Button) findViewById(R.id.gameStart);
        backGround = (ImageView) findViewById(R.id.imageView2);
        swipeCount = (TextView) findViewById(R.id.swipeCount);
        goalCount = (TextView) findViewById(R.id.goalCount);

        String sessionUser = getIntent().getStringExtra("Username");
        String gameType = getIntent().getStringExtra("gameType");

        bookReady=false;

        //Set target count
        if(gameType.equals("test")){targetCount = 10;}
        else if(gameType.equals("enroll")){targetCount = 20;}
        else{
            Toast.makeText(GameView.this.getApplicationContext(), "Game not made correctly", Toast.LENGTH_LONG).show();
            onBackPressed();
            targetCount = 0;
        }

        targetStr=""+targetCount;
        goalCount.setText(targetStr);
        book1 = findViewById(R.id.book1);
        arrow = findViewById(R.id.arrow);
        bookUp = AnimationUtils.loadAnimation(this, R.anim.book_anim1);
        arrowUp = AnimationUtils.loadAnimation(this, R.anim.arrow_anim_up);
        bookDown = AnimationUtils.loadAnimation(this, R.anim.book_down_anim);
        arrowDown = AnimationUtils.loadAnimation(this, R.anim.book_down_anim);
        bookUp.setStartOffset(500);
        arrowUp.setStartOffset(500);
        bookDown.setFillAfter(true);
        arrowDown.setFillAfter(true);


        gameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                swipeStr=""+count;
                swipeCount.setText(swipeStr);
                fireNewBook();
            }
        });

        gameBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToMainPage();
            }
        });

        backGround.setOnTouchListener((v, event)->{
            swipeStarted=true;
            if(bookReady){
                final float x = event.getX();
                final float y = event.getY();
                float lastXAxis = x;
                float lastYAxis = y;
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    swipeData.clear();
                    //Start timer when that is added to the data
                    addSwipeData(lastXAxis,lastYAxis,event.getEventTime(),event.getPressure(),event.getSize(), event.getOrientation());
                }
                else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    addSwipeData(lastXAxis,lastYAxis,event.getEventTime(),event.getPressure(),event.getSize(),event.getOrientation());
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    addSwipeData(lastXAxis,lastYAxis,event.getEventTime(),event.getPressure(),event.getSize(),event.getOrientation());
                    //set data string using swipeData and preSwipe
                    if(swipeData.size() > 1) {// adjust to change min swipe length
                        data = setData(swipeData, preSwipe);
                        //clear preSwipe
                        preSwipe.clear();
                        //set preSwipe to swipeData
                        preSwipe = swipeData;
                        //clear swipeData
                        swipeData.clear();
                        //send data via api
                        sendSwipeData(sessionUser, gameType, data);
                    }else {swipeData.clear();}
                    //do what game logic tells us to do
                    //Toast.makeText(getApplicationContext(),"Data Collected",Toast.LENGTH_SHORT).show();
                    bookReady=false;
                    swipeStarted=false;
                }
                //DETERMINE ANGLE OF SWIPE
                if(/*Within Certain Angle*/direction==0){
                    cutDir=0;
                }
                else if(/*Within Certain Angle*/direction==1){
                    cutDir=1;
                }
                else if(/*Within Certain Angle*/direction==2){
                    cutDir=2;
                }
                else if(/*Within Certain Angle*/direction==3){
                    cutDir=3;
                }
                else if(/*Within Certain Angle*/direction==4){
                    cutDir=4;
                }
                else if(/*Within Certain Angle*/direction==5){
                    cutDir=5;
                }
                else if(/*Within Certain Angle*/direction==6){
                    cutDir=6;
                }
                else if(/*Within Certain Angle*/direction==7){
                    cutDir=7;
                }
                else{
                    //OUTPUT INCORRECT SIGNAL
                }
                //IF ANGLE IS CORRECT, RESET AND ADD COUNT
                if(cutDir==direction){
                    if(!swipeStarted) {
                        //CUT BOOK

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fireNewBook();
                                bookReady = false;
                            }
                        }, 1000);
                        count = count + 1;
                        swipeStr = "" + count;
                        swipeCount.setText(swipeStr);
                    }
                }
            }
            else{

            }
            if(count==targetCount){
                returnToMainPage();
            }
            return true;
        });

    }

    private void setTargetCount(int num){
        targetCount=num;
    }

    private void setArrowRotation(int num){
        if(num==0){
            arrow.setRotation(0);
        }
        else if(num==1){
            arrow.setRotation(45);
        }
        else if(num==2){
            arrow.setRotation(90);
        }
        else if(num==3){
            arrow.setRotation(135);
        }
        else if(num==4){
            arrow.setRotation(180);
        }
        else if(num==5){
            arrow.setRotation(225);
        }
        else if(num==6){
            arrow.setRotation(270);
        }
        else{
            arrow.setRotation(315);
        }
    }

    private void fireNewBook(){
        direction= random.nextInt(8);
        setArrowRotation(direction);
        bookReady=false;
        book1.startAnimation(bookUp);
        arrow.startAnimation(arrowUp);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                book1.startAnimation(bookDown);
                arrow.startAnimation(arrowDown);
            }
        }, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bookReady=true;
            }
        }, 2000);
    }

    public void returnToMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Swipe is an custom class
    public class Swipe<Fst,Snd,Time,Pres,FSize,Ori> {
        private Fst fst;
        private Snd snd;
        private Time time;
        private Pres pres;
        private FSize fSize;
        private Ori ori;
        public Swipe(Fst fst, Snd snd, Time time, Pres pres, FSize fSize, Ori ori){
            this.fst = fst;
            this.snd = snd;
            this.time = time;
            this.pres = pres;
            this.fSize = fSize;
            this.ori = ori;
        }
        public Fst getFst(){ return fst; }
        public Snd getSnd(){ return snd; }
        public Time getTime(){return time;}
        public Pres getPres(){return pres;}
        public FSize getFSize(){return fSize;}
        public Ori getOri(){return ori;}
        public void setFst(Fst fst){ this.fst = fst; }
        public void setSnd(Snd snd){ this.snd = snd; }
        public void setTime(Time time){ this.time = time; }
        public void setPres(Pres pres){ this.pres = pres; }
        public void setFSize(FSize fSize){this.fSize = fSize;}
        public void setOri(Ori ori){this.ori = ori;}
    }

    private void addSwipeData(float x, float y, long time, float pres, float fSize, float ori){
        Swipe swipe = new Swipe(x,y,time,pres,fSize, ori);
        swipeData.add(swipe);
    }

    public String[] setData(List<Swipe<Float,Float,Long,Float,Float,Float>> swipe, List<Swipe<Float,Float,Long,Float,Float,Float>> preSwipe){
        String[] data = new String [31];
        // Not Done: 21,23
        //inter_stroke_time (time between strokes)
        if(preSwipe.isEmpty()) {data[0] = Long.toString(0);}
        else{data[0] = Float.toString((float)(preSwipe.get(preSwipe.size()-1).getTime()-swipe.get(0).getTime())/1000);}
        //stroke_duration
        data[1] = Float.toString((float)(swipe.get(swipe.size()-1).getTime() - swipe.get(0).getTime())/1000);
        //start_x
        data[2] = Float.toString(swipe.get(0).getFst());
        //start_y
        data[3] = Float.toString(swipe.get(0).getSnd());
        //stop_x
        data[4] = Float.toString(swipe.get(swipe.size()-1).getFst());
        //stop_y
        data[5] = Float.toString(swipe.get(swipe.size()-1).getSnd());
        //direct_e2e_dist
        data[6] = Float.toString(disOfTwoPoints(0,swipe.size()-1,swipe));
        //mean_resultant_length (length between each point averaged)
        float sum = 0;
        for(int i=0;i<swipe.size()-1;i++){
            sum = sum + disOfTwoPoints(i, i+1,swipe);
        }
        data[7] = Float.toString(sum/swipe.size());
        //direction_enum
        float dir = calcDirectionTwoPoints(0,swipe.size()-1,swipe);
        if(dir >= -Math.PI && dir < (-Math.PI/2)){data[8] = Integer.toString(4);}
        if(dir <= 0 && dir > (-Math.PI/2)){data[8] = Integer.toString(2);}
        if(dir > 0 && dir <= (Math.PI/2)){data[8] = Integer.toString(3);}
        if(dir <= Math.PI && dir > (Math.PI/2)){data[8] = Integer.toString(1);}
        //dir_e2e_line
        data[9] = Float.toString(calcDirectionTwoPoints(0,swipe.size()-1,swipe));
        //20p_pairwise_velocity
        data[10] = Float.toString(velPoint((int) Math.round((swipe.size()-1)*.2),swipe));
        //50p_pairwise_velocity
        data[11] = Float.toString(velPoint((int) Math.round((swipe.size()-1)*.5),swipe));;
        //80p_pairwise_velocity
        data[12] = Float.toString(velPoint((int) Math.round((swipe.size()-1)*.8),swipe));;
        //20p_pairwise_acc
        data[13] = Float.toString(accPoint((int) Math.round((swipe.size()-1)*.2),swipe));
        //50p_pairwise_acc
        data[14] = Float.toString(accPoint((int) Math.round((swipe.size()-1)*.5),swipe));
        //80p_pairwise_acc
        data[15] = Float.toString(accPoint((int) Math.round((swipe.size()-1)*.8),swipe));
        //med_velocity_last3
        if(swipe.size() > 3) {
            float[] last3Vel = new float[3];
            int count = 0;
            for (int i = swipe.size() - 3; i < swipe.size(); i++) {
                last3Vel[count] = velPoint(i, swipe);
                count++;
            }
            Arrays.sort(last3Vel);
            data[16] = Float.toString(last3Vel[1]);
        }else{data[16] = Float.toString(0);}
        //lgst_dev_e2e_line
        float longestDist = 0;
        for(int i = 0; i < swipe.size();i++){
            if(devFromBestLineAtPoint(i,swipe)> longestDist){longestDist = devFromBestLineAtPoint(i, swipe);}}
        data[17] = Float.toString(longestDist);
        //20p_dev_e2e_line
        data[18] = Float.toString(devFromBestLineAtPoint((int) Math.round((swipe.size()-1)*.2), swipe));
        //50p_dev_e2e_line
        data[19] = Float.toString(devFromBestLineAtPoint((int) Math.round((swipe.size()-1)*.5), swipe));
        //80p_dev_e2e_line
        data[20] = Float.toString(devFromBestLineAtPoint((int) Math.round((swipe.size()-1)*.8), swipe));
        //average_direction
        float dirSum = 0;
        for(int i =0; i< swipe.size()-1;i++){
            dirSum = dirSum + calcDirectionTwoPoints(i, i+1, swipe);
        }
        data[21] = Float.toString(dirSum/(swipe.size()-1));
        //trajectory_length
        float trajectoryLength = 0;
        for(int i = 0; i < swipe.size()-1; i++){
            trajectoryLength = trajectoryLength + disOfTwoPoints(i,i+1,swipe);
        }
        data[22] = Float.toString(trajectoryLength);
        //ratio_e2e_dist_traj_length
        data[23] = Float.toString(disOfTwoPoints(0,swipe.size()-1,swipe)/trajectoryLength);
        //average_velocity
        float velSum = 0;
        for(int i = 0; i<swipe.size();i++){
            velSum = velSum + velPoint(i,swipe);
        }
        data[24] = Float.toString(velSum/swipe.size());
        //median_acc_first5
        if(swipe.size() > 5) {
            float accFirst5Sum = 0;
            for (int i = 0; i < 5; i++) {
                accFirst5Sum = accFirst5Sum + accPoint(i, swipe);
            }
            data[25] = Float.toString(accFirst5Sum / 5);
        }else{data[25] = Float.toString(0);}
        //midstroke_pressure
        data[26] = Float.toString(swipe.get((int) Math.round((swipe.size()-1)*.5)).getPres());
        //midstroke_area_covered
        data[27] = Float.toString(swipe.get((int) Math.round((swipe.size()-1)*.5)).getFSize());
        //midstroke_finger_orientation
        data[28] = Float.toString(swipe.get((int) Math.round((swipe.size()-1)*.5)).getOri());
        //finger_orientation_changed
        data[29] = Float.toString(0); //TA said it was fine for this feature to be set to 0
        //phone_orientation
        data[30] = Integer.toString(getResources().getConfiguration().orientation);
        return data;
    }

    public float velPoint(int point,List<Swipe<Float,Float,Long,Float,Float,Float>> swipe){
        float vel;
        if(swipe.size() == 1){
            vel = 0;
        }else {
            if (point == 0) {
                vel = disOfTwoPoints(point, point + 1, swipe) / (swipe.get(point).getTime() - swipe.get(point + 1).getTime());
            } else if (point == swipe.size() - 1) {
                vel = disOfTwoPoints(point - 1, point, swipe) / (swipe.get(point - 1).getTime() - swipe.get(point).getTime());
            } else {
                vel = (disOfTwoPoints(point, point + 1, swipe) / (swipe.get(point).getTime() - swipe.get(point + 1).getTime()) +
                        disOfTwoPoints(point - 1, point, swipe) / (swipe.get(point - 1).getTime() - swipe.get(point).getTime())) / 2;
            }
        }
        return vel;
    }
    public float accPoint(int point,List<Swipe<Float,Float,Long,Float,Float,Float>> swipe){
        float acc = 0;
        if (point == 0) {
            acc = (velPoint(point, swipe)-velPoint(point+1, swipe))/(swipe.get(point).getTime()-swipe.get(point+1).getTime());
        }
        else if(point == swipe.size()-1){
            acc = (velPoint(point-1, swipe)-velPoint(point, swipe))/(swipe.get(point-1).getTime()-swipe.get(point).getTime());
        }
        else{
            acc = ((velPoint(point, swipe)-velPoint(point+1, swipe))/(swipe.get(point).getTime()-swipe.get(point+1).getTime())+
                    (velPoint(point-1, swipe)-velPoint(point, swipe))/(swipe.get(point-1).getTime()-swipe.get(point).getTime()))/2;

        }
        return acc;
    }

    public float disOfTwoPoints(int point1, int point2, List<Swipe<Float,Float,Long,Float,Float,Float>> swipe){
        float dis = (float) Math.sqrt(
                (float)Math.pow((swipe.get(point1).getFst()-swipe.get(point2).getFst()),2)+
                        (float)Math.pow(swipe.get(point1).getSnd()-swipe.get(point2).getSnd(),2));
        return dis;
    }

    public float devFromBestLineAtPoint(int point, List<Swipe<Float,Float,Long,Float,Float,Float>> swipe){
        float x1 = swipe.get(0).getFst();
        float y1 = swipe.get(0).getSnd();
        float x2 = swipe.get(swipe.size()-1).getFst();
        float y2 = swipe.get(swipe.size()-1).getSnd();;
        float x3 = swipe.get(point).getFst();
        float y3 = swipe.get(point).getSnd();;
        float px=x2-x1;
        float py=y2-y1;
        float temp=(px*px)+(py*py);
        float u=((x3 - x1) * px + (y3 - y1) * py) / (temp);
        if(u>1){u=1;}
        else if(u<0){u=0;}
        float x = x1 + u * px;
        float y = y1 + u * py;

        float dx = x - x3;
        float dy = y - y3;
        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        return dist;

    }
    public float calcDirectionTwoPoints(int point1, int point2, List<Swipe<Float,Float,Long,Float,Float,Float>> swipe){
        float dir;
        float x = swipe.get(point2).getFst() - swipe.get(point1).getFst();
        float y = swipe.get(point2).getSnd() - swipe.get(point1).getSnd();
        dir = (float) (Math.atan2(y,x));
        return dir;
    }

    public void sendSwipeData(String sessionUser, String gameType, String[] swipeData) {
        String callType = "";
        String[] dataHeaders = new String[]{"inter_stroke_time", "stroke_duration", "start_x",
                "start_y", "stop_x", "stop_y", "direct_e2e_dist",
                "mean_resultant_length", "direction_enum", "dir_e2e_line",
                "20p_pairwise_velocity", "50p_pairwise_velocity",
                "80p_pairwise_velocity", "20p_pairwise_acc", "50p_pairwise_acc",
                "80p_pairwise_acc", "med_velocity_last3", "lgst_dev_e2e_line",
                "20p_dev_e2e_line", "50p_dev_e2e_line", "80p_dev_e2e_line",
                "average_direction", "trajectory_length", "ratio_e2e_dist_traj_length",
                "average_velocity", "median_acc_first5", "midstroke_pressure",
                "midstroke_area_covered", "midstroke_finger_orientation",
                "finger_orientation_changed", "phone_orientation"};
        if(gameType.equals("test")){
            callType = "test_user";
            JSONObject jsonParams = new JSONObject();
            try {
                // Put username
                jsonParams.put("username", sessionUser);

                // Put params:
                for(int i = 0; i <31;i++) {
                    jsonParams.put(dataHeaders[i], swipeData[i]);
                }

                new ApiRequest(GameView.this, baseUrl + callType, jsonParams) {
                    @Override
                    public void PostCallback(JSONObject jsonObject, VolleyError volleyError) {
                        Boolean testResult = jsonObject.optBoolean("prediction");
                        if(testResult != null){
                            Toast.makeText(GameView.this.getApplicationContext(), "Correct User: " + Boolean.toString(testResult), Toast.LENGTH_LONG).show();
                        }
                        if(volleyError == null){String errString = jsonObject.optString("error");}
                    }
                };
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(
                gameType.equals("enroll")){callType = "enroll_user";
            JSONObject jsonParams = new JSONObject();
            try {
                // Put username
                jsonParams.put("username", sessionUser);

                // Put params:
                for(int i = 0; i <31;i++) {
                    jsonParams.put(dataHeaders[i], swipeData[i]);
                }

                new ApiRequest(GameView.this, baseUrl + callType, jsonParams) {
                    @Override
                    public void PostCallback(JSONObject jsonObject, VolleyError volleyError) {
                        Boolean enrollStatus = jsonObject.optBoolean("trained");
                        if(enrollStatus != null){
                            Toast.makeText(GameView.this.getApplicationContext(), "Enrollment complete: " + Boolean.toString(enrollStatus), Toast.LENGTH_LONG).show();
                        }
                        if(volleyError == null){String errString = jsonObject.optString("error");}
                    }
                };
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(GameView.this.getApplicationContext(), "Game not made correctly", Toast.LENGTH_LONG).show();
            callType = "";
            onBackPressed();
        }
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

// Swipe is an custom class
