package com.riksolution.medreport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DoctorHomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_homepage);
    }

    public void launchInput(View view) {
        Intent i = new Intent(DoctorHomepageActivity.this,DocInputActivity.class);
        startActivity(i);
        finish();
    }
}
