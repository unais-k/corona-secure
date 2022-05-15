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

public class SendMedicineRequest extends AppCompatActivity {
    EditText ed1,ed2;
    Button B1;

    SharedPreferences sh;
    RequestQueue rq;
    String lid="",utype="",medname="",details="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_medicine_request);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");
        rq= Volley.newRequestQueue(this);

        ed1=(EditText) findViewById(R.id.edittext1);
        ed2=(EditText) findViewById(R.id.edittext2);
        B1=(Button) findViewById(R.id.button1);

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medname=ed1.getText().toString();
                details=ed2.getText().toString();

                if (medname.equalsIgnoreCase(""))
                {
                    ed1.setError("Invalid Medicine Name");
                    ed1.setFocusable(true);
                } else if (details.equalsIgnoreCase(""))
                {
                    ed2.setError("Invalid Details ");
                    ed2.setFocusable(true);

                }else {

                    SendMedicineRequest();
                }
            }
        });



    }

    private void SendMedicineRequest(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "SendMedicineRequest", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job=new JSONObject(response);
                    String status=job.getString("status");
                    if (status.equalsIgnoreCase("success")){


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
                p.put("mnam",medname);
                p.put("details",details);
                return p;


            }
        };
        rq.add(ja);
    }

}