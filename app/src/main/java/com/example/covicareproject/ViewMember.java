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

public class ViewMember extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lv;
    public static int pos;


    SharedPreferences sh;
    RequestQueue rq;
    String lid="",houseid="";

    public static ArrayList<String> memerid,mname,age,photo,covid,gender,adarno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_member);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");

        houseid=ViewHouse.houseid.get(ViewHouse.pos);

        rq= Volley.newRequestQueue(this);

        lv=(ListView) findViewById(R.id.lv);

        lv.setOnItemClickListener(this);

        memerid=new ArrayList<>();
        mname=new ArrayList<>();
        age=new ArrayList<>();
        photo=new ArrayList<>();
        gender=new ArrayList<>();
        covid=new ArrayList<>();
        adarno=new ArrayList<>();

        ViewMembers();

    }

    private void ViewMembers(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "ViewMembers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job=new JSONObject(response);
                    String status=job.getString("status");
                    if (status.equalsIgnoreCase("success")){

                        JSONArray jar=job.getJSONArray("results");
                        for(int i=0;i<jar.length();i++)
                        {
                            JSONObject jo=jar.getJSONObject(i);
                            memerid.add(jo.getString("member_login_id"));
                            adarno.add(jo.getString("aadhar_no"));
                            mname.add(jo.getString("member_name"));
                            age.add(jo.getString("age"));
                            photo.add(jo.getString("photo"));
                            covid.add(jo.getString("covid_positive"));
                            gender.add(jo.getString("gender"));
                        }

                        ArrayAdapter<String> ad=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,mname);
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
                p.put("houseid",houseid);
                return p;


            }
        };
        rq.add(ja);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        pos=i;
        Intent ii=new Intent(getApplicationContext(),MemberDetails.class);
        startActivity(ii);
    }
}