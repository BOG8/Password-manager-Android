package com.bog.password_manager_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bog.password_manager_android.network.NetworkIntentService;
import com.bog.password_manager_android.network.ServiceHelper;


public class RegistrationActivity extends AppCompatActivity implements ServiceHelper.registrationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        findViewById(R.id.registration_btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String password = ((EditText) findViewById(R.id.registration_password)).getText().toString();
                String login = ((EditText) findViewById(R.id.registration_login)).getText().toString();

                if (!login.isEmpty() && !password.isEmpty()) {
                    sendRegistrationRequest(login, password);
                } else {
                    Toast.makeText(RegistrationActivity.this, R.string.validation_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendRegistrationRequest(String login, String password) {
        ServiceHelper helper = ServiceHelper.getInstance(this);
        helper.registrate(this, this, login, password);
    }


    @Override
    protected void onStop() {
        ServiceHelper.getInstance(this).removeRegListener();
        super.onStop();
    }

    @Override
    public void onRegistrationResult(String result) {
        if (!result.equals(NetworkIntentService.REGISTRATE_SUCCESS)) {
            Toast.makeText(RegistrationActivity.this, getString(R.string.registration_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegistrationActivity.this, R.string.registration_succes, Toast.LENGTH_SHORT).show();
        }
    }
}
