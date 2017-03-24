package com.treefrogapps.googlecontactsyncapp.contacts_service.di;


import com.treefrogapps.googlecontactsyncapp.common.di_scopes.ServiceScope;
import com.treefrogapps.googlecontactsyncapp.contacts_service.ApiService;

import dagger.Subcomponent;

@ServiceScope @Subcomponent(modules = {LoginServiceModule.class})
public interface LoginServiceComponent {

    void inject(ApiService service);
}
