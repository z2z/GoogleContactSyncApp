package com.treefrogapps.googlecontactsyncapp.common.base_classes;

import com.treefrogapps.googlecontactsyncapp.common.base_interfaces.IModelLifeCycle;

public abstract class BasePresenter<IPresenter, IModel, Model extends IModelLifeCycle<IPresenter>> {

    private Model model;

    public void initialise(Model model, IPresenter presenter, boolean savedInstance) {
        this.model = model;
        if (!savedInstance) {
            model.onCreate(presenter);
        } else {
            model.onConfigChange(presenter);
        }
    }

    @SuppressWarnings("unchecked") public IModel getModel() {
        return (IModel) model;
    }
}
