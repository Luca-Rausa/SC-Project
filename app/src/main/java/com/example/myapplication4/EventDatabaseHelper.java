package com.example.myapplication4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Pair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "eventDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_EVENTS = "events";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_ATTENDEES = "attendees";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_IMAGE_PATHS = "image_paths";
    private static final String COLUMN_LINK_NAMES = "link_names";
    private static final String COLUMN_LINK_URL = "link_url";
    private static final String COLUMN_CREATOR_USERNAME = "creator_username";

    public EventDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_TYPE + " TEXT," +
                COLUMN_ATTENDEES + " INTEGER," +
                COLUMN_DATE + " DATE," +
                COLUMN_DURATION + " INTEGER," +
                COLUMN_IMAGE_PATHS + " TEXT," +
                COLUMN_LINK_NAMES + " TEXT," +
                COLUMN_LINK_URL + " TEXT," +
                COLUMN_CREATOR_USERNAME + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(sqLiteDatabase);
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, event.getTitle());
        values.put(COLUMN_DESCRIPTION, event.getDescription());
        values.put(COLUMN_TYPE, event.getType().getStringValue());
        values.put(COLUMN_ATTENDEES, event.getAttendees());
        values.put(COLUMN_DATE, event.getDate().toString());
        values.put(COLUMN_DURATION, event.getDuration());
        String imageList = fromListToString(event.getImage_paths());
        if(imageList != null) {
            values.put(COLUMN_IMAGE_PATHS, imageList);
        }
        Pair<String,String> linksList = fromPairListToString(event.getLinks());
        if(linksList != null) {
            values.put(COLUMN_LINK_NAMES, linksList.first);
            values.put(COLUMN_LINK_URL, linksList.second);
        }
        values.put(COLUMN_CREATOR_USERNAME, event.getCreatorUsername());

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public List<Event> getAllEvents() {
        SQLiteDatabase db = getReadableDatabase();
        List<Event> eventList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_EVENTS;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                event.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                event.setType(EventType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))));
                event.setAttendees(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ATTENDEES)));
                event.setDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE))));
                event.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                event.setImage_paths(fromStringToList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATHS))));
                event.setLinks(fromStringToPairList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK_NAMES)),cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK_NAMES))));
                event.setCreatorUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATOR_USERNAME)));
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventList;
    }

    private String fromListToString(List<Uri> mList) {
        StringBuilder finalString = new StringBuilder();
        if(mList != null) {
            for (Uri mValue : mList) {
                finalString.append(mValue.toString()).append(";");
            }
            return finalString.substring(0, finalString.length() - 1);
        } else {
            return null;
        }
    }

    private List<Uri> fromStringToList(String images) {
        if(images != null && !images.isEmpty()) {
            List<Uri> imageList = new ArrayList<>();
            String[] arrPaths = images.split(";");
            for(String image : arrPaths)
                imageList.add(Uri.parse(image));
            return  imageList;
        }
        return null;
    }

    private Pair<String,String> fromPairListToString(List<Pair<String,String>> mList) {
        StringBuilder names = new StringBuilder();
        StringBuilder url = new StringBuilder();
        if(mList != null) {
            for (Pair<String, String> mPair : mList) {
                names.append(mPair.first).append(";");
                url.append(mPair.second).append(";");
            }
            String strNames = names.substring(0, names.length() - 1);
            String strUrl = url.substring(0, url.length() - 1);
            return new Pair<>(strNames, strUrl);
        } else {
            return null;
        }
    }

    private static List<Pair<String, String>> fromStringToPairList(String strNames, String strUrl) {
        List<Pair<String, String>> pairList = new ArrayList<>();
        if (strNames != null && strUrl != null && !strNames.isEmpty() && !strUrl.isEmpty()) {
            String[] namesArray = strNames.split(";");
            String[] urlArray = strUrl.split(";");
            int minLength = Math.min(namesArray.length, urlArray.length);
            for (int i = 0; i < minLength; i++) {
                pairList.add(new Pair<>(namesArray[i], urlArray[i]));
            }
        }
        return pairList;
    }

    public void deleteExpiredEvents() {
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        db.delete("events", "date <= ?", new String[]{currentDate});

        db.close();
    }

}
