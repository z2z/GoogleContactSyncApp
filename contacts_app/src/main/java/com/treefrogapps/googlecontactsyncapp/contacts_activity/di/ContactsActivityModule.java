package com.treefrogapps.googlecontactsyncapp.contacts_activity.di;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.treefrogapps.googlecontactsyncapp.common.di_scopes.ActivityScope;
import com.treefrogapps.googlecontactsyncapp.common.di_scopes.ApplicationScope;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.model.Clock;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.model.ContactsModel;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.presenter.ContactsPresenter;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module public class ContactsActivityModule {

    private FragmentManager fragmentManager;

    public ContactsActivityModule(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Provides @ActivityScope ContactsPresenter providePresenter(ContactsModel model) {
        return new ContactsPresenter(model, fragmentManager);
    }

    @Module public static class ApplicationScoped {

        @Provides @ApplicationScope Clock provideClock() {
            return () -> TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        }

        @Provides @ApplicationScope ObjectMapper provideJSONMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper;
        }

        @Provides @ApplicationScope ContactsModel provideModel(ContentResolver resolver, SharedPreferences preferences,
                                                               OkHttpClient client, ObjectMapper jsonMapper, Clock clock) {
            return new ContactsModel(resolver, preferences, client, jsonMapper, clock);
        }
    }
}
