package com.example.covicareproject;

import androidx.appcompat.app.AppCompatActivity;

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

public class AddHouse extends AppCompatActivity {
    EditText ed1,ed2,ed3,ed4;
    Button B1;

    SharedPreferences sh;
    RequestQueue rq;
    String lid="",utype="",hname="",hno="",address="",rno="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);

        ed1=(EditText) findViewById(R.id.edittext1);
        ed2=(EditText) findViewById(R.id.edittext2);
        ed3=(EditText) findViewById(R.id.edittext3);
        ed4=(EditText) findViewById(R.id.edittext4);
        B1=(Button) findViewById(R.id.button1);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");
        rq= Volley.newRequestQueue(this);

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hno = ed1.getText().toString();
                hname = ed2.getText().toString();
                address = ed3.getText().toString();
                rno = ed4.getText().toString();

                if (hno.equalsIgnoreCase(""))
                {
                    ed1.setError("Invalid House Number");
                    ed1.setFocusable(true);
                } else if (hname.equalsIgnoreCase(""))
                {
                    ed2.setError("Invalid House Name");
                    ed2.setFocusable(true);

                } else if (address.equalsIgnoreCase(""))
                {
                    ed3.setError("Invalid address");
                    ed3.setFocusable(true);
                }else if (rno.equalsIgnoreCase(""))
                {
                    ed4.setError("Invalid Ration Number");
                    ed4.setFocusable(true);

                }

                else {
                    AddFamily();
                }
            }
        });

    }

    private void AddFamily(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "AddFamily", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job=new JSONObject(response);
                    String status=job.getString("status");
                    if (status.equalsIgnoreCase("succes")){


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_LONG).show();

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
                p.put("lid",lid);
                p.put("hname",hname);
                p.put("hno",hno);
                p.put("rno",rno);
                p.put("address",address);
                return p;


            }
        };
        rq.add(ja);
    }

}