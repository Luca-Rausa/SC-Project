package com.example.myapplication4;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event implements Parcelable {
    private long id;
    private String title;
    private String description;
    private EventType type;
    private int attendees;
    private Date date;
    private int duration;
    private List<Bitmap> images;
    private List<CustomPair<String, String>> links;
    private String creatorUsername;

    public Event(String title, String description, EventType type, int attendees, Date date,
                 int duration, List<Bitmap> images, List<CustomPair<String, String>> links, String creatorUsername) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.attendees = attendees;
        this.date = date;
        this.duration = duration;
        this.images = images;
        this.links = links;
        this.creatorUsername = creatorUsername;
    }

    public Event(){}

    protected Event(Parcel in) {
        title = in.readString();
        description = in.readString();
        type = (EventType) in.readSerializable();
        attendees = in.readInt();
        date = new Date(in.readLong());
        duration = in.readInt();
        images = new ArrayList<>();
        in.readList(images, Bitmap.class.getClassLoader());
        links = new ArrayList<>();
        in.readList(links, CustomPair.class.getClassLoader());
        creatorUsername = in.readString();
    }

    public Event(String title, String description, EventType eventType, Date date, int duration, String creatorUsername) {
        this.title = title;
        this.description = description;
        this.type = eventType;
        this.date = date;
        this. duration = duration;
        this.creatorUsername = creatorUsername;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeSerializable(type);
        dest.writeInt(attendees);
        dest.writeLong(date.getTime());
        dest.writeInt(duration);
        if (images != null)
            dest.writeList(images);
        if (links != null)
            dest.writeList(links);
        dest.writeString(creatorUsername);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public int getAttendees() {
        return attendees;
    }

    public void setAttendees(int attendees) {
        this.attendees = attendees;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Bitmap> getImages() {
        return images;
    }

    public void setImages(List<Bitmap> images) {
        this.images = images;
    }

    public List<CustomPair<String, String>> getLinks() {
        return links;
    }

    public void setLinks(List<CustomPair<String, String>> links) {
        this.links = links;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public void setId(long id) {
        this. id = id;
    }

    public long getId() {
        return id;
    }
}
