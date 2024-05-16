package com.example.myapplication4;

public class FeedBackData{
    public String Feed;
    public String rating;
    public long ID;

    public FeedBackData(String userFeedback, String rating) {
        this.Feed=userFeedback;
        this.rating=rating;
    }
}
