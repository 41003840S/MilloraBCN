package com.example.manuel.millorabcn;

import android.location.Location;

/**
 * Created by Manuel on 24/02/2016.
 */
public class OnLocationChangedEvent {
    Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public OnLocationChangedEvent(Location location) {
        this.location = location;
    }
}
