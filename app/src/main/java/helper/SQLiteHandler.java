package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Isuru Samaranayake on 12/17/2017.
 * Updated by Kalana Ratnayake on 12/17/2017.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    public SQLiteHandler(Context context) {
        super(context, "MedReportLocal", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Doctor( unique_id varchar(23) primary key, firstName Varchar(50) Not Null, lastName Varchar(50) Not Null, regNo varChar(10) Not Null, nicNo varChar(12) Not Null, contactNo numeric Not null, username varchar(50) not null UNIQUE, doc_password varchar(80) not null, hospital varChar(50) not null )");
        sqLiteDatabase.execSQL("CREATE table patient( uniqueId varchar(23) primary key, firstName varchar(50) not null, lastName varchar(50) not null,  nicNo varchar(12) not null unique, houseNo varchar(50), city varchar(50)not null, district varchar(50) not null, contactNo numeric not null, dob datetime not null,gender varchar(1) not null, occupation varchar(50) not null, habits varchar(500) not null,allergies varchar(500) not null, pastMedHistory varchar(500) not null, username varchar(50) not null unique, encryptedPassword varchar(100) not null )");
        sqLiteDatabase.execSQL("CREATE table medrecord( uniqueId varChar(25) not null PRIMARY KEY, docId varchar(25) not null, name varchar(50) not null, nicNo varchar(12) not null, place varchar(50) not null, cTime varchar(15)not null, age int(5) not null, diagnosis varchar(500) not null, disease varchar(500)not null, treatment varchar(500) not null, type int(5) not null, FOREIGN key(docId) REFERENCES doctor(unique_id) )");

        Log.d(TAG, "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Doctor");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS patient");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS medrecord");

        // Create tables again
        onCreate(sqLiteDatabase);

    }

    /*Enter data locally*/
    public void addDoctor(String uniqueID, String FirstName, String LastName, String RegistrationNo, String NIC, String Hospital, String ContactNo, String UserName, String Password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("unique_id", uniqueID);
        values.put("firstName", FirstName);
        values.put("lastName", LastName);
        values.put("regNo", RegistrationNo);
        values.put("nicNo", NIC);
        values.put("hospital", Hospital);
        values.put("contactNo", ContactNo);
        values.put("username", UserName);
        values.put("doc_password", Password);

        // Inserting Row
        long id = db.insert("Doctor", null, values);
        db.close(); // Closing database connection

        Log.d(TAG, FirstName + LastName + "registered as a new Doctor");
    }

    public void addPatient(String uniqueId, String firstName, String lastName, String nicNo, String houseNo, String city, String district, Long contactNo, String dob, String gender, String occupation, String habits, String allergies, String pastMedHistory, String username, String encryptedPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("uniqueId", uniqueId);
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("nicNo", nicNo);
        values.put("houseNo", houseNo);
        values.put("city", city);
        values.put("district", district);
        values.put("contactNo", contactNo);
        values.put("dob", dob);
        values.put("gender", gender);
        values.put("occupation", occupation);
        values.put("habits", habits);
        values.put("allergies", allergies);
        values.put("pastMedHistory", pastMedHistory);
        values.put("username", username);
        values.put("encryptedPassword", encryptedPassword);

        // Inserting Row
        long id = db.insert("patient", null, values);
        db.close(); // Closing database connection

        Log.d(TAG, firstName + lastName + "registered as a new patient");
    }

    public void addRecord( String uniqueId, String docId, String name, String nicNo, String place, String cTime, int age, String diagnosis, String disease, String treatment, int type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("uniqueId", uniqueId);
        values.put("docId", docId);
        values.put("name", name);
        values.put("nicNo", nicNo);
        values.put("place", place);
        values.put("cTime", cTime);
        values.put("age", age);
        values.put("diagnosis", diagnosis);
        values.put("disease", disease);
        values.put("treatment", treatment);
        values.put("type", type);

        // Inserting Row
        long id = db.insert("medrecord", null, values);
        db.close(); // Closing database connection

        Log.d(TAG, nicNo + name + "registered as a new patient");
    }

    public boolean validDoctor(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectString = "SELECT * FROM Doctor WHERE username = ? AND doc_password = ?";

        Cursor cursor = db.rawQuery(selectString, new String[]{username, password});
        boolean exist;
        if(cursor.getCount()>0){
            exist=true;
        } else {
            exist=false;
        }
        db.close();
        cursor.close();

        return exist;
    }

    public boolean validPatient(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectString = "SELECT * FROM patient WHERE username = ? AND encryptedPassword = ?";

        Cursor cursor = db.rawQuery(selectString, new String[]{username, password});
        boolean exist;
        if(cursor.getCount()>0){
            exist=true;
        } else {
            exist=false;
        }
        db.close();
        cursor.close();

        return exist;
    }

    public HashMap<String, String> getMedReports(String NIC, String UserName) {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM medrecord where nicNo = ? AND name = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{NIC, UserName});

        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("docId", cursor.getString(1));
            user.put("name", cursor.getString(2));
            user.put("nicNo", cursor.getString(3));
            user.put("place", cursor.getString(4));
            user.put("cTime", cursor.getString(5));
            user.put("age", cursor.getString(6));
            user.put("diagnosis", cursor.getString(7));
            user.put("disease", cursor.getString(8));
            user.put("treatment", cursor.getString(9));
            user.put("type", cursor.getString(10));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteDoctor() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete("Doctor", null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from database");
    }

    public void deletePatient() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete("Patient", null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from database");
    }
}
