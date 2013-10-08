package co.profi.input;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created with IntelliJ IDEA.
 * User: Dario
 * Date: 08.10.13.
 * Time: 00:45
 * To change this template use File | Settings | File Templates.
 */

class InputMethodService {
    final static String TAG = "InputMethodService";

    private static Class interface__IInputMethodManger;
    private static ClassNotFoundException interfaceNotFound;

    static {
        try {
            interface__IInputMethodManger = Class.forName("com.android.internal.view.IInputMethodManager");
        } catch (ClassNotFoundException e) {
            interfaceNotFound = e;
            Log.e(TAG, "Error obtaining interface IInputMethodManager!");
        }
    }

    Field mService;
    SoftInputHero.SoftInputCallback callback;

    InputMethodService(InputMethodManager imm) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        mService = imm.getClass().getDeclaredField("mService");
        mService.setAccessible(true);

        final Object ims = mService.get(imm);

        if (null == interface__IInputMethodManger)
            throw new ClassNotFoundException(interfaceNotFound.getMessage(), interfaceNotFound);

        mService.set(imm, Proxy.newProxyInstance(interface__IInputMethodManger.getClassLoader(),
                new Class[]{interface__IInputMethodManger}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("showSoftInput".equals(method.getName())) {
                    // populate arguments
                    Object[] temp = new Object[3];
                    temp[0] = args[0];
                    temp[1] = args[1];

                    // store original receiver
                    final ResultReceiver receiver = (ResultReceiver) args[2];

                    // add callback
                    temp[2] = new ResultReceiver(new Handler()) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            if (receiver != null)
                                receiver.send(resultCode, resultData);

                            if (callback != null && SoftInputState.SOFT_INPUT_SHOWN == SoftInputState.get(resultCode))
                                callback.onShow();
                        }
                    };

                    // redirect reference
                    args = temp;
                }

                // super invoke
                return method.invoke(ims, args);
            }
        }));
    }

    public void addSoftInputCallback(SoftInputHero.SoftInputCallback callback) {
        this.callback = callback;
    }

    public static InputMethodService from(InputMethodManager imm) {
        try {
            return new InputMethodService(imm);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "Error obtaining input method service from input method manager!");
            return null;
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Error setting proxy for service interface!");
            return null;
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Error accessing/modifying method service from input method manager!");
            return null;
        }
    }
}
