package com.bog.password_manager_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        findViewById(R.id.registration_btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // валидируем данные и посылаем запрос
                String password = ((EditText) findViewById(R.id.registration_password)).getText().toString();
                String login = ((EditText) findViewById(R.id.registration_login)).getText().toString();

                if (login.length() > 3 && password.length() > 3) {//validate(login) && validate(password)) {
                    sendRegistrationRequest(login, password);
                } else {
                    Toast.makeText(RegistrationActivity.this, "Слишком слабый пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendRegistrationRequest(String password, String login) {
        NetworkManager manager = NetworkManager.getInstance();

        manager.setCallback(new NetworkManager.Callback() {
            @Override
            public void onLoaded(Integer key) {
                onTextLoaded(key);
            }
        });

        manager.registrate("{ \"username\":\"" + login +  "\", \"password\":\"" + password + "\"}");
    }


    private void onTextLoaded(Integer resultCode) {
        if (resultCode != 200) {
            Toast.makeText(RegistrationActivity.this, "Ошибка регистрации " + resultCode, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegistrationActivity.this, "Регистрация прошла успешно ", Toast.LENGTH_SHORT).show();
        }
    }

}
