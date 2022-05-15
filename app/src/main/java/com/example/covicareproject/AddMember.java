package com.example.covicareproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddMember extends AppCompatActivity {
    EditText ed1,ed2,ed3,ed4;
    Button B1,b2;
    RadioButton r1,r2;
    ImageView im;

    byte[] photoBytes = null;
    private final int RESPONSE_OK = 200;
    private static final int CAMERA_REQUEST = 1888, GALLERY_CODE = 201;
    private Uri mImageCaptureUri;
    private File outPutFile = null;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    static String photo, fileName,path,attach,url="";

    SharedPreferences sh;
    RequestQueue rq;
    String lid="",houseid="",name="",age="",covid="",adarno="",gender="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        ed1=(EditText) findViewById(R.id.edittext1);
        ed2=(EditText) findViewById(R.id.edittext2);
        ed3=(EditText) findViewById(R.id.edittext3);
        ed4=(EditText) findViewById(R.id.edittext4);
        r1=(RadioButton) findViewById(R.id.radioButton1);
        r2=(RadioButton) findViewById(R.id.radioButton2);
        b2=(Button)findViewById(R.id.button);
        im=(ImageView)findViewById(R.id.imageView);
        B1=(Button) findViewById(R.id.button1);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");

        houseid=ViewHouse.houseid.get(ViewHouse.pos);

        rq= Volley.newRequestQueue(this);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageOption();
            }
        });


        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=ed1.getText().toString();
                age=ed2.getText().toString();
                covid=ed3.getText().toString();
                if(r1.isChecked())
                {
                    gender="male";
                }
                else {
                    gender ="female";
                }
                adarno=ed4.getText().toString();

                if (name.equalsIgnoreCase(""))
                {
                    ed1.setError("Invalid Name");
                    ed1.setFocusable(true);
                } else if (age.equalsIgnoreCase(""))
                {
                    ed2.setError("Invalid Age ");
                    ed2.setFocusable(true);

                } else if (covid.equalsIgnoreCase(""))
                {
                    ed3.setError("Invalid Covid Information");
                    ed3.setFocusable(true);
                }else if (adarno.equalsIgnoreCase(""))
                {
                    ed4.setError("Invalid Aadhar Number");
                    ed4.setFocusable(true);

                }else if (attach.equalsIgnoreCase(""))
                {
                    Toast.makeText(getApplicationContext(), "Select Image", Toast.LENGTH_SHORT).show();

                }

                else {
                    AddFamilyMemeber();
                }



            }
        });
    }

    private void AddFamilyMemeber(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "AddFamilyMemeber", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job=new JSONObject(response);
                    String status=job.getString("status");
                    if (status.equalsIgnoreCase("success")){
                        Toast.makeText(getApplicationContext(), "Successfully Registerd", Toast.LENGTH_LONG).show();
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
                p.put("name",name);
                p.put("age",age);
                p.put("gender",gender);
                p.put("covid",covid);
                p.put("photo",attach);
                p.put("adarno",adarno);
                return p;


            }
        };
        rq.add(ja);
    }

    private void selectImageOption()
    {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddMember.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Capture Photo")) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, GALLERY_CODE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {

            mImageCaptureUri = data.getData();
            Uri uri = data.getData();
            try
            {
                photo = FileUtils.getPath(this, uri);
                path=getRealPathFromURI(uri);

                Bitmap photo1 = decodeFile(new File(path));
                im.setImageBitmap(photo1);
                File fl=new File(photo);
                int ln=(int) fl.length();
                InputStream inputStream = new FileInputStream(fl);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[ln];
                int bytesRead =0;

                while ((bytesRead = inputStream.read(b)) != -1)
                {
                    bos.write(b, 0, bytesRead);
                }
                inputStream.close();
                photoBytes = bos.toByteArray();

                String str = Base64.encodeToString(photoBytes, Base64.NO_WRAP);
                attach = str;
            }
            catch(Exception e){
                Toast.makeText(getApplicationContext(), "2=========="+e.toString(), Toast.LENGTH_LONG).show();

            }
        }else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {


            try
            {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                im.setImageBitmap(photo);

                Uri tempUri = getImageUri(getApplicationContext(), photo);


                File fl=new File(getRealPathFromURI(tempUri));
                int ln=(int) fl.length();
                InputStream inputStream = new FileInputStream(fl);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[ln];
                int bytesRead =0;

                while ((bytesRead = inputStream.read(b)) != -1)
                {
                    bos.write(b, 0, bytesRead);
                }
                inputStream.close();
                photoBytes = bos.toByteArray();

                String str = Base64.encodeToString(photoBytes, Base64.NO_WRAP);
                attach = str;
            }
            catch(Exception e){
                Toast.makeText(getApplicationContext(), "1========="+e.toString(), Toast.LENGTH_LONG).show();

            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri contentURI) {

        Cursor cursor = getContentResolver()
                .query(contentURI, null, null, null, null);
        if (cursor == null)
            path=contentURI.getPath();

        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path=cursor.getString(idx);

        }
        if(cursor!=null)
            cursor.close();
        return path;
    }
    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 512;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "3========"+e.toString(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


}