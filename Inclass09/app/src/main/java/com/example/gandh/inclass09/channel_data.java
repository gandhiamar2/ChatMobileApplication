package com.example.gandh.inclass09;

/**
 * Created by gandh on 3/27/2017.
 */

public class channel_data {
    String id;
    String name;
    boolean added;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdded() {
        return added;
    }

    @Override
    public String toString() {
        return "channel_data{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", added=" + added +
                '}';
    }

    public void setAdded(boolean added) {
        this.added = added;
    }
}
