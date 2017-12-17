package com.riksolution.medreport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import helper.SQLiteHandler;
import helper.SessionManager;


public class DoctorRegistrationActivity extends AppCompatActivity {

    private EditText firstname;
    private EditText lastname;
    private EditText regNumber;
    private EditText contactNo;
    private EditText hospital;
    private EditText nic;
    private EditText checkPassWord;

    private EditText userName;
    private EditText passWord;

    private String username;
    private String password;
    private String checkPassword;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler dbhandler;

    private RequestQueue requestQueue;
    private StringRequest strReq;
    //private static final String TAG = DoctorRegistrationActivity.class.getSimpleName();
    private static final String URL = "http://10.10.26.56/MedReport/doctorRegister.php";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);


        userName = (EditText)findViewById(R.id.etDocUsername);
        passWord = (EditText)findViewById(R.id.etDocPassword);
        checkPassWord = (EditText)findViewById(R.id.etPasswordReenter);

        firstname = (EditText)findViewById(R.id.etDocFirstName);
        lastname = (EditText)findViewById(R.id.etDocLastName);
        nic = (EditText)findViewById(R.id.etNIC);
        regNumber = (EditText)findViewById(R.id.etDocRegNo);
        contactNo = (EditText)findViewById(R.id.etContactNo);
        hospital = (EditText)findViewById(R.id.etHospital);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler

        db = new SQLiteHandler(getApplicationContext());


        requestQueue = Volley.newRequestQueue(this);
    }

    public void sendPost(View view) {

        username = userName.getText().toString().trim();
        password = passWord.getText().toString().trim();
        checkPassword = passWord.getText().toString().trim();

        if (!username.isEmpty() && !password.isEmpty()  && password.equals(checkPassword) ) {
            //registerUser(username, password);
            strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean error = jsonObject.getBoolean("error");
                        if(!error){
                            //usr successfully registered
                            Toast.makeText(getApplicationContext(),"SUCCESS "+jsonObject.getString("success"),Toast.LENGTH_SHORT).show();

                            String uid = jsonObject.getString("uid");
                            JSONObject user = jsonObject.getJSONObject("user");
                            String firstName = user.getString("firstName");
                            String lastName = user.getString("lastName");
                            String regNo = user.getString("regNo");
                            String nicNo = user.getString("nicNo");
                            String contactNo = user.getString("contactNo");
                            String Username = user.getString("Username");
                            String Encrypted_password = user.getString("Encrypted_password");
                            String Hospital = user.getString("Hospital");

                            db.addDoctor(uid,firstName,lastName,regNo,nicNo,Hospital,contactNo,Username,Encrypted_password );

                            startActivity(new Intent(getApplicationContext(),DoctorLoginActivity.class));
                        }else {
                            Toast.makeText(getApplicationContext(), "Error : " +jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
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
                    hashMap.put("firstName",firstname.getText().toString().trim());
                    hashMap.put("lastName",lastname.getText().toString().trim());
                    hashMap.put("regNo",regNumber.getText().toString().trim());
                    hashMap.put("contactNo",contactNo.getText().toString().trim());
                    hashMap.put("hospital",hospital.getText().toString().trim());
                    hashMap.put("nicNo",nic.getText().toString().trim());

                    return hashMap;
                }
            };

            requestQueue.add(this.strReq);

            }

         else {
            Toast.makeText(getApplicationContext(),
                    "Please enter your details!", Toast.LENGTH_LONG).show();
        }
    }








}

