package com.bog.password_manager_android;

import android.util.Base64;

/**
 * Created by Олег on 13.04.2017.
 */

class Converter {
    static String toString(byte[] buf) {
        return Base64.encodeToString(buf, Base64.DEFAULT);
    }

    static byte[] toByte(String str) {
        return Base64.decode(str, Base64.DEFAULT);
    }
}
