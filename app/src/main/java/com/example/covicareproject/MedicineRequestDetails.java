package com.example.covicareproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class MedicineRequestDetails extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6,t7;
    Button b1;

    SharedPreferences sh;
    RequestQueue rq;
    String lid="",utype="",reqid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_request_details);

        t1=(TextView) findViewById(R.id.textView1);
        t2=(TextView) findViewById(R.id.textView2);
        t3=(TextView) findViewById(R.id.textView3);
        t4=(TextView) findViewById(R.id.textView4);
        t5=(TextView) findViewById(R.id.textView5);
        t6=(TextView) findViewById(R.id.textView6);
        t7=(TextView) findViewById(R.id.textView7);
        b1=(Button) findViewById(R.id.button1);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");
        rq= Volley.newRequestQueue(this);


        t1.setText(ViewApprovedMedRequest.mname.get(ViewApprovedMedRequest.pos));
        t2.setText(ViewApprovedMedRequest.hname.get(ViewApprovedMedRequest.pos));
        t3.setText(ViewApprovedMedRequest.hno.get(ViewApprovedMedRequest.pos));
        t4.setText(ViewApprovedMedRequest.address.get(ViewApprovedMedRequest.pos));
        t5.setText(ViewApprovedMedRequest.med_name.get(ViewApprovedMedRequest.pos));
        t6.setText(ViewApprovedMedRequest.date.get(ViewApprovedMedRequest.pos));
        t7.setText(ViewApprovedMedRequest.details.get(ViewApprovedMedRequest.pos));

        reqid=ViewApprovedMedRequest.mrid.get(ViewApprovedMedRequest.pos);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicineRequestUpdate();
            }
        });


    }

    private void MedicineRequestUpdate(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "MedicineRequestUpdate", new Response.Listener<String>() {
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