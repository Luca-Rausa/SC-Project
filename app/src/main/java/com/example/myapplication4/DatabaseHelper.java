package com.example.myapplication4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SC.db";
    private static final int DATABASE_VERSION = 1;

    // Events
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

    // Images
    private static final String TABLE_IMAGES = "images";
    private static final String COLUMN_IMAGE_ID = "_id";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_EVENT = "event_id";

    // Attendees
    private static final String TABLE_ATTENDEES = "attendees";
    private static final String COLUMN_ATTENDEE_ID = "attendee_id";

    // UserInfo
    private static final String TABLE_USER = "user";
    private static final String COL_ID = "id";
    private static final String COL_FIRSTNAME = "firstname";
    private static final String COL_LASTNAME = "lastname";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ROLE = "role";

    public DatabaseHelper(Context context) {
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

        String createTable = "CREATE TABLE " + TABLE_USER + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FIRSTNAME + " TEXT, " +
                COL_LASTNAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT)";
        sqLiteDatabase.execSQL(createTable);

        String CREATE_ATTENDEES_TABLE = "CREATE TABLE " + TABLE_ATTENDEES + "(" +
                COLUMN_ATTENDEE_ID + " INTEGER," +
                COLUMN_EVENT + " INTEGER," +
                "FOREIGN KEY(" + COLUMN_ATTENDEE_ID + ") REFERENCES " + TABLE_USER + "(" + COL_ID + ")," +
                "FOREIGN KEY(" + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS + "(" + COLUMN_EVENT_ID + "),"+
                "PRIMARY KEY(" + COLUMN_ATTENDEE_ID + ", " + COLUMN_EVENT + ")" +
                ")";
        sqLiteDatabase.execSQL(CREATE_ATTENDEES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDEES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(sqLiteDatabase);
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, event.getTitle());
        values.put(COLUMN_DESCRIPTION, event.getDescription());
        values.put(COLUMN_TYPE, event.getType().toString());
        values.put(COLUMN_ATTENDEES, event.getAttendees());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String strDate = sdf.format(event.getDate());
        values.put(COLUMN_DATE, strDate);
        values.put(COLUMN_DURATION, event.getDuration());
        Pair<String,String> linksList = fromPairListToString(event.getLinks());
        if(linksList != null) {
            values.put(COLUMN_LINK_NAMES, linksList.first);
            values.put(COLUMN_LINK_URL, linksList.second);
        }
        values.put(COLUMN_CREATOR_USERNAME, event.getCreatorUsername());

        long eventId = db.insert(TABLE_EVENTS, null, values);

        if(event.getImages() != null) {
            for (Bitmap image : event.getImages()) {
                ContentValues imageValues = new ContentValues();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 0, stream);
                imageValues.put(COLUMN_EVENT, eventId);
                imageValues.put(COLUMN_IMAGE, stream.toByteArray());
                db.insert(TABLE_IMAGES, null, imageValues);
            }
        }
        db.close();
    }

    public List<Event> getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENTS, null);
        List<Event> eventList = getEventsWithQuery(cursor);
        db.close();
        return eventList;
    }

    public List<Event> getAllEventsByCreator(String creator) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENTS, null, COLUMN_CREATOR_USERNAME + "=?",
                new String[]{creator}, null, null, null);
        List<Event> eventList = getEventsWithQuery(cursor);
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
            try {
                String strDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                event.setDate(sdf.parse(strDate));
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
        sqLiteDatabase.beginTransaction();
        int result = 0;
        try {
            sqLiteDatabase.delete(TABLE_IMAGES, COLUMN_EVENT + " = ?", new String[]{String.valueOf(event.getId())});
            sqLiteDatabase.delete(TABLE_ATTENDEES, COLUMN_EVENT + " = ?", new String[]{String.valueOf(event.getId())});

            result = sqLiteDatabase.delete(TABLE_EVENTS, COLUMN_EVENT_ID + "=?", new String[]{String.valueOf(event.getId())});
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        sqLiteDatabase.close();
        return result>0;
    }

    public void removeExpiredEvents() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        sqLiteDatabase.beginTransaction();
        try {
            String deleteImagesQuery = "DELETE FROM " + TABLE_IMAGES + " WHERE "
                    + COLUMN_EVENT + " IN (SELECT " + COLUMN_EVENT_ID + " FROM " + TABLE_EVENTS + " WHERE " + COLUMN_DATE + " < ?)";
            sqLiteDatabase.execSQL(deleteImagesQuery, new String[]{currentDate});

            String deleteAttendeesQuery = "DELETE FROM " + TABLE_ATTENDEES + " WHERE "
                    + COLUMN_EVENT + " IN (SELECT " + COLUMN_EVENT_ID + " FROM " + TABLE_EVENTS + " WHERE " + COLUMN_DATE + " < ?)";
            sqLiteDatabase.execSQL(deleteAttendeesQuery, new String[]{currentDate});

            String query = "DELETE FROM " + TABLE_EVENTS + " WHERE " + COLUMN_DATE + " < ?";
            sqLiteDatabase.execSQL(query, new String[]{currentDate});
        } finally {
            sqLiteDatabase.endTransaction();
        }
        sqLiteDatabase.close();
    }

    public boolean isUserAttendingEvent(long userId, long eventId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        boolean isAttending = false;

        Cursor cursor = sqLiteDatabase.query(TABLE_ATTENDEES, null, COLUMN_EVENT + "=? AND " + COLUMN_ATTENDEE_ID + "=?",
                new String[]{String.valueOf(eventId), String.valueOf(userId)}, null, null, null);

        if(cursor != null) {
            if(cursor.moveToFirst())
                isAttending = true;
            cursor.close();
        }
        sqLiteDatabase.close();
        return isAttending;
    }

    public boolean isUserEventCreator(String mail, long eventId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        boolean isCreator = false;

        Cursor cursor = sqLiteDatabase.query(TABLE_EVENTS, new String[]{COLUMN_CREATOR_USERNAME},
                COLUMN_EVENT_ID + "=?",
                new String[]{String.valueOf(eventId)}, null, null, null);

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                String creatorMail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATOR_USERNAME));
                isCreator = creatorMail.equals(mail);
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return isCreator;
    }

    public void attendEvent(long userId, long eventId) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ATTENDEE_ID, userId);
        values.put(COLUMN_EVENT, eventId);
        sqLiteDatabase.insert(TABLE_ATTENDEES, null, values);

        String updateQuery = "UPDATE " + TABLE_EVENTS + " SET " + COLUMN_ATTENDEES + " = " + COLUMN_ATTENDEES + " - 1 WHERE " + COLUMN_EVENT_ID + " = ?";
        sqLiteDatabase.execSQL(updateQuery, new String[]{String.valueOf(eventId)});
        sqLiteDatabase.close();
    }

    public List<Event> getAttendedEvents(long userId) {
        List<Event> attendedEvents = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_ATTENDEES, new String[]{COLUMN_EVENT},
                COLUMN_ATTENDEE_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long eventId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_EVENT));
                Event event = getEvent(eventId);
                if (event != null) {
                    attendedEvents.add(event);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        sqLiteDatabase.close();
        return attendedEvents;
    }

    public boolean isEventFull(long eventId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        boolean isFull = false;

        Cursor cursor = sqLiteDatabase.query(TABLE_EVENTS, new String[]{COLUMN_ATTENDEES},
                COLUMN_EVENT_ID + "=? AND " + COLUMN_ATTENDEES + " <= 0",
                new String[]{String.valueOf(eventId)}, null, null, null);

        if (cursor != null) {
            isFull = cursor.getCount() > 0;
            cursor.close();
        }

        sqLiteDatabase.close();
        return isFull;
    }

    public List<String> getAttendingUsersMail(long eventId) {
        List<String> attendingUserMails = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT " + COL_EMAIL + " FROM " + TABLE_USER + " INNER JOIN " +
                    TABLE_ATTENDEES + " ON " + TABLE_USER + "." + COL_ID + " = " +
                    TABLE_ATTENDEES + "." + COLUMN_ATTENDEE_ID + " WHERE " +
                    COLUMN_EVENT + " = ?";
            cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(eventId)});

            if(cursor != null) {
                if(cursor.moveToFirst()) {
                    do {
                        String mail = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL));
                        attendingUserMails.add(mail);
                    }while (cursor.moveToNext());
                }
            }
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return attendingUserMails;
    }

    public boolean addUser(String firstname, String lastname, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FIRSTNAME, firstname);
        contentValues.put(COL_LASTNAME, lastname);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);
        contentValues.put(COL_ROLE, role);

        long result = db.insert(TABLE_USER, null, contentValues);
        return result != -1;
    }

    public User getUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " +
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

    private List<Event> getEventsWithQuery(Cursor query) {
        List<Event> eventList = new ArrayList<>();

        if(query.moveToFirst()) {
            do {
                long eventId = query.getLong(query.getColumnIndexOrThrow(COLUMN_EVENT_ID));
                Event event = getEvent(eventId);
                if (event != null) {
                    eventList.add(event);
                }
            } while (query.moveToNext());
        }
        query.close();
        return eventList;
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