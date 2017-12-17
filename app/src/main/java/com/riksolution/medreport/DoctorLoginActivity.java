package com.riksolution.medreport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class DoctorLoginActivity extends AppCompatActivity {
    //TextView content;
    EditText userName, passWord;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        userName = (EditText) findViewById(R.id.etDocUsername);
        passWord = (EditText) findViewById(R.id.etDocPassword);

        Button btnDocLogin = (Button) findViewById(R.id.btnDoctor);

        btnDocLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                try {
                    getText();
                } catch (Exception e) {
                    //error message

                }
            }

        });}

    public void getText() throws UnsupportedEncodingException {
        username = userName.getText().toString();
        password = passWord.getText().toString();

        String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
        data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

        String text = "";
        BufferedReader reader = null;

        try {

            // Defined URL  where to send data
            URL url = new URL("10.10.26.56/edc/user_control.php");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");

            }


            text = sb.toString();
            userName.setText(text);
        } catch (Exception ex) {
            //connection error
            userName.setText("connection");
        } finally {
            try {

                reader.close();
            } catch (Exception ex) {//close error}
            }

            // Show response on activity
            //content.setText( text  );

        }

    }
}

