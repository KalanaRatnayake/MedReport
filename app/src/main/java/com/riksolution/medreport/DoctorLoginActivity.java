package com.riksolution.medreport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
       pDialog.setMessage("Logging in ...");
       showDialog();

       boolean recordExists = db.validDoctor(username, password);
       if(recordExists == true){
           //user can be successfully logged in
           session.setLogin(true);
           Intent intentSignIn = new Intent(getApplicationContext(), DoctorHomepageActivity.class);
           Toast.makeText(getApplicationContext(), "Login successful, redirecting to Home Page.", Toast.LENGTH_LONG).show();
           startActivity(intentSignIn);
       } else {
           Toast.makeText(getApplicationContext(), "Invalid credentials, please try again.", Toast.LENGTH_LONG).show();
       }
   }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
   }

   public void register(View view){
       Intent intentRegistration = new Intent(getApplicationContext(), DoctorRegistrationActivity.class);
       startActivity(intentRegistration);
   }
}

