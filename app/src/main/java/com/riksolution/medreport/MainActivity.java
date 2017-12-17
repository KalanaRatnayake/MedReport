package com.riksolution.medreport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //private Button btnDocLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent in = new Intent(getApplicationContext(),DoctorLoginActivity.class);
        startActivity(in);
        //Button btnDocLogin = (Button)findViewById(R.id.btnDocLogin);
        //btnDocLogin.setOnClickListener(btnDocLogin);

    }



}
