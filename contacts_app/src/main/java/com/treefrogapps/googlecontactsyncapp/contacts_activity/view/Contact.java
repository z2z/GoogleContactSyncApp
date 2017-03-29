package com.treefrogapps.googlecontactsyncapp.contacts_activity.view;


import android.support.annotation.Nullable;

public class Contact {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String imageUrl;

    public Contact(String firstName, String lastName, String phoneNumber, @Nullable String imageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
