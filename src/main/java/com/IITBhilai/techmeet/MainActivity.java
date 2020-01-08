package com.IITBhilai.techmeet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    Button tv,b1,b2;
    TextView t1;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        t1 = findViewById(R.id.textView4);
        b1=findViewById(R.id.button3);
        b2=findViewById(R.id.button5);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Will display record of potholes captured",Toast.LENGTH_LONG).show();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Will display reports/updates of complaints lodged",Toast.LENGTH_LONG).show();
            }
        });
        prefs=getApplicationContext().getSharedPreferences("pothole", Context.MODE_PRIVATE);
        t1.setText("   "+prefs.getLong("score",0)+" \nSCORE");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UnityPlayerActivity.class);
                startActivity(intent);
            }
        });
        LogThread a = new LogThread();
        a.start();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(MainActivity.this, "GPS Locations Sent successfully! ", Toast.LENGTH_LONG).show();
                    MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
                        @Override
                        public void gotLocation(Location location){
                            Toast.makeText(MainActivity.this, "Latitude = "+location.getLatitude()+"\nLongitude = "+location.getLongitude(), Toast.LENGTH_LONG).show();
                        }
                    };
                    MyLocation myLocation = new MyLocation();
                    myLocation.getLocation(MainActivity.this, locationResult);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("score",prefs.getLong("score",0)+20);
                    editor.apply();
            }
            return true;
        }
    });

    public class LogThread extends Thread{
        LogsUtil util;

        LogThread(){
            util=new LogsUtil();
        }

        @Override
        public void run() {
            if(!isInterrupted()) {
                while(true){
                    long q=System.currentTimeMillis();
                    while(System.currentTimeMillis()-q<1000);
                    if(util.readLogs()==1){
                        handler.obtainMessage(1,0,-1,"".getBytes()).sendToTarget();
                    }
                }
            }else{
                return ;
            }
        }
    }
}
