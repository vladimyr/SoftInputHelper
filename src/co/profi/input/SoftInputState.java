package co.profi.input;

import android.view.inputmethod.InputMethodManager;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

enum SoftInputState {
    SOFT_INPUT_UNCHANGED_SHOWN(InputMethodManager.RESULT_UNCHANGED_SHOWN),
    SOFT_INPUT_UNCHANGED_HIDDEN(InputMethodManager.RESULT_UNCHANGED_HIDDEN),
    SOFT_INPUT_SHOWN(InputMethodManager.RESULT_SHOWN),
    SOFT_INPUT_HIDDEN(InputMethodManager.RESULT_HIDDEN);

    private static final Map<Byte, SoftInputState> lookup
            = new HashMap<Byte, SoftInputState>();

    static {
        Set<SoftInputState> values = EnumSet.allOf(SoftInputState.class);
        for (SoftInputState result : values)
            lookup.put(result.getCode(), result);
    }

    private byte code;

    SoftInputState(int val) {
        code = (byte) val;
    }

    private byte getCode() {
        return code;
    }

    public static SoftInputState get(int code) {
        return lookup.get((byte) code);
    }
}