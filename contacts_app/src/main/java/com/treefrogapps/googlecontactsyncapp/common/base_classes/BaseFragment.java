package com.treefrogapps.googlecontactsyncapp.common.base_classes;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.treefrogapps.googlecontactsyncapp.common.base_interfaces.IContext;
import com.treefrogapps.googlecontactsyncapp.common.base_interfaces.IPresenterLifeCycle;

public abstract class BaseFragment<IView, IViewPresenter, Presenter extends IPresenterLifeCycle>
        extends Fragment implements IContext {

    private Presenter presenter;

    public void initialise(Presenter presenter, IView view, boolean savedInstance){
        this.presenter = presenter;
        if(!savedInstance) {
            presenter.onCreate(view);
        } else {
            presenter.onConfigChange(view);
        }
    }

    @SuppressWarnings("unchecked") public IViewPresenter getPresenter(){
        return (IViewPresenter) presenter;
    }
    @Override public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override public Context getActivityContext() {
        return getActivity();
    }
}
