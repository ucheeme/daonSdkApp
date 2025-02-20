package com.daon.ps.demo;

import android.content.Context;
import android.os.Bundle;

import com.daon.fido.client.sdk.IXUAFInitialiseListener;
import com.daon.fido.client.sdk.core.Error;
import com.daon.fido.client.sdk.util.TaskExecutor;

import java.util.List;

public class InitialiseSdkTask extends TaskExecutor<Void> implements IXUAFInitialiseListener {

    private final IXUAFInitialiseListener callback;
    private final Context context;
    private final Bundle parameters = new Bundle();

    public InitialiseSdkTask(Context context, IXUAFInitialiseListener callback) {
        this.context = context;
        this.callback = callback;

    }

    @Override
    protected Void doInBackground() {

        parameters.clear();
        
        parameters.putString("com.daon.sdk.log", "true");
        parameters.putString("com.daon.sdk.initParamsToServer", "true");
        // Not recommended. Enable Authenticate Per Use key access.
        //parameters.putString("com.daon.finger.access.biometry", "true");
        // Enable performance score
        //parameters.putString("com.daon.sdk.devicePerformance", "true");
        parameters.putString("com.daon.sdk.ados.enabled", "true");
        //Enabling ADoS SRP Passcode
        parameters.putString("com.daon.sdk.passcode.ados.version", "2");
        parameters.putString("com.daon.sdk.screencapture.enabled", "true");
        parameters.putString("com.daon.sdk.extendedErrorCodes", "true");
        parameters.putString("com.daon.face.liveness.enroll", "false");
        parameters.putString("com.daon.face.liveness.active.type", "none");
        parameters.putString("com.daon.face.liveness.passive.type", "server");

        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        CoreApplication.getFido().initWithService(parameters, new CustomCaptureFragmentFactory(), RPSAServiceComm.getInstance(context), this);
    }

    @Override
    public void onInitialiseComplete() {
        if(callback != null) {
            callback.onInitialiseComplete();
        }
    }

    @Override
    public void onInitialiseFailed(int errorCode, String message) {
        if(callback != null) {
            callback.onInitialiseFailed(errorCode, message);
        }
    }

    @Override
    public void onInitialiseWarnings(List<Error> warnings) {
        if(callback != null) {
            callback.onInitialiseWarnings(warnings);
        }
    }
}
