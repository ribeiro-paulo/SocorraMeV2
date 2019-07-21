package com.example.gladson.socorramev2.model;

public class EmmergencyContact implements Comparable {

    private String name;
    private String number;

    public EmmergencyContact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int compareTo(Object o) {
        EmmergencyContact em = (EmmergencyContact) o;
        return getName().compareTo(em.getName());
    }
}
