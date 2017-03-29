package com.treefrogapps.googlecontactsyncapp.contacts_service.di;

import com.treefrogapps.googlecontactsyncapp.common.di_scopes.ServiceScope;
import com.treefrogapps.googlecontactsyncapp.contacts_service.clients.ContactsApiClient;
import com.treefrogapps.googlecontactsyncapp.contacts_service.clients.PeopleApiClient;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module public class ApiServiceModule {

    @Provides @ServiceScope PeopleApiClient providePeopleApiRequest(OkHttpClient client) {
        return new PeopleApiClient(client);
    }

    @Provides @ServiceScope ContactsApiClient provideContactApiRequest(OkHttpClient client) {
        return new ContactsApiClient(client);
    }
}
