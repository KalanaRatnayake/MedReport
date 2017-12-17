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

    private EditText fullname;
    private EditText regNumber;
    private EditText contactNo;
    private EditText hospital;
    private EditText nic;

    private EditText userName;
    private EditText passWord;

    private String username;
    private String password;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    private RequestQueue requestQueue;
    private StringRequest strReq;
    //private static final String TAG = DoctorRegistrationActivity.class.getSimpleName();
    private static final String URL = "http://10.10.26.56/edc/user_control.php";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);


        userName = (EditText)findViewById(R.id.etDocUsername);
        passWord = (EditText)findViewById(R.id.etDocPassword);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        //session = new SessionManager(getApplicationContext());

        // SQLite database handler
        //db = new SQLiteHandler(getApplicationContext());
        requestQueue = Volley.newRequestQueue(this);


    }

    public void sendPost(View view) {

        username = userName.getText().toString().trim();
        password = passWord.getText().toString().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            //registerUser(username, password);
            strReq = new StringRequest(Request.Method.POST,
                    URL, new Response.Listener<String>() {
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
                    hashMap.put("username",userName.getText().toString().trim());
                    hashMap.put("password",passWord.getText().toString().trim());

                    return hashMap;
                }
            };

            requestQueue.add(this.strReq);

            }

         else {
            Toast.makeText(getApplicationContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
        }
    }








}

