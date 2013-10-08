package co.profi.input;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class SoftInputHero {
    private final static String TAG = "SoftInputHero";

    private static SoftInputHero self = null;

    private Context mContext;
    private InputMethodService mService;

    private SoftInputHero(Context context) {
        mContext = context;

        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mService = InputMethodService.from(imm);
    }

    public static SoftInputHero from(Context context) {
        if (null == self)
            self = new SoftInputHero(context);

        return self;
    }

    public boolean registerSoftInputCallback(SoftInputCallback callback) {
        if (null == mService)
            return false;

        mService.addSoftInputCallback(callback);
        return true;
    }

    public static interface SoftInputCallback {
        public void onShow();
    }
}
