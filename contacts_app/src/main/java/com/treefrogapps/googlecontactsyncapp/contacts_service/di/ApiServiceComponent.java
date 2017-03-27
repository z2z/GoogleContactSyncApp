package com.treefrogapps.googlecontactsyncapp.contacts_service.di;


import com.treefrogapps.googlecontactsyncapp.common.di_scopes.ServiceScope;
import com.treefrogapps.googlecontactsyncapp.contacts_service.ApiService;

import dagger.Subcomponent;

@ServiceScope @Subcomponent(modules = {ApiServiceModule.class})
public interface ApiServiceComponent {

    void inject(ApiService service);
}
