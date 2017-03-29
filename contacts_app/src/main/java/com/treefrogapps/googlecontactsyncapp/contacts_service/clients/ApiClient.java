package com.treefrogapps.googlecontactsyncapp.contacts_service.clients;

import okhttp3.OkHttpClient;

abstract class ApiClient {

    private OkHttpClient client;

    ApiClient(OkHttpClient client) {
        this.client = client;
    }

    public OkHttpClient getClient() {
        return client;
    }
}
