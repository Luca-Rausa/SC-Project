package com.example.myapplication4;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Pair;
import java.util.Date;
import java.util.List;

public class Event {
    private String title;
    private String description;
    private EventType type;
    private int attendees;
    private Date date;
    private int duration;
    private List<Bitmap> images;
    private List<Pair<String,String>> links;
    private String creatorUsername;

    public Event(String title, String description, EventType type, int attendees, Date date,
                 int duration, List<Bitmap> images, List<Pair<String,String>> links, String creatorUsername) {
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

    public Event(String title, String description, EventType type, Date date, int duration, String creatorUsername) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.date = date;
        this.duration = duration;
        this.creatorUsername = creatorUsername;
    }

    public Event() {}

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

    public void setImages(List<Bitmap> image_paths) {
        this.images = image_paths;
    }

    public List<Pair<String, String>> getLinks() {
        return links;
    }

    public void setLinks(List<Pair<String,String>> links) {
        this.links = links;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }
}
