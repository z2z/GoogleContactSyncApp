package com.treefrogapps.googlecontactsyncapp.common.base_interfaces;


public interface IPresenterLifeCycle<IView> {

    void onCreate(IView view);

    void onConfigChange(IView view);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy(boolean isFinishing);
}
