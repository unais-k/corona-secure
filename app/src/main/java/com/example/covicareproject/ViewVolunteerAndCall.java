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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

public class ViewVolunteerAndCall extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5;
    ImageView Iv1;
    Button B1;

    SharedPreferences sh;
    RequestQueue rq;
    String lid="",utype="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_volunteer_and_call);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");
        rq= Volley.newRequestQueue(this);

        t1 = (TextView) findViewById(R.id.textView1);
        t2 = (TextView) findViewById(R.id.textView2);
        t3 = (TextView) findViewById(R.id.textView3);
        t4 = (TextView) findViewById(R.id.textView4);
        t5 = (TextView) findViewById(R.id.textView5);
        Iv1 = (ImageView) findViewById(R.id.imageView1);
        B1 = (Button) findViewById(R.id.button1);

        t1.setText(ViewVolunteers.volname.get(ViewVolunteers.pos));
        t2.setText(ViewVolunteers.Addres.get(ViewVolunteers.pos));
        t3.setText(ViewVolunteers.Phone.get(ViewVolunteers.pos));
        t4.setText(ViewVolunteers.Email.get(ViewVolunteers.pos));
        t5.setText(ViewVolunteers.Age.get(ViewVolunteers.pos));



        String ip=sh.getString("ip","");
        Picasso.with(getApplicationContext())
                .load("http://"+ip+":5000/static/volunteerPics/"+ViewVolunteers.Photo.get(ViewVolunteers.pos)).transform(new CircleTransform())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background).into(Iv1);



        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+ViewVolunteers.Phone.get(ViewVolunteers.pos)));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);

            }
        });
    }
}