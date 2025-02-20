package com.daon.ps.demo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.daon.sdk.authenticator.ErrorCodes;
import com.daon.sdk.face.RecognitionResult;
import com.daon.sdk.face.YUV;
import com.daon.sdk.faceauthenticator.controller.FaceControllerProtocol;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuthenticateFaceFragment} factory method to
 * create an instance of this fragment.
 */
public class AuthenticateFaceFragment extends BaseFaceFragment {

    protected final Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        operationMode = OperationMode.AUTHENTICATION;
        return view;
    }

    @Override
    protected void done() {
        showMessage(R.string.face_verify, false);
        onAuthenticateWait(true);
        getController().authenticateImage(capturedImage, getCameraRotationDegrees(), new VerifyResultListener());
    }

    @Override
    public void onLivenessEvent(FaceControllerProtocol.LivenessEventInfo info) {
        if (!getController().getExpectedLivenessEvents().isEmpty()) {
            if (info.allLivenessEventsDetected()) {
                vibrate();
                stopCameraPreview();
                stopCapture();
                if (getController().isShowScore() || getController().isShowFace()) { //DELETE
                    setFragmentTerminationDelay(Delay.SHORT);
                    setParentActivityTerminationDelay(Delay.SHORT);
                }
                showMessage(R.string.face_verify, false);
                onAuthenticateWait(true);
                if (info.getImage() != null) {
                    getController().authenticateImage(info.getImage(), getCameraRotationDegrees(), new VerifyResultListener());
                } else {
                    showNoLivenessImageError();
                }
            } else {
                // More events
                Log.d("DAON-STATE", "state: " + info.getLivenessEvent());
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

    private void showNoLivenessImageError() {
        handler.post(() -> {
            if (getActivity() == null) return;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Image Error");
            builder.setMessage("LIveness event returned a null image. Cancelling the transaction.");

            builder.setPositiveButton(getResources().getString(R.string.alert_dialog_positive_button_text), (dialog, id) -> {
                dialog.cancel();
                cancel();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private class VerifyResultListener implements FaceControllerProtocol.VerifyResultListener {

        @Override
        public void onVerifyResult(int errorCode, RecognitionResult result, YUV collectedImage) {
            if (errorCode == ErrorCodes.NO_ERROR) {
                // Face recognized
                if (getController().isShowFace()) setPreviewImage(collectedImage.toGrayscale());
                else removePreview();

                if (getController().isShowScore()) {
                    @SuppressLint("StringFormatMatches") String msg = getString(R.string.face_verify_complete_with_score, result.getScore(), getController().getRecognitionThreshold());
                    showMessage(msg, false);
                } else {
                    if (getController().isShowFace()) {
                        showMessage(R.string.face_verify_complete, false);
                    }
                }
            } else {
                // Face not recognized
                if (getController().isShowScore()) {
                    @SuppressLint("StringFormatMatches") String msg = getString(R.string.face_verify_failed_with_score, result.getScore(), getController().getRecognitionThreshold());

                    showMessage(msg, false);
                }
            }
        }
    }

    @Override
    protected int getCaptureFailedMessageId() {
        return R.string.face_verify_failed;
    }

    @Override
    protected int getCaptureSuccessMessageId() {
        return R.string.face_verify_complete;
    }

    @Override
    protected int getCaptureWarningMessageId() {
        return R.string.face_verify_warning;
    }

    @Override
    protected int getDoneButtonText() {
        return R.string.authenticate_button;
    }
}
