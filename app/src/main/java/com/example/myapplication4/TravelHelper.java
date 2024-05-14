package com.example.myapplication4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TravelHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "travel_itinerary.db";
    private static final int DATABASE_VERSION = 2; // Increment version when changing schema

    // Define table and column names
    public static final String TABLE_NAME = "itinerary";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRST_NAME = "firstName";
    public static final String COLUMN_LAST_NAME = "lastName";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_START_DATE = "startDate";
    public static final String COLUMN_END_DATE = "endDate";
    public static final String COLUMN_DESCRIPTION = "itineraryDesc";
    public static final String COLUMN_TRAVEL_GROUP = "travelGroup";
    public static final String COLUMN_PROGRAM_OF_STUDY = "groupTravel"; // New column for program of study

    public TravelHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Define SQL statement to create the table with the new column
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_FIRST_NAME + " TEXT, " +
                        COLUMN_LAST_NAME + " TEXT, " +
                        COLUMN_EMAIL + " TEXT, " +
                        COLUMN_START_DATE + " TEXT, " +
                        COLUMN_END_DATE + " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_TRAVEL_GROUP + " TEXT, " +
                        COLUMN_PROGRAM_OF_STUDY + " TEXT)";

        // Execute the SQL statement to create the table
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
