package com.daon.ps.demo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.daon.sdk.authenticator.ErrorCodes;
import com.daon.sdk.authenticator.controller.AuthenticatorError;
import com.daon.sdk.authenticator.controller.LockResult;
import com.daon.sdk.face.Result;
import com.daon.sdk.face.YUV;
import com.daon.sdk.faceauthenticator.FaceErrorCodes;
import com.daon.sdk.faceauthenticator.controller.FaceControllerProtocol;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFaceFragment#} factory method to
 * create an instance of this fragment.
 */
public class RegisterFaceFragment extends BaseFaceFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        operationMode = OperationMode.REGISTRATION;
        getController().setImageQualityChecker((result, controllerConfiguration) -> result.getQualityResult().hasAcceptableQuality() && result.getQualityResult().hasAcceptableEyeDistance());
        return view;
    }

    @Override
    protected void done() {
        showMessage(R.string.face_enroll, false);
        if (getController() != null) {
            onAuthenticateWait(true);
            getController().registerImage(capturedImage, getCameraRotationDegrees(), new EnrolResultListener());
        }
    }

    @Override
    public void onLivenessEvent(FaceControllerProtocol.LivenessEventInfo info) {
        if (!getController().getExpectedLivenessEvents().isEmpty()) {
            if (info.allLivenessEventsDetected()) {
                if (takePhotoButton != null) takePhotoButton.setVisibility(View.VISIBLE);
                hideInfo();
                hideWarning();
                photoMode = PhotoMode.TAKE;
            } else {
                switch (info.getLivenessEvent()) {
                    case INITIALIZING : setStatus("Initializing", R.color.white);
                    case STARTED : setStatus("Determining liveness", R.color.white);
                    case TRACKING : setStatus("Look alive!", R.color.white);
                    case ANALYZING : {
                        vibrate();
                        setStatus("Analyzing", R.color.white);
                    }
                    case COMPLETED : setStatus("Done", R.color.white);
                    case RESET : setStatus("Looking for a face", R.color.white);
                }
            }
        }
    }


    private class EnrolResultListener implements FaceControllerProtocol.EnrolResultListener {
        @Override
        public void onEnrolResult(int errorCode, Result result, YUV collectedImage) {
            switch (errorCode) {
                case ErrorCodes.NO_ERROR : {
                    stopCameraPreview();
                    stopCapture();
                    setCheckMark();
                }
                case ErrorCodes.ERROR_ENROLL_QUALITY : {
                    onAuthenticateWait(false);
                    showMessage(R.string.face_quality, false);
                    retakePhotoDelayed();
                }
                default : {
                    onAuthenticateWait(false);
                    showMessage(R.string.face_enroll_failed, false);
                    retakePhotoDelayed();
                }
            }
        }
    }

    @Override
    protected int getCaptureFailedMessageId() {
        return R.string.face_enroll_failed;
    }

    @Override
    protected int getCaptureSuccessMessageId() {
        return R.string.face_enroll_complete;
    }

    @Override
    protected int getDoneButtonText() {
        return R.string.enroll_button;
    }

    @Override
    public void onFailure(AuthenticatorError error, LockResult result) {
        Log.d("DAON", "RegisterFaceFragment onFailure errorCode :" + error.getCode());
        switch (error.getCode()) {
            case FaceErrorCodes.FACE_LIVENESS_AT_REG_TIMEOUT : {
                showMessage(R.string.face_verify_timeout, false);
                setInstructions(R.string.face_verify_timeout, R.color.red);
                setInfoUpdate(false);
                onRecapture();
            }
            case FaceErrorCodes.FACE_LOST_FACE_CONTINUITY : {
                showMessage(R.string.face_tracking_lost, false);
                setInstructions(R.string.face_tracking_lost, R.color.red);
                setInfoUpdate(false);
                onRecapture();
            }
        }
    }
}
