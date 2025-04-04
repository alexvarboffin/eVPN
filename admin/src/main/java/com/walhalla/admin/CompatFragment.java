package com.walhalla.admin;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class CompatFragment extends Fragment {

    Callback mainCallback;

    public interface Callback {

        void showMessage(String s);


        void onAttach(String tag, boolean b);

        void checkAnswer(int cursor, String string);

        void showLoader();

        void hideLoader();

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mainCallback = (Callback) context;
        } else {
            throw new RuntimeException(context + " must implement Callback");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mainCallback = null;
    }
}

