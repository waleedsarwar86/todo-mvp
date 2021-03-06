package com.todo.ui.base;


import io.reactivex.Single;

/**
 * @author Waleed Sarwar
 * @since Nov 25, 2017
 */

public interface BaseContract {

    interface View {

        void hideKeyboard();

        void showSnackBar(final String message);

        Single<Boolean> showSnackBar(final String message, final String action);

        Single<Boolean> showSnackBar(int messageRes, final int actionRes);


        void finishActivity();

    }

    interface Presenter<V extends BaseContract.View> {

        void attachView(V View);

        void detachView();


    }

}
