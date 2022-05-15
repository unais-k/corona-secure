package com.example.covicareproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewAshaworkerAndCall extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5,t6,t7;
    ImageView Iv1;
    Button B1;

    SharedPreferences sh;
    RequestQueue rq;
    String lid="",utype="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ashaworker_and_call);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");
        utype=sh.getString("utype","");
        rq= Volley.newRequestQueue(this);

        t1=(TextView) findViewById(R.id.textView1);
        t2=(TextView) findViewById(R.id.textView2);
        t3=(TextView) findViewById(R.id.textView3);
        t4=(TextView) findViewById(R.id.textView4);
        t5=(TextView) findViewById(R.id.textView5);
        t6=(TextView) findViewById(R.id.textView6);
        t7=(TextView) findViewById(R.id.textView7);
        Iv1=(ImageView) findViewById(R.id.imageView1);
        B1=(Button) findViewById(R.id.button1);

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+t3.getText().toString()));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });

        ViewAshaWorkerProfile();
    }


    private void ViewAshaWorkerProfile(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "ViewAshaWorkerProfile", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job=new JSONObject(response);
                    String status=job.getString("status");
                    if (status.equalsIgnoreCase("success")){
                        t1.setText(job.getString("name"));
                        t2.setText(job.getString("address"));
                        t3.setText(job.getString("phone"));
                        t4.setText(job.getString("pin"));
                        t5.setText(job.getString("email"));
                        t6.setText(job.getString("gender"));
                        t7.setText(job.getString("aadhar_no"));


                        String ip=sh.getString("ip","");
                        Picasso.with(getApplicationContext())
                                .load("http://"+ip+":5000/static/ashaworkerPics/"+job.getString("photo")).transform(new CircleTransform())
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_background).into(Iv1);

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
                p.put("utype",utype);
                return p;


            }
        };
        rq.add(ja);
    }

}