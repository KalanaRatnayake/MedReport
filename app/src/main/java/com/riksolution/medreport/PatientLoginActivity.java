package com.riksolution.medreport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import helper.SQLiteHandler;
import helper.SessionManager;

public class PatientLoginActivity extends AppCompatActivity {
    EditText userName, passWord;
    String username, password;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private static final String TAG = PatientRegistrationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }

    public void checkLogin(View view){
        userName = (EditText) findViewById(R.id.etUsername);
        passWord = (EditText) findViewById(R.id.etPassword);

        username = userName.getText().toString();
        password = passWord.getText().toString();

        sendCred(username,md5(password));
    }

    public void sendCred(final String username, final String password){
        pDialog.setMessage("Logging in ...");
        showDialog();

        boolean recordExists = db.validPatient(username, password);
        if(recordExists == true){
            //user can be successfully logged in
            session.setLogin(true);
            Intent intentSignIn = new Intent(getApplicationContext(), PatientHomepageActivity.class);
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
        Intent intentRegistration = new Intent(getApplicationContext(), PatientRegistrationActivity.class);
        startActivity(intentRegistration);
    }

    public static final String md5(final String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e2){
        }
        return null;
    }
}
