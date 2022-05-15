package com.example.covicareproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MemberDetails extends AppCompatActivity {
    TextView t1, t2, t3, t4, t5, t6, t7, t8, t9;
    ImageView Iv1;

    SharedPreferences sh;
    RequestQueue rq;
    String lid = "", utype = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid = sh.getString("lid", "");
        rq = Volley.newRequestQueue(this);


        t1 = (TextView) findViewById(R.id.textView1);
        t2 = (TextView) findViewById(R.id.textView2);
        t3 = (TextView) findViewById(R.id.textView3);
        t4 = (TextView) findViewById(R.id.textView4);
        t5 = (TextView) findViewById(R.id.textView5);
        t6 = (TextView) findViewById(R.id.textView6);
        t7 = (TextView) findViewById(R.id.textView7);
        t8 = (TextView) findViewById(R.id.textView8);
        t9 = (TextView) findViewById(R.id.textView9);
        Iv1 = (ImageView) findViewById(R.id.imageView1);

        t1.setText(ViewMember.mname.get(ViewMember.pos));
        t2.setText(ViewMember.age.get(ViewMember.pos));
        t3.setText(ViewMember.covid.get(ViewMember.pos));
        t4.setText(ViewMember.adarno.get(ViewMember.pos));
        t5.setText(ViewMember.gender.get(ViewMember.pos));

        String ip=sh.getString("ip","");
        Picasso.with(getApplicationContext())
                .load("http://"+ip+":5000/static/MemberPics/"+ViewMember.photo.get(ViewMember.pos)).transform(new CircleTransform())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background).into(Iv1);

        MemberHealthStatus();

    }
    private void MemberHealthStatus(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "MemberHealthStatus", new Response.Listener<String>() {
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
                            t6.setText(jo.getString("temperature"));
                            t7.setText(jo.getString("heartbeat"));
                            t8.setText(jo.getString("cough"));
                            t9.setText(jo.getString("status"));
                        }

                       // t6.setText(job.getString("temperature"));
                        Toast.makeText(getApplicationContext(), job.getString("temperature"), Toast.LENGTH_SHORT).show();


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(), "Error..1..."+e.toString(), Toast.LENGTH_SHORT).show();
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
                p.put("mlid",ViewMember.memerid.get(ViewMember.pos));
                return p;


            }
        };
        rq.add(ja);
    }

}