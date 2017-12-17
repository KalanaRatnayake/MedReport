package com.riksolution.medreport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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



public class DocInputActivity extends AppCompatActivity {

    private EditText patientName;
    private EditText patientNIC;
    private EditText age;
    private EditText area;
    private EditText date;
    private EditText diagnosis;
    private EditText disease;
    private EditText treatment;
    private CheckBox minor;
    private CheckBox medium;
    private CheckBox risky;
    private CheckBox epidemic;

    private String patientname;
    private String patientnic;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    private RequestQueue requestQueue;
    private StringRequest strReq;
    //private static final String TAG = DoctorRegistrationActivity.class.getSimpleName();
    private static final String URL = "http://10.10.26.56/MedReport/medrecordAd.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_input);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        requestQueue = Volley.newRequestQueue(this);

        patientName = (EditText)findViewById(R.id.patientName);
        patientNIC = (EditText)findViewById(R.id.patientNIC);

        age = (EditText)findViewById(R.id.patientAge);
        area = (EditText)findViewById(R.id.patientArea);
        date = (EditText)findViewById(R.id.date);
        diagnosis = (EditText)findViewById(R.id.diagnosis);
        disease = (EditText)findViewById(R.id.disease);
        treatment = (EditText)findViewById(R.id.treatment);




    }


    public void sendDiagnosis(View view) {
        patientname = patientName.getText().toString().trim();
        patientnic = patientNIC.getText().toString().trim();

        if (!patientname.isEmpty() && !patientnic.isEmpty()) {

            strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean error = jsonObject.getBoolean("error");
                        if (!error) {
                            //usr successfully registered
                            Toast.makeText(getApplicationContext(), "SUCCESS " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(), DoctorHomepageActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Error : " + jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("name", patientName.getText().toString().trim());
                    hashMap.put("nicNo", patientNIC.getText().toString().trim());
                    hashMap.put("docId", patientNIC.getText().toString().trim());
                    hashMap.put("place", area.getText().toString().trim());
                    hashMap.put("cTime", date.getText().toString().trim());
                    hashMap.put("age", age.getText().toString().trim());
                    hashMap.put("diagnosis", diagnosis.getText().toString().trim());
                    hashMap.put("disease", disease.getText().toString().trim());
                    hashMap.put("treatment", treatment.getText().toString().trim());
                    //hashMap.put("type", type.getText().toString().trim());




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
