package com.daon.ps.demo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.daon.fido.client.sdk.IXUAFRegisterEventListener;
import com.daon.fido.client.sdk.core.Error;
import com.daon.fido.client.sdk.core.INotifyUafResultCallback;
import com.daon.fido.client.sdk.model.Authenticator;
import java.util.UUID;
import kotlin.NotImplementedError;

public class RegisterFaceActivity extends BaseActivity implements IXUAFRegisterEventListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_face);

        if(discover()){
            //Authenticator already registered
            //check aaidList for the specific authenticator registered and do something with it
        }else{
           setUp();
        }

    }

    private void setUp() {

        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        String email = "email09812@test.com"+
                randomUUIDString+"@test.com";

        getFido().registerWithUsername(email,this);

    }


    @Override
    public void onAuthListAvailable(Authenticator[][] authenticators) {
        throw new NotImplementedError();
    }

    @Override
    public void onRegistrationComplete() {
        Toast.makeText(RegisterFaceActivity.this,
                        "Registration Complete", Toast.LENGTH_LONG)
                .show();

        Intent intent = new Intent(RegisterFaceActivity.this, LandingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRegistrationFailed(Error error) {
        Toast.makeText(RegisterFaceActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG)
                .show();
        Intent intent = new Intent(RegisterFaceActivity.this, LandingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onExpiryWarning(INotifyUafResultCallback.ExpiryWarning[] expiryWarnings) {
        throw new NotImplementedError();
    }

    @Override
    public void onUserLockWarning() {
        throw new NotImplementedError();
    }

}