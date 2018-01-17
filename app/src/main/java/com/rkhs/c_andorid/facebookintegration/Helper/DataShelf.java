package com.rkhs.c_andorid.facebookintegration.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.rkhs.c_andorid.facebookintegration.Pojo.LoginDetails;
import com.rkhs.c_andorid.facebookintegration.Pojo.UserDetails;

/**
 * Created by Admin on 16-01-2018.
 */

public class DataShelf extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_db";
    private static final int DATABASE_VERSION = 1;


    //variable for first table...
    private static final String TABLE_LOGIN = "login_table";
    private static final String SR_NO = "sr_no";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    //variable for second table...
    private static final String TABLE_LOGIN_DETAILS = "login_table";
    private static final String SR_NOS = "sr_nos";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String MAILID = "mail_id";
    private static final String GENDER = "gender";

    public DataShelf(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" CREATE TABLE " + TABLE_LOGIN + " (" +
                SR_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERNAME + " TEXT NOT NULL, " +
                PASSWORD + " TEXT NOT NULL);");

        db.execSQL(" CREATE TABLE " + TABLE_LOGIN_DETAILS + " (" +
                SR_NOS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIRSTNAME + " TEXT NOT NULL, " +
                LASTNAME + " TEXT NOT NULL, " +
                MAILID + " TEXT NOT NULL, " +
                GENDER + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN_DETAILS);
        onCreate(db);
    }

    //METHODS TO EXECUTE DATABASE FUNCTION...
    public boolean insertIntoLogin(LoginDetails loginDetails) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USERNAME,loginDetails.getUsername());
        cv.put(PASSWORD,loginDetails.getPassword());

        long res = sqLiteDatabase.insert(TABLE_LOGIN,null,cv);
        if (res >= 1){return true;}
        else {return false;}
    }

    public Cursor selectFromLogin(String username) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("select * from "+TABLE_LOGIN+" where "+USERNAME+"= ?",new String[]{username});
        return c;
    }

    //METHODS FOR THE SECOND TABLE...
    public boolean insertIntoLoginDetails(UserDetails userDetails) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIRSTNAME,userDetails.getkFname());
        cv.put(LASTNAME,userDetails.getkLname());
        cv.put(MAILID,userDetails.getkEmailId());
        cv.put(GENDER,userDetails.getkGender());

        long res = sqLiteDatabase.insert(TABLE_LOGIN_DETAILS,null,cv);
        if (res >= 1){return true;}
        else {return false;}
    }

    public Cursor selectFromLoginDetails(String mailId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("select * from "+TABLE_LOGIN_DETAILS+" where "+MAILID+"= ?",new String[]{mailId});
        return c;
    }

}
