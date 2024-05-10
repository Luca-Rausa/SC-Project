package com.example.myapplication4;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
public class LoginHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserInfo.db";
    private static final String TABLE_NAME = "user";
    private static final String COL_ID = "id";
    private static final String COL_FIRSTNAME = "firstname";
    private static final String COL_LASTNAME = "lastname";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ROLE = "role";

    public LoginHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FIRSTNAME + " TEXT, " +
                COL_LASTNAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addUser(String firstname, String lastname, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FIRSTNAME, firstname);
        contentValues.put(COL_LASTNAME, lastname);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);
        contentValues.put(COL_ROLE, role);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public User getUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COL_EMAIL + " = ? AND " +
                COL_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        User user = null;

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex(COL_ID));
            String firstname = cursor.getString(cursor.getColumnIndex(COL_FIRSTNAME));
            String lastname = cursor.getString(cursor.getColumnIndex(COL_LASTNAME));
            String userEmail = cursor.getString(cursor.getColumnIndex(COL_EMAIL));
            String userPassword = cursor.getString(cursor.getColumnIndex(COL_PASSWORD));
            String role = cursor.getString(cursor.getColumnIndex(COL_ROLE));

            user = new User(userId, firstname, lastname, userEmail, userPassword, role);
        }

        cursor.close();
        return user;
    }
}
