package com.friday20.isyiar.helpers;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboardHelper {

    private static final String LOG_TAG = "SoftKeyboardHelper";

    /**
     * Hidden constructor
     */
    private SoftKeyboardHelper() {
    }

    /**
     * getInputMethodManager
     */
    private static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager)context.getSystemService(context.INPUT_METHOD_SERVICE);
    }

    /**
     * Hide the soft keyboard
     */
    public static void hide(Context context, View view) {

        if (context == null) {

        }
        else if (view == null) {

        }
        else {
            InputMethodManager imm = getInputMethodManager(context);
            IBinder token = view.getApplicationWindowToken();
            imm.hideSoftInputFromWindow(token, 0);
        }
    }

    /**
     * Show the soft keyboard
     */
    public static void show(Context context) {

        if (context == null) {

        }
        else {
            InputMethodManager imm = getInputMethodManager(context);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }
}