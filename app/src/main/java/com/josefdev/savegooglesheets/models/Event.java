package com.josefdev.savegooglesheets.models;

import java.util.Date;

public class Event {
    String id;
    String name;
    Date fecha = new Date();

    String fechaString = fecha.toString();

    public Event(String id, String name, String fechaString) {
        this.id = id;
        this.name = name;
        this.fechaString = fechaString;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getFecha() {
        return fechaString;
    }
}
