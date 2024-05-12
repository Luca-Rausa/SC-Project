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
import android.util.Pair;

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
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_EVENTS = "events";
    private static final String COLUMN_EVENT_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_ATTENDEES = "attendees";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_LINK_NAMES = "link_names";
    private static final String COLUMN_LINK_URL = "link_url";
    private static final String COLUMN_CREATOR_USERNAME = "creator_username";

    private static final String TABLE_IMAGES = "images";
    private static final String COLUMN_IMAGE_ID = "_id";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_EVENT = "event_id";

    public EventDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "(" +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_TYPE + " TEXT," +
                COLUMN_ATTENDEES + " INTEGER," +
                COLUMN_DATE + " DATE," +
                COLUMN_DURATION + " INTEGER," +
                COLUMN_LINK_NAMES + " TEXT," +
                COLUMN_LINK_URL + " TEXT," +
                COLUMN_CREATOR_USERNAME + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(CREATE_EVENTS_TABLE);

        String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_IMAGES + "(" +
                COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY," +
                COLUMN_EVENT + " INTEGER," +
                COLUMN_IMAGE + " BLOB," +
                "FOREIGN KEY(" + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS + "(" + COLUMN_EVENT_ID + ")"+
                ")";

        sqLiteDatabase.execSQL(CREATE_IMAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
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
        Pair<String,String> linksList = fromPairListToString(event.getLinks());
        if(linksList != null) {
            values.put(COLUMN_LINK_NAMES, linksList.first);
            values.put(COLUMN_LINK_URL, linksList.second);
        }
        values.put(COLUMN_CREATOR_USERNAME, event.getCreatorUsername());

        long eventId = db.insert(TABLE_EVENTS, null, values);

        for(Bitmap image: event.getImages()) {
            ContentValues imageValues = new ContentValues();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 0, stream);
            imageValues.put(COLUMN_EVENT, eventId);
            imageValues.put(COLUMN_IMAGE, stream.toByteArray());
            db.insert(TABLE_IMAGES, null, imageValues);
        }
        db.close();
    }

    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENTS, null);
        if(cursor.moveToFirst()) {
            do {
                long eventId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_EVENT_ID));
                Event event = getEvent(eventId);
                if (event != null) {
                    eventList.add(event);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return eventList;
    }

    public Event getEvent(long eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Event event = new Event();

        Cursor cursor = db.query(TABLE_EVENTS, null, COLUMN_EVENT_ID + "=?",
                new String[]{String.valueOf(eventId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            event.setId(eventId);
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
            int linkNameIndex = cursor.getColumnIndexOrThrow(COLUMN_LINK_NAMES);
            int linkUrlIndex = cursor.getColumnIndexOrThrow(COLUMN_LINK_URL);
            if(!cursor.isNull(linkNameIndex) && !cursor.isNull(linkUrlIndex)) {
                String linkNames = cursor.getString(linkNameIndex);
                String linkUrls = cursor.getString(linkUrlIndex);
                event.setLinks(fromStringToPairList(linkNames, linkUrls));
            }
            List<Bitmap> images = getImagesForEvent(db, eventId);
            event.setImages(images);
        }

        db.close();
        return event;
    }

    public boolean removeEvent(Event event) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int result = sqLiteDatabase.delete(TABLE_EVENTS,COLUMN_EVENT_ID+"=?", new String[]{String.valueOf(event.getId())});
        sqLiteDatabase.delete(TABLE_IMAGES, COLUMN_EVENT + " = ?", new String[]{String.valueOf(event.getId())});
        sqLiteDatabase.close();
        return result>0;
    }

    private List<Bitmap> getImagesForEvent(SQLiteDatabase database, long eventId) {
        List<Bitmap> images = new ArrayList<>();
        Cursor cursor = database.query(TABLE_IMAGES, new String[]{COLUMN_IMAGE}, COLUMN_EVENT + " = ?",
                new String[]{String.valueOf(eventId)}, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                byte[] imageData = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                Bitmap image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                images.add(image);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return images;
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

    private List<Pair<String, String>> fromStringToPairList(String names, String urls) {
        List<Pair<String, String>> linkList = new ArrayList<>();
        if (names != null && urls != null) {
            String[] nameArray = names.split(";");
            String[] urlArray = urls.split(";");
            for (int i = 0; i < Math.min(nameArray.length, urlArray.length); i++) {
                linkList.add(new Pair<>(nameArray[i], urlArray[i]));
            }
        }
        return linkList;
    }
}
