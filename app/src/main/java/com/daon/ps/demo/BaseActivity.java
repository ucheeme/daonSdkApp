package com.daon.ps.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.daon.fido.client.sdk.AuthenticationEventListener;
import com.daon.fido.client.sdk.Fido;
import com.daon.fido.client.sdk.IXUAF;
import com.daon.fido.client.sdk.core.Error;
import com.daon.fido.client.sdk.core.IUafCancellableClientOperation;
import com.daon.fido.client.sdk.model.Authenticator;
import com.daon.fido.client.sdk.model.AuthenticatorReg;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;


public abstract class BaseActivity extends AppCompatActivity {
    String ADOS_FACE_AAID = "D409#8201";
    ArrayList<String> aaidList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (CoreApplication.getFido() == null) {
            CoreApplication.setFido(Fido.getInstance(this.getApplicationContext()));
        }
    }

    public static boolean isUafInitialised() {
        return CoreApplication.isUafInitialised();
    }

    public static void setIsUafInitialised(boolean isUafInitialised) {
        CoreApplication.setUafInitialised(isUafInitialised);
    }

    protected static IXUAF getFido() {
        return CoreApplication.getFido();
    }

    protected void showMessage(final String message) {
        Log.d(CoreApplication.TAG, "show message: " + message);
        if (message != null && !message.isEmpty()) {
            Snackbar sb = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            View snackbarView = sb.getView();
            TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setMaxLines(5);
            sb.show();
        }
    }

    public boolean discover(){
        aaidList = new ArrayList<>();
        Authenticator[] availableAuthenticators = null;
        try {
            availableAuthenticators = getFido().discover().getAvailableAuthenticators();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        boolean authenticatorAvailable = false;
        for (Authenticator authenticator: availableAuthenticators) {
            if(((AuthenticatorReg)authenticator).isRegistered()){
                Log.d("RegisteredAAID", authenticator.getAaid());
                aaidList.add(authenticator.getAaid());
                authenticatorAvailable = true;
            }
        }

        return authenticatorAvailable;
    }

    public class AuthEventListener extends AuthenticationEventListener {


        @Override
        public void onAuthListAvailable(Authenticator[][] authenticators) {

        }

        @Override
        public void onAuthenticationComplete() {
            Toast.makeText(BaseActivity.this,
                            "Authentication Complete", Toast.LENGTH_LONG)
                    .show();
            Intent intent = new Intent(BaseActivity.this, LandingActivity.class);
            startActivity(intent);

        }

        @Override
        public void onAuthenticationFailed(Error error) {

            IUafCancellableClientOperation authOperation = (IUafCancellableClientOperation) getFido().getCurrentFidoOperation();
            if(authOperation != null){
                authOperation.cancelAuthenticationUI();
            }

            Toast.makeText(BaseActivity.this,
                            error.getMessage(), Toast.LENGTH_LONG)
                    .show();
            Intent intent = new Intent(BaseActivity.this, LandingActivity.class);
            startActivity(intent);
        }
    }

}