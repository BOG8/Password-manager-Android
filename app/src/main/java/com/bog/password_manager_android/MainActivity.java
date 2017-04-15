package com.bog.password_manager_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final static String PASSWORD_MANAGER = "Password manager";
    final static String CIPHER_DATA = "cipher data";
    final static String IV = "iv";
    private final static String INITIAL_DATA = "[{\"name\": \"Example.com\", \"password\": \"MyPass1234\", \"additionalFields\": {\"login\": \"LoginExample\"}}]" ;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(PASSWORD_MANAGER, 0);

        findViewById(R.id.entering_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = ((EditText) findViewById(R.id.password_edit_text)).getText().toString();
                if (!password.isEmpty()) {
                    PasswordCipher cipher = PasswordCipher.getInstance();
                    cipher.setPassword(password);
                    String cipherData = preferences.getString(CIPHER_DATA, null);
                    if (cipherData != null) {
                        String iv = preferences.getString(IV, null);
                        String clearData = cipher.decrypt(Converter.toByte(cipherData), Converter.toByte(iv));
                        if (clearData != null) {
                            startActivity(new Intent(MainActivity.this, ResourceActivity.class));
                        } else {
                            Toast.makeText(MainActivity.this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        initializeData(cipher);
                        startActivity(new Intent(MainActivity.this, ResourceActivity.class));
                    }
                } else {
                    Toast.makeText(MainActivity.this, R.string.empty_password, Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.registration_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (preferences.getString(CIPHER_DATA, null) == null) {
            ((TextView) findViewById(R.id.no_pass_text)).setText(R.string.no_pass_text);
        } else {
            ((TextView) findViewById(R.id.no_pass_text)).setText(null);
        }
    }

    private void initializeData(PasswordCipher cipher) {
        cipher.encrypt(INITIAL_DATA);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CIPHER_DATA, cipher.getCipherData());
        editor.putString(IV, cipher.getIv());
        editor.apply();
        cipher.clearCipher();
    }
}
