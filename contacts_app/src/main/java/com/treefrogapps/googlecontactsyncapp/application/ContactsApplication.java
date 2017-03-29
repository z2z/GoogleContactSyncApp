package com.treefrogapps.googlecontactsyncapp.application;

import android.app.Application;
import android.content.Context;

import com.treefrogapps.googlecontactsyncapp.application.di.ApplicationComponent;
import com.treefrogapps.googlecontactsyncapp.application.di.ApplicationModule;
import com.treefrogapps.googlecontactsyncapp.application.di.DaggerApplicationComponent;


public class ContactsApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static ApplicationComponent getApplicationComponent(Context context) {
        return ((ContactsApplication) context.getApplicationContext()).applicationComponent;
    }
}
