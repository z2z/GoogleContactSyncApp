package com.treefrogapps.googlecontactsyncapp.contacts_service.clients;


import okhttp3.OkHttpClient;

public class ContactsApiClient extends ApiClient {

    public ContactsApiClient(OkHttpClient client) {
        super(client);
    }
}
