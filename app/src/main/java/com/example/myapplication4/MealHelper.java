package com.example.myapplication4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MealHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "meal_planning.db";
    private static final int DATABASE_VERSION = 1;

    // Define table and column names
    public static final String TABLE_NAME = "meals";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRST_NAME = "firstName";
    public static final String COLUMN_LAST_NAME = "lastName";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_START_DATE = "startDate";
    public static final String COLUMN_END_DATE = "endDate";
    public static final String COLUMN_MEAL_TIMINGS = "mealTimings";
    public static final String COLUMN_PROGRAM_OF_STUDY = "programOfStudy";

    public MealHelper(Context context) {
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
                        COLUMN_MEAL_TIMINGS + " TEXT, " +
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
