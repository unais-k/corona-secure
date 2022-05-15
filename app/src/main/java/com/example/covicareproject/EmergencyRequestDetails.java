package com.example.covicareproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
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

public class EmergencyRequestDetails extends AppCompatActivity {
    TextView t1,t2,t3,t4;
    Button b1;

    SharedPreferences sh;
    RequestQueue rq;
    String lid="",utype="",reqid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_request_details);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");
        rq= Volley.newRequestQueue(this);


        t1=(TextView) findViewById(R.id.textView1);
        t2=(TextView) findViewById(R.id.textView2);
        t3=(TextView) findViewById(R.id.textView3);
        t4=(TextView) findViewById(R.id.textView4);
        b1=(Button) findViewById(R.id.button1);

        reqid=ViewEmergencyOptionRequest.rid.get(ViewEmergencyOptionRequest.pos);

        t1.setText(ViewEmergencyOptionRequest.member_name.get(ViewEmergencyOptionRequest.pos));
        t2.setText(ViewEmergencyOptionRequest.house_name.get(ViewEmergencyOptionRequest.pos));
        t3.setText(ViewEmergencyOptionRequest.address.get(ViewEmergencyOptionRequest.pos));
        t4.setText(ViewEmergencyOptionRequest.emergencyoption.get(ViewEmergencyOptionRequest.pos));


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmergencyRequestUpdate();
            }
        });


    }

    private void EmergencyRequestUpdate(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "EmergencyRequestUpdate", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job=new JSONObject(response);
                    String status=job.getString("status");
                    if (status.equalsIgnoreCase("succes")){

                        Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
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
                p.put("reqid",reqid);
                return p;


            }
        };
        rq.add(ja);
    }

}