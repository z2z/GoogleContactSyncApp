package com.treefrogapps.googlecontactsyncapp.application.di;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;

import com.treefrogapps.googlecontactsyncapp.common.di_scopes.ApplicationScope;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.di.ContactsActivityModule;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

@Module(includes = {ContactsActivityModule.ApplicationScoped.class})
public class ApplicationModule {

    private static final String APP_PREFERENCES = "app_preferences";

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides @ApplicationScope ContentResolver provideContentProvider() {
        return application.getContentResolver();
    }

    @Provides @ApplicationScope OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .cache(new Cache(application.getCacheDir(), 10 * 1024 * 1024))
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .build();
    }

    @Provides @ApplicationScope SharedPreferences provideSharedPreferences() {
        return application.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }
}
