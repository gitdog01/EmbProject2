package com.example.mju4.embproject;

import java.util.Date;

/**
 * Created by mju4 on 2017-12-09.
 */

public class EventDTO {
    private String EventName;
    private String EventCo;
    private String userName;

    private Date EventTime;
    private double Latitude;//위도
    private double Longitude;//경도


    public EventDTO(){}
    public EventDTO(String nEventName, String nEventCo, String nuserName, Date nDate, double nlatitude, double nlongitude){
        this.EventName = nEventName;
        this.EventCo = nEventCo;
        this.userName = nuserName;

        this.EventTime = nDate;
        this.Latitude = nlatitude;
        this.Longitude = nlongitude;
    }

    public double getLongitude() {
        return Longitude;
    }
    public double getLatitude() {
        return Latitude;
    }
    public Date getEventTime() {
        return EventTime;
    }
    public String getUserName() {
        return userName;
    }
    public String getEventCo() {
        return EventCo;
    }
    public String getEventName() {
        return EventName;
    }
}
