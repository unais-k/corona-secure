package com.example.covicareproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewMedicineReqStatus extends AppCompatActivity {
    ListView lv;

    SharedPreferences sh;
    RequestQueue rq;
    String lid="",utype="";

    public static ArrayList<String> mname,des,mstatus,full;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicine_req_status);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");
        rq= Volley.newRequestQueue(this);

        lv=(ListView) findViewById(R.id.lv);

        mname=new ArrayList<>();
        des=new ArrayList<>();
        mstatus=new ArrayList<>();
        full=new ArrayList<>();

        MedicineRequestStatus();
    }

    private void MedicineRequestStatus(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "MedicineRequestStatus", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job=new JSONObject(response);
                    String status=job.getString("status");
                    if (status.equalsIgnoreCase("success")){

                        JSONArray jar=job.getJSONArray("results");
                        for(int i=0;i<jar.length();i++) {
                            JSONObject jo = jar.getJSONObject(i);
                            mname.add(jo.getString("medicine_name"));
                            des.add(jo.getString("description"));
                            mstatus.add(jo.getString("status"));
                            full.add("Medicine Name : "+jo.getString("medicine_name")+"\nDetails : "+jo.getString("description")+"\nStatus : "+jo.getString("status"));
                        }
                        ArrayAdapter<String> ad=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,full);
                        lv.setAdapter(ad);
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
                return p;


            }
        };
        rq.add(ja);
    }

}