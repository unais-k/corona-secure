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

public class ViewVolunteers extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lv;

    SharedPreferences sh;
    RequestQueue rq;
    String lid="";

    public static int pos;

    public static ArrayList<String> volid,volname,Addres,Age,Phone,Photo,Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_volunteers);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");
        rq= Volley.newRequestQueue(this);

        lv=(ListView) findViewById(R.id.lv);

        lv.setOnItemClickListener(this);



        volid=new ArrayList<>();
        volname=new ArrayList<>();
        Addres=new ArrayList<>();
        Age=new ArrayList<>();
        Phone=new ArrayList<>();
        Photo=new ArrayList<>();
        Email=new ArrayList<>();

        ViewVolunteers();
    }

    private void ViewVolunteers(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "ViewVolunteers", new Response.Listener<String>() {
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
                            volid.add(jo.getString("volunteer_id"));
                            volname.add(jo.getString("user_name"));
                            Addres.add(jo.getString("address"));
                            Age.add(jo.getString("age"));
                            Phone.add(jo.getString("phone"));
                            Photo.add(jo.getString("photo"));
                            Email.add(jo.getString("email"));

                        }

                        ArrayAdapter<String> ad=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,volname);
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
        Intent ii= new Intent(getApplicationContext(),ViewVolunteerAndCall.class);
        startActivity(ii);
    }
}
