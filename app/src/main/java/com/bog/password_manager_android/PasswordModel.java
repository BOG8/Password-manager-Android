package com.bog.password_manager_android;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by alex on 14.04.2017.
 */

class PasswordModel implements Serializable, Cloneable {
    public String name;
    public String password;
    public HashMap<String, String> additionalFields = new HashMap<>();
    public PasswordModel clone() {
        PasswordModel result = new PasswordModel();
        result.name = name;
        result.password = password;
        result.additionalFields = new HashMap<>(additionalFields);
        return result;
    }
}
