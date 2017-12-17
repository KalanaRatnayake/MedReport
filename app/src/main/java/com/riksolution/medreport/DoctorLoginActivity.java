package com.riksolution.medreport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;

public class DoctorLoginActivity extends AppCompatActivity {
    //TextView content;
    EditText userName, passWord;
    String username, password;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private static final String TAG = DoctorRegistrationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

    }

   public void checkLogin(View view){
       userName = (EditText) findViewById(R.id.etDocUsername);
       passWord = (EditText) findViewById(R.id.etDocPassword);

       username = userName.getText().toString();
       password = passWord.getText().toString();

       sendCred(username,password);

   }

   public void sendCred(final String username, final String password){
       // Tag used to cancel the request
       String tag_string_req = "req_login";

       pDialog.setMessage("Logging in ...");
       showDialog();

       StringRequest strReq = new StringRequest(Request.Method.POST,
               AppConfig.loginURL, new Response.Listener<String>() {

           @Override
           public void onResponse(String response) {
               //Log.d(TAG, "Login Response: " + response.toString());
               //hideDialog();
               pDialog.setMessage(response.toString());
               showDialog();

               try {
                   JSONObject jObj = new JSONObject(response);
                   boolean error = jObj.getBoolean("error");

                   // Check for error node in json
                   if (!error) {
                       // user successfully logged in
                       // Create login session
                       session.setLogin(true);

                       // Now store the user in SQLite
                       String uid = jObj.getString("uid");

                       JSONObject user = jObj.getJSONObject("user");
                       String name = user.getString("name");
                       String email = user.getString("email");
                       String created_at = user
                               .getString("created_at");

                       // Inserting row in users table
                       db.addUser(name, email, uid, created_at);

                       // Launch main activity
                       Intent intent = new Intent(DoctorLoginActivity.this,
                               MainActivity.class);
                       startActivity(intent);
                       finish();
                   } else {
                       // Error in login. Get the error message
                       String errorMsg = jObj.getString("error_msg");
                       Toast.makeText(getApplicationContext(),
                               errorMsg, Toast.LENGTH_LONG).show();
                   }
               } catch (JSONException e) {
                   // JSON error
                   e.printStackTrace();
                   Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
               }

           }
       }, new Response.ErrorListener() {

           @Override
           public void onErrorResponse(VolleyError error) {
               //Log.e(TAG, "Login Error: " + error.getMessage());
               Toast.makeText(getApplicationContext(),
                       error.getMessage(), Toast.LENGTH_LONG).show();
               //hideDialog();
           }
       }) {

           @Override
           protected Map<String, String> getParams() {
               // Posting parameters to login url
               Map<String, String> params = new HashMap<String, String>();
               params.put("email", username);
               params.put("password", password);

               return params;
           }

       };

       // Adding request to request queue
       AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
   }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
   }
}

