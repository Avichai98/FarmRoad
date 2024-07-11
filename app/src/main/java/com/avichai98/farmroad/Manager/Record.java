package com.avichai98.farmroad.Manager;

import androidx.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Record {
    private int score;
    private Date date;
    private double lat, lon;

    public Record() {
    }

    public Record(int score) {
        this.score = score;
        this.date = Calendar.getInstance().getTime();
        this.lat = 0;
        this.lon = 0;
    }

    public int getScore() {
        return score;
    }

    // Method to get formatted date string ("dd.MM.yyyy")
    public String getFormattedDate() {
       SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault());
        return dateFormat.format(this.date.getTime());
    }

    // Method to get formatted time string ("HH:mm:ss")
    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault());
        return formatter.format(this.date.getTime());
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @NonNull
    @Override
    public String toString() {
        return "Record{" +
                "score=" + score +
                ", date=" + getFormattedDate() +
                ", time=" + getFormattedTime() +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}

