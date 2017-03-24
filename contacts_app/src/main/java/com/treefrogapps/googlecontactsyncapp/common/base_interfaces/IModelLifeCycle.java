package com.treefrogapps.googlecontactsyncapp.common.base_interfaces;


public interface IModelLifeCycle<IPresenter> {

    void onCreate(IPresenter presenter);
    void onConfigChange(IPresenter presenter);
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
}
