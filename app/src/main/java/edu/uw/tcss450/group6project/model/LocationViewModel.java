package edu.uw.tcss450.group6project.model;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class LocationViewModel extends ViewModel {

    private MutableLiveData<Location> mLocation;

    public LocationViewModel() {
        mLocation = new MutableLiveData<>();
    }

    public void setLocation(final Location location) {
        if(!location.equals(mLocation.getValue())) {
            mLocation.setValue(location);
        }
    }

    public double getLatitude() {
        return mLocation.getValue().getLatitude();
    }

    public double getLongitude() {
        return mLocation.getValue().getLongitude();
    }
}
