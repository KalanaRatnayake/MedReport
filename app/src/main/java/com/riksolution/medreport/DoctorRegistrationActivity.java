package com.riksolution.medreport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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


public class DoctorRegistrationActivity extends AppCompatActivity {

    private EditText username,password;
    private Button sign_in_register;
    private RequestQueue requestQueue;
    private static final String URL = "http://10.10.26.56/edc/user_control.php";
    private StringRequest request;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        username = (EditText) findViewById(R.id.etDocUsername);
        password = (EditText) findViewById(R.id.etDocPassword);
        sign_in_register = (Button) findViewById(R.id.btnDocRegister);

        requestQueue = Volley.newRequestQueue(this);

        sign_in_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.names().get(0).equals("success")){
                                Toast.makeText(getApplicationContext(),"SUCCESS "+jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }else {
                                Toast.makeText(getApplicationContext(), "Error" +jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> hashMap = new HashMap<String, String>();
                        hashMap.put("username",username.getText().toString());
                        hashMap.put("password",password.getText().toString());

                        return hashMap;
                    }
                };

                requestQueue.add(request);
            }
        });
    }
}


