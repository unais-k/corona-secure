package com.example.covicareproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class UserHome extends AppCompatActivity {
    Button B1,B2,B3,B4,B5,B6,B7;

    SharedPreferences sh;
    RequestQueue rq;
    String lid="",utype="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");
        rq= Volley.newRequestQueue(this);

        B1=(Button) findViewById(R.id.button1);
        B2=(Button) findViewById(R.id.button2);
        B3=(Button) findViewById(R.id.button3);
        B4=(Button) findViewById(R.id.button4);
        B5=(Button) findViewById(R.id.button5);
        B6=(Button) findViewById(R.id.button6);
        B7=(Button) findViewById(R.id.button7);

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ViewEmergencyOptions.class);
                startActivity(i);
            }
        });

        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),SendMedicineRequest.class);
                startActivity(i);
            }
        });
        B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ViewMedicineReqStatus.class);
                startActivity(i);
            }
        });
        B4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ViewAshaworkerAndCall.class);
                startActivity(i);
            }
        });
        B5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ViewVolunteers.class);
                startActivity(i);

            }
        });
        B6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });
        B7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),NearestHouse.class);
                startActivity(i);
            }
        });

    }
}