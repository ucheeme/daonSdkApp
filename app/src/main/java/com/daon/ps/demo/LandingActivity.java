package com.daon.ps.demo;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import androidx.activity.OnBackPressedCallback;

import com.daon.fido.client.sdk.IXUAFInitialiseListener;
import com.daon.fido.client.sdk.core.Error;
import com.daon.ps.demo.api.ErrorCode;

import java.util.List;

public class LandingActivity extends BaseActivity {

    Button faceCapture, authenticateFace;
    boolean hideSplash = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        final View content = findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        // Check whether the initial data is ready.
                        if (hideSplash) {
                            // The content is ready. Start drawing.
                            content.getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        } else {
                            // The content isn't ready. Suspend.
                            return false;
                        }
                    }
                });

        faceCapture = findViewById(R.id.face_capture_btn);
        authenticateFace = findViewById(R.id.authenticate_face_btn);


        faceCapture.setOnClickListener(v -> {
            Intent intent = new Intent(LandingActivity.this, RegisterFaceActivity.class);
            startActivity(intent);
        });

        authenticateFace.setOnClickListener(v -> {
            Intent intent = new Intent(LandingActivity.this, AuthenticateFaceActivity.class);
            startActivity(intent);
        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Back is pressed... Closing the application
                finishAffinity();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initFido();
    }

    private void enableCaptureButtons() {

        if(aaidList == null || aaidList.isEmpty()){
            faceCapture.setEnabled(true);
        }


        if (aaidList.contains(ADOS_FACE_AAID)){
            authenticateFace.setEnabled(true);
            faceCapture.setEnabled(false);
        }

    }

    private void initFido() {
        if(!isUafInitialised()){
            new InitialiseSdkTask(getApplicationContext(), new IXUAFInitialiseListener() {
                @Override
                public void onInitialiseComplete() {
                    Log.d("DAON", "Init Complete :" + "Success");
                    setIsUafInitialised(true);
                    discover();
                    hideSplash = false;
                    runOnUiThread(() -> enableCaptureButtons());
                }

                @Override
                public void onInitialiseWarnings(List<Error> warnings) {
                    for(int i=0; i<warnings.size(); i++) {
                        Log.d("DAON", "Init warning :" + warnings.get(i).getMessage());
                    }
                    showMessage(warnings.get(0).getMessage());
                    hideSplash = false;
                }

                @Override
                public void onInitialiseFailed(int code, String message) {
                    showMessage(message);
                    hideSplash = false;
                }
            }).execute();
        }else{
            discover();
            runOnUiThread(this::enableCaptureButtons);

        }
    }

}