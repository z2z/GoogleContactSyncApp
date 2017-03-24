package com.treefrogapps.googlecontactsyncapp.application.di;

import android.app.Application;
import android.content.ContentResolver;

import com.treefrogapps.googlecontactsyncapp.common.di_scopes.ApplicationScope;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.di.ContactsActivityModule;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

@Module(includes = {ContactsActivityModule.ApplicationScoped.class})
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides @ApplicationScope ContentResolver provideContentProvider(){
        return application.getContentResolver();
    }

    @Provides @ApplicationScope OkHttpClient getOkHttpClient(){
        return new OkHttpClient.Builder()
                .cache(new Cache(application.getCacheDir(), 10 * 1024 * 1024))
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .build();
    }
}
