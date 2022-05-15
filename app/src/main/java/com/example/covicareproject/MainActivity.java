package com.example.covicareproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    EditText ed1;
    Button b1;

    SharedPreferences sh;
    RequestQueue rq;
    String ip="",url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        rq= Volley.newRequestQueue(this);


        ed1=(EditText) findViewById(R.id.edittext1);
        b1=(Button) findViewById(R.id.button1);

        ed1.setText(sh.getString("ip",""));

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ip=ed1.getText().toString();
                url="http://"+ip+":5000/";
                SharedPreferences.Editor ee=sh.edit();
                ee.putString("url",url);
                ee.putString("ip",ip);
                ee.commit();
                Intent I=new Intent(getApplicationContext(),Login.class);
                startActivity(I);


            }
        });

    }
}