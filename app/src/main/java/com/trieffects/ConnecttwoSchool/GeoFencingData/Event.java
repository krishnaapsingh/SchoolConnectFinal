package com.trieffects.ConnecttwoSchool.GeoFencingData;

/**
 * Created by Trieffects on 15-Nov-17.
 */

public class Event {

    protected int id;
    protected String type;
    protected String date;
    protected String placeName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
