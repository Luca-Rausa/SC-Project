package com.example.myapplication4;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
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
    private static final String COLUMN_IMAGES = "images";
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
                COLUMN_IMAGES + " BLOB," +
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
        values.put(COLUMN_TYPE, event.getType().toString());
        values.put(COLUMN_ATTENDEES, event.getAttendees());
        values.put(COLUMN_DATE, event.getDate().toString());
        values.put(COLUMN_DURATION, event.getDuration());
        values.put(COLUMN_IMAGES, serializeImages(event.getImages()));
        CustomPair<String,String> linksList = fromPairListToString(event.getLinks());
        if(linksList != null) {
            values.put(COLUMN_LINK_NAMES, linksList.first);
            values.put(COLUMN_LINK_URL, linksList.second);
        }
        values.put(COLUMN_CREATOR_USERNAME, event.getCreatorUsername());

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENTS, null);
        if(cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                event.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                event.setType(EventType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))));
                event.setAttendees(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ATTENDEES)));
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                DateFormat outputDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
                try {
                    String strDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                    Date date = inputDateFormat.parse(strDate);
                    String formattedDate = outputDateFormat.format(date);
                    event.setDate(outputDateFormat.parse(formattedDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                event.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                event.setCreatorUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATOR_USERNAME)));
                int imageIndex = cursor.getColumnIndexOrThrow(COLUMN_IMAGES);
                if(!cursor.isNull(imageIndex)) {
                    byte[] imageData = cursor.getBlob(imageIndex);
                    event.setImages(deserializeImages(imageData));
                }
                int linkNameIndex = cursor.getColumnIndexOrThrow(COLUMN_LINK_NAMES);
                int linkUrlIndex = cursor.getColumnIndexOrThrow(COLUMN_LINK_URL);
                if(!cursor.isNull(linkNameIndex) && !cursor.isNull(linkUrlIndex)) {
                    String linkNames = cursor.getString(linkNameIndex);
                    String linkUrls = cursor.getString(linkUrlIndex);
                    event.setLinks(fromStringToPairList(linkNames, linkUrls));
                }
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return eventList;
    }

    private List<Bitmap> deserializeImages(byte[] imageData) {
        List<Bitmap> images = new ArrayList<>();
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(imageData));
            images = (List<Bitmap>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return images;
    }

    private byte[] serializeImages(List<Bitmap> images) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(images);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    private CustomPair<String,String> fromPairListToString(List<CustomPair<String,String>> mList) {
        StringBuilder names = new StringBuilder();
        StringBuilder url = new StringBuilder();
        if(mList != null) {
            for (CustomPair<String, String> mPair : mList) {
                names.append(mPair.first).append(";");
                url.append(mPair.second).append(";");
            }
            String strNames = names.substring(0, names.length() - 1);
            String strUrl = url.substring(0, url.length() - 1);
            return new CustomPair<>(strNames, strUrl);
        } else {
            return null;
        }
    }

    private List<CustomPair<String, String>> fromStringToPairList(String names, String urls) {
        List<CustomPair<String, String>> linkList = new ArrayList<>();
        if (names != null && urls != null) {
            String[] nameArray = names.split(";");
            String[] urlArray = urls.split(";");
            for (int i = 0; i < Math.min(nameArray.length, urlArray.length); i++) {
                linkList.add(new CustomPair<>(nameArray[i], urlArray[i]));
            }
        }
        return linkList;
    }
}
