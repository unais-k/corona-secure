package com.example.covicareproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
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

public class ViewApprovedMedRequest extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lv;

    SharedPreferences sh;
    RequestQueue rq;
    String lid="",utype="";

    public static int pos;

    public static ArrayList<String> mrid,mname,hname,hno,address,med_name,details,date,full;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_approved_med_request);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");
        rq= Volley.newRequestQueue(this);

        lv=(ListView) findViewById(R.id.lv);

        mname=new ArrayList<>();
        mrid=new ArrayList<>();
        hname=new ArrayList<>();
        hno=new ArrayList<>();
        address=new ArrayList<>();
        med_name=new ArrayList<>();
        details=new ArrayList<>();
        date=new ArrayList<>();
        full=new ArrayList<>();

        ViewApprovedMedicineRequest();

        lv.setOnItemClickListener(this);
    }

    private void ViewApprovedMedicineRequest(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "ViewApprovedMedicineRequest", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job=new JSONObject(response);
                    String status=job.getString("status");
                    if (status.equalsIgnoreCase("success")){

                        JSONArray jar=job.getJSONArray("results");
                        for(int i=0;i<jar.length();i++) {
                            JSONObject jo = jar.getJSONObject(i);
                            mrid.add(jo.getString("request_id"));
                            mname.add(jo.getString("member_name"));
                            hname.add(jo.getString("house_name"));
                            hno.add(jo.getString("house_no"));
                            address.add(jo.getString("address"));
                            med_name.add(jo.getString("medicine_name"));
                            details.add(jo.getString("description"));
                            date.add(jo.getString("date"));
                            full.add("House : "+jo.getString("house_name")+"\nMember : "+jo.getString("member_name"));

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        pos=i;
        Intent ii=new Intent(getApplicationContext(),MedicineRequestDetails.class);
        startActivity(ii);
    }
}