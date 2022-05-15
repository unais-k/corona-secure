package com.example.covicareproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {
    EditText ed1,ed2;
    Button B1;

    SharedPreferences sh;
    RequestQueue rq;
    String lid="",utype="",email="",password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");
        rq= Volley.newRequestQueue(this);

        ed1=(EditText) findViewById(R.id.edittext1);
        ed2=(EditText) findViewById(R.id.edittext2);
        B1=(Button) findViewById(R.id.button1);

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=ed1.getText().toString();
                password=ed2.getText().toString();

                login();
            }
        });

    }

    private void login(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "Login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job=new JSONObject(response);
                    String status=job.getString("status");

                    if (status.equalsIgnoreCase("success")){
                        lid=job.getString("lid");
                        String utype=job.getString("usertype");
                        SharedPreferences.Editor ed= sh.edit();
                        ed.putString("lid",lid);
                        ed.putString("utype",utype);
                        ed.commit();
                        if(utype.equalsIgnoreCase("ashaworker"))
                        {
                            Intent aw=new Intent(getApplicationContext(),AshaWorkerHome.class);
                            startActivity(aw);
                        }else if(utype.equalsIgnoreCase("volunteer"))
                        {
                            Intent aw=new Intent(getApplicationContext(),VolunteerHome.class);
                            startActivity(aw);
                        }else if(utype.equalsIgnoreCase("member"))
                        {
                            Intent aw=new Intent(getApplicationContext(),UserHome.class);
                            startActivity(aw);
                        }else{

                        }

                    }
                    else
                    {


                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error..1..."+e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error..1..."+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String,String> getParams(){
                Map<String,String>p= new HashMap<String,String>();
                p.put("email",email);
                p.put("pwd",password);
                return p;


            }
        };
        rq.add(ja);
    }
}

