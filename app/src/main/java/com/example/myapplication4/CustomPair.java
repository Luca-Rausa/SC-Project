package com.example.myapplication4;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomPair<F, S> implements Parcelable {
    public F first;
    public S second;

    public CustomPair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    protected CustomPair(Parcel in) {
        first = (F) in.readValue(getClass().getClassLoader());
        second = (S) in.readValue(getClass().getClassLoader());
    }

    public static final Creator<CustomPair> CREATOR = new Creator<CustomPair>() {
        @Override
        public CustomPair createFromParcel(Parcel in) {
            return new CustomPair(in);
        }

        @Override
        public CustomPair[] newArray(int size) {
            return new CustomPair[size];
        }
    };

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(first);
        dest.writeValue(second);
    }
}
