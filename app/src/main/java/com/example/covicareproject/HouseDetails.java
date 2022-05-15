package com.example.covicareproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HouseDetails extends AppCompatActivity {
    TextView t1,t2,t3,t4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        t1=(TextView) findViewById(R.id.textView2);
        t2=(TextView) findViewById(R.id.textView4);
        t3=(TextView) findViewById(R.id.textView6);
        t4=(TextView) findViewById(R.id.textView8);

        t1.setText(ViewHouse.hno.get(ViewHouse.pos));
        t2.setText(ViewHouse.hname.get(ViewHouse.pos));
        t3.setText(ViewHouse.address.get(ViewHouse.pos));
        t4.setText(ViewHouse.ration.get(ViewHouse.pos));

    }

}