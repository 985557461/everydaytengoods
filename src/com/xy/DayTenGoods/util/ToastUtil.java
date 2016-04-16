package com.xy.DayTenGoods.util;import android.content.Context;import android.view.Gravity;import android.widget.Toast;import com.xy.DayTenGoods.ui.CustomApplication;public class ToastUtil extends Toast {    private static Toast mToast;    private ToastUtil(Context context) {        super(context);    }    public static Toast makeText(Context context, CharSequence text,                                 int duration) {        if (mToast == null) {            if (context == null) {                mToast = Toast.makeText(CustomApplication.getInstance().getApplicationContext(), "", duration);            } else {                mToast = Toast.makeText(context, "", duration);            }        }        mToast.setGravity(Gravity.CENTER, 0, 0);        mToast.setText(text);        mToast.setDuration(duration);        return mToast;    }    public static Toast makeText(Context context, int resId, int duration) {        if (mToast == null) {            if (context == null) {                mToast = Toast.makeText(CustomApplication.getInstance().getApplicationContext(), "", duration);            } else {                mToast = Toast.makeText(context, "", duration);            }        }        mToast.setGravity(Gravity.CENTER, 0, 0);        mToast.setText(context.getString(resId));        mToast.setDuration(duration);        return mToast;    }    public static void makeShortText(CharSequence text) {        if (mToast == null) {            mToast = Toast.makeText(CustomApplication.getInstance().getApplicationContext(), "", Toast.LENGTH_SHORT);        }        mToast.setGravity(Gravity.CENTER, 0, 0);        mToast.setText(text);        mToast.setDuration(Toast.LENGTH_SHORT);        mToast.show();    }    public static void makeShortTextCenter(CharSequence text) {        if (mToast == null) {            mToast = Toast.makeText(CustomApplication.getInstance().getApplicationContext(), "", Toast.LENGTH_SHORT);        }        mToast.setGravity(Gravity.CENTER, 0, 0);        mToast.setText(text);        mToast.setDuration(Toast.LENGTH_SHORT);        mToast.setGravity(Gravity.CENTER, 0, 0);        mToast.show();    }    public static void makeShortText(int resId) {        if (mToast == null) {            mToast = Toast.makeText(CustomApplication.getInstance().getApplicationContext(), "", Toast.LENGTH_SHORT);        }        mToast.setGravity(Gravity.CENTER, 0, 0);        mToast.setText(CustomApplication.getInstance().getApplicationContext().getString(resId));        mToast.setDuration(Toast.LENGTH_SHORT);        mToast.show();    }    public static void makeLongText(CharSequence text) {        if (mToast == null) {            mToast = Toast.makeText(CustomApplication.getInstance().getApplicationContext(), "", Toast.LENGTH_LONG);        }        mToast.setGravity(Gravity.CENTER, 0, 0);        mToast.setText(text);        mToast.setDuration(Toast.LENGTH_LONG);        mToast.show();    }    public static void makeLongText(int resId) {        if (mToast == null) {            mToast = Toast.makeText(CustomApplication.getInstance().getApplicationContext(), "", Toast.LENGTH_LONG);        }        mToast.setGravity(Gravity.CENTER, 0, 0);        mToast.setText(CustomApplication.getInstance().getApplicationContext().getString(resId));        mToast.setDuration(Toast.LENGTH_LONG);        mToast.show();    }}