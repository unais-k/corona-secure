package com.example.covicareproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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


public class ViewEmergencyOptions extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lv;

    SharedPreferences sh;
    RequestQueue rq;

    String lid="",utype="",emid="";

    public static int pos;

    public static ArrayList<String> emergency_options,emergency_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_emergency_options);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");
        utype=sh.getString("utype","");
        rq= Volley.newRequestQueue(this);


        lv=(ListView) findViewById(R.id.lv);

        if(utype.equalsIgnoreCase("member")) {
            lv.setOnItemClickListener(this);
        }

        emergency_id=new ArrayList<>();
        emergency_options=new ArrayList<>();

        ViewEmergencyOptions();
    }

    private void ViewEmergencyOptions(){

        StringRequest ja=new StringRequest(Request.Method.GET, sh.getString("url", "") + "ViewEmergencyOptions", new Response.Listener<String>() {
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
                            emergency_id.add(jo.getString("emergency_id"));
                            emergency_options.add(jo.getString("emergency_option"));

                        }

                        ArrayAdapter<String> ad=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,emergency_options);
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

                return p;


            }
        };
        rq.add(ja);
    }


    private void SendEmergencyOptionRequest(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "SendEmergencyOptionRequest", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job=new JSONObject(response);
                    String status=job.getString("status");
                    Toast.makeText(getApplicationContext(), job.toString(), Toast.LENGTH_SHORT).show();
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
                p.put("emid",emid);
                return p;


            }
        };
        rq.add(ja);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        pos=i;
        emid=emergency_id.get(i);
        final CharSequence[] options = { "Yes", "No", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewEmergencyOptions.this);
        builder.setTitle("Do you want to send Request ?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Yes"))
                {
                    SendEmergencyOptionRequest();
                }
                else if (options[item].equals("No"))
                {
                    dialog.dismiss();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }


}