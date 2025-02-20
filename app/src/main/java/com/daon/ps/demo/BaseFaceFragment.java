package com.daon.ps.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.util.Size;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.daon.sdk.authenticator.Authenticator;
import com.daon.sdk.authenticator.ErrorCodes;
import com.daon.sdk.authenticator.controller.AuthenticatorError;
import com.daon.sdk.authenticator.controller.LockResult;
import com.daon.sdk.device.IXAErrorCodes;
import com.daon.sdk.face.LivenessResult;
import com.daon.sdk.face.QualityResult;
import com.daon.sdk.face.Result;
import com.daon.sdk.face.YUV;
import com.daon.sdk.faceauthenticator.FaceErrorCodes;
import com.daon.sdk.faceauthenticator.YUVTools;
import com.daon.sdk.faceauthenticator.controller.FaceControllerProtocol;


public abstract class BaseFaceFragment extends BaseCaptureFragment implements FaceControllerProtocol.FaceAnalysisListener {
    private static final int RESUME_PROCESSING_DELAY_MILLIS = 500;

    protected enum PhotoMode {DETECT, TAKE, RETAKE}

    protected enum OperationMode {REGISTRATION, AUTHENTICATION}

    protected OperationMode operationMode;

    protected PhotoMode photoMode = PhotoMode.DETECT;

    protected Button takePhotoButton;
    private Button doneButton;
    protected YUV capturedImage;

    private ImageView photo;
    private ImageView check;

    private ImageView overlay = null;
    private TextView instructions;
    private TextView status;
    private TextView warningTextView;
    private TextView maskWarning;
    private ExplicitPermission explicitCameraPermission = null;
    private boolean cameraPermissionGranted;
    private boolean enableInfoText = true;

    private CameraXWrapper cameraXWrapper;
    private ViewGroup layout;

    public FaceControllerProtocol getController() {
        return (FaceControllerProtocol) super.getController();
    }

    public void setController(FaceControllerProtocol controller) {
        super.setController(controller);
    }

    /**
     * @return number of degrees clockwise from portrait (0 degrees) that  the camera image is rotated
     */
    protected int getCameraRotationDegrees() {
        return YUVTools.mirroredAngle(cameraXWrapper.getDegreesToRotate());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daon_face, container, false);
        takePhotoButton = view.findViewById(R.id.takePhotoButton);
        doneButton = view.findViewById(R.id.doneButton);
        takePhotoButton.setOnClickListener(v -> {
            if (photoMode == PhotoMode.TAKE) {
                takePhoto();
            } else {
                retakePhoto();
            }

        });
        doneButton.setText(getDoneButtonText());
        doneButton.setOnClickListener(v -> {
            if (doneButton != null) doneButton.setVisibility(View.GONE);
            if (takePhotoButton != null) takePhotoButton.setVisibility(View.INVISIBLE);
            done();
        });

        return view;
    }

    private void takePhoto() {
        stopCapture();
        stopCameraPreview();
        hideInfo();
        enableOverlay(false);
        photoMode = PhotoMode.RETAKE;
        if (takePhotoButton != null) takePhotoButton.setText(R.string.photo_retake);
        if (doneButton != null) doneButton.setVisibility(View.VISIBLE);

        if (getController().isLivenessEnabled()) {
            if (getController() != null) {
                capturedImage = getController().captureImage();
            }
        }
        setPreviewImage(capturedImage.toBitmap());
    }

    private void retakePhoto() {
        if (operationMode == OperationMode.REGISTRATION) {
            getController().resumeRegistrationProcessing();
        } else {
            getController().resumeAuthenticationProcessing();
        }

        hideInfo();
        setPreviewFrameCapture(false);

        startCameraPreview();
        enableOverlay(true);
        setInfoUpdate(true);

        capturedImage = null;
        photoMode = PhotoMode.DETECT;

        if (takePhotoButton != null) {
            takePhotoButton.setVisibility(View.INVISIBLE);
            takePhotoButton.setText(R.string.photo_take);
        }

        if (doneButton != null) doneButton.setVisibility(View.GONE);
        if (photo != null) photo.setImageDrawable(null);
        if (check != null) check.setImageDrawable(null);
    }

    protected void setPreviewImage(Bitmap bmp) {

        setPreviewFrameCapture(false);

        if (getActivity() != null) {
            ViewGroup layout = getActivity().findViewById(R.id.preview);
            if (layout != null) {

                photo = new ImageView(getActivity());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(layout.getLayoutParams().width, layout.getLayoutParams().height);
                photo.setLayoutParams(params);
                photo.setImageBitmap(BitmapUtil.rotate(bmp, cameraXWrapper.getDegreesToRotate(), true));
                check = new ImageView(getActivity());
                params = new FrameLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END);
                check.setLayoutParams(params);
                check.setImageResource(R.mipmap.verified);

                layout.addView(photo);
                layout.addView(check);
            }
        }
    }

    @Override
    protected void onRecapture() {
        stopCameraPreview();
        stopCapture();
        hideInfo();
        enableOverlay(false);
        retakePhotoDelayed();
    }

    protected void retakePhotoDelayed() {
        new Handler().postDelayed(this::retakePhoto, RESUME_PROCESSING_DELAY_MILLIS);
    }

    @Override
    public void onResume() {
        super.onResume();
        enableOverlay(true);
        setInfoUpdate(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideInfo();
        enableOverlay(false);
    }

    @Override
    protected void onPermissionResult(Boolean result) {
        if (result) {
            explicitCameraPermission = ExplicitPermission.GRANTED;
            cameraPermissionGranted = true;
        } else {
            explicitCameraPermission = ExplicitPermission.DENIED;
            completeCaptureWithError(new AuthenticatorError(IXAErrorCodes.ERROR_HW_UNAVAILABLE, getString(R.string.face_camera_access_denied)));
        }
    }

    @Override
    protected void start() {
        super.start();
        if (explicitCameraPermission == null) {
            if (checkPermissions(Manifest.permission.CAMERA)) {
                cameraPermissionGranted = true;
            }
        } else {
            if (explicitCameraPermission != ExplicitPermission.GRANTED) {
                if (getController() != null) {
                    getController().completeCaptureWithError(new AuthenticatorError(IXAErrorCodes.ERROR_HW_UNAVAILABLE, getString(R.string.face_camera_access_denied)));
                }
            }
        }

        if (cameraPermissionGranted) {
            startCameraPreview();
        }
    }

    @Override
    protected void stop() {
        super.stop();
        stopCameraPreview();
        stopCapture();
    }

    /**********************************
     * FaceAnalysisListener callbacks
     *********************************/

    @Override
    public void onImageAnalyzed(YUV yuv, Result result, boolean isQualityImage) {

        boolean noErrorCheck = checkAndUpdateQualityInfo(result);

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (result.isDeviceUpright() && isQualityImage && noErrorCheck) {
                    GradientDrawable drawable = (GradientDrawable) layout.getBackground();
                    drawable.setColor(Color.GREEN);
                } else {
                    GradientDrawable drawable = (GradientDrawable) layout.getBackground();
                    drawable.setColor(Color.RED);
                }
            });
        }
        if (!enableInfoText) return;

        if (!getController().isLivenessEnabled()) {
            if (takePhotoButton != null) {
                takePhotoButton.setVisibility((noErrorCheck && isQualityImage) ? View.VISIBLE : View.INVISIBLE);
            }
            if (noErrorCheck && isQualityImage) {
                capturedImage = yuv;
                photoMode = PhotoMode.TAKE;
                hideInfo();
                hideWarning();
            }
        } else {
            if (noErrorCheck) {
                updateInfo(result);
            }
        }
    }

    @Override
    public void onAlert(int alert) {
    }

    protected int getAlertMessage(int alert) {
        int result = -1;
        switch (alert) {
            case LivenessResult.ALERT_FACE_NOT_DETECTED:
                result = R.string.face_liveness_hmd_face_not_detected;
                break;
            case LivenessResult.ALERT_FACE_NOT_CENTERED:
                result = R.string.face_liveness_hmd_face_not_centered;
                break;
            case LivenessResult.ALERT_MOTION_TOO_FAST:
                result = R.string.face_liveness_hmd_motion_too_fast;
                break;
            case LivenessResult.ALERT_MOTION_SWING_TOO_FAST:
                result = R.string.face_liveness_hmd_motion_swing_too_fast;
                break;
            case LivenessResult.ALERT_MOTION_TOO_FAR:
                result = R.string.face_liveness_hmd_motion_too_far;
                break;
            case LivenessResult.ALERT_FACE_TOO_CLOSE_TO_EDGE:
                result = R.string.face_liveness_hmd_too_close_to_edge;
                break;
            case LivenessResult.ALERT_FACE_TOO_NEAR:
                result = R.string.face_liveness_hmd_too_near;
                break;
            case LivenessResult.ALERT_FACE_TOO_FAR:
                result = R.string.face_liveness_hmd_too_far;
                break;
            case LivenessResult.ALERT_LIVENESS_SPOOF:
                result = R.string.face_liveness_hmd_spoof;
                break;
            case LivenessResult.ALERT_INSUFFICIENT_FACE_DATA:
                result = R.string.face_liveness_hmd_insufficient_face_data;
                break;
            case LivenessResult.ALERT_INSUFFICIENT_FRAME_DATA:
                result = R.string.face_liveness_hmd_insufficient_frame_data;
                break;
            case LivenessResult.ALERT_FRAME_MISMATCH:
                result = R.string.face_liveness_hmd_frame_mismatch;
                break;
            case LivenessResult.ALERT_NO_MOVEMENT_DETECTED:
                result = R.string.face_liveness_hmd_no_movement_detected;
                break;
            case LivenessResult.ALERT_FACE_QUALITY:
                result = R.string.face_liveness_hmd_quality;
                break;
            case LivenessResult.ALERT_TIMEOUT:
                result = R.string.face_liveness_timeout;
                break;
            case LivenessResult.ALERT_PERFORMANCE:
                result = R.string.face_liveness_performance;
                break;
        }

        return result;
    }


    @Override
    public void onFailure(AuthenticatorError error, LockResult result) {
        Log.d("DAON", "BaseFace onFailure :" + error.getCode());
        stopCameraPreview();
        stopCapture();
        switch (error.getCode()) {
            case ErrorCodes.ERROR_MAX_ATTEMPTS:
                terminateParentActivityWithError(error.getCode(), error.getMessage());
            case FaceErrorCodes.FACE_LIVENESS_AT_AUTH_TIMEOUT:
            case FaceErrorCodes.FACE_REC_TIMEOUT:
            case FaceErrorCodes.FACE_VERIFY_TIMEOUT_NO_FACE_FOUND:
                showMessage(R.string.face_verify_timeout, false);
                setInfoUpdate(false);
                setInstructions(R.string.face_verify_timeout, R.color.red);
                if (result != null) {
                    if (result.getLockInfo().getState() == Authenticator.Lock.UNLOCKED) {
                        onRecapture();
                    } else {
                        stopCameraPreview();
                        stopCapture();
                        terminateParentActivityWithError(error.getCode(), error.getMessage());
                    }
                } else {
                    stopCameraPreview();
                    stopCapture();
                    terminateParentActivityWithError(error.getCode(), error.getMessage());
                }
                break;
            case FaceErrorCodes.FACE_LOST_FACE_CONTINUITY:
                stopCameraPreview();
                stopCapture();
                showMessage(error.getMessage(), false);
                terminateParentActivityWithError(error.getCode(), error.getMessage());
                break;
        }
    }

    /****************************************
     * End of FaceAnalysisListener callbacks
     ***************************************/

    private void startCameraPreview() {

        if (getActivity() != null) {
            layout = getActivity().findViewById(R.id.preview);
            if (layout != null) {
                FrameLayout cameraOverlayLayout = new FrameLayout(getActivity());
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                cameraOverlayLayout.setLayoutParams(layoutParams);

                PreviewView previewView = getActivity().findViewById(R.id.view_finder);
                cameraXWrapper = new CameraXWrapper(previewView, getContext(), this);

                int res = getResources().getIdentifier(getActivity().getPackageName() + ":drawable/preview_overlay", null, null);
                if (res > 0) {
                    overlay = new ImageView(getActivity());
                    FrameLayout.LayoutParams faceCaptureOverlayLayoutParams = new FrameLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, Gravity.CENTER);
                    overlay.setLayoutParams(faceCaptureOverlayLayoutParams);
                    overlay.setBackgroundResource(res);
                    cameraOverlayLayout.addView(overlay);
                }

                layout.addView(cameraOverlayLayout);
                layout.setVisibility(View.VISIBLE);
            }
            warningTextView = getActivity().findViewById(R.id.warning);
            status = getActivity().findViewById(R.id.status);
            instructions = getActivity().findViewById(R.id.instruction);
            maskWarning = getActivity().findViewById(R.id.maskWarning);
        }

        // Start camera preview
        Size size;
        if (getController().getPassiveLivenessEvent() == FaceControllerProtocol.LivenessEvent.SERVER && getController().getType() == Authenticator.Type.ADOS) {
            size = new Size(1280, 720);
        } else {
            size = new Size(640, 480);
        }
        cameraXWrapper.startPreview(size);
        getController().startFaceCapture(size.getWidth(), size.getHeight(), this, new DefaultCaptureCompleteListener());


        // Set preview frame callback and start collecting frames.
        // Frames are in the NV21 format YUV encoding.
        //500ms delay ,to make sure the camera is ready
        new Handler().postDelayed(() -> setPreviewFrameCapture(true), 500);
    }

    private void setPreviewFrameCapture(boolean on) {

        if (cameraXWrapper == null) return;

        if (!on) {
            cameraXWrapper.setPreviewCallback(null);
        } else {

            cameraXWrapper.setPreviewCallback(yuv -> {
                if (getController() != null) {
                    getController().analyzeImage(yuv);
                }
            });
        }

    }

    protected void stopCameraPreview() {
        if (cameraXWrapper != null) cameraXWrapper.stopPreview();
        setPreviewFrameCapture(false);
    }

    protected void stopCapture() {
        if (getController() != null) {
            getController().stopFaceCapture();
        }
    }

    protected void setInstructions(final int resId, final int color) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (instructions != null && resId > 0) {
                    instructions.setText(resId);
                    instructions.setTextColor(getResources().getColor(color));
                    instructions.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    protected void setStatus(String message, final int color) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (status != null) {
                    status.setText(message);
                    status.setTextColor(getResources().getColor(color));
                    status.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    protected void vibrate() {
        Activity activity = getActivity();
        if (activity != null) {
            Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) vibrator.vibrate(200);
        }
    }


    private void setWarning(int resId) {
        if (resId < 0) {
            hideWarning();
            return;
        }
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (warningTextView != null) {
                    warningTextView.setText(resId);
                    warningTextView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void setMaskWarning(int resId) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (maskWarning != null) {
                    if (resId < 0) {
                        maskWarning.setVisibility(View.INVISIBLE);
                    } else {
                        maskWarning.setText(resId);
                        maskWarning.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    protected void hideWarning() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (warningTextView != null) {
                    warningTextView.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    protected void setInfoUpdate(boolean enable) {
        enableInfoText = enable;
    }

    protected void hideInfo() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (instructions != null) {
                    instructions.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void enableOverlay(boolean enable) {
        if (overlay != null) overlay.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    private void updateInfo(final Result result) {
        if (result.isTrackingFace()) {
            if (getController().getExpectedLivenessEvents().contains(FaceControllerProtocol.LivenessEvent.BLINK)) {
                if (getController().getExpectedLivenessEvents().contains(FaceControllerProtocol.LivenessEvent.PASSIVE)) {
                    if (getController().getDetectedLivenessEvents().contains(FaceControllerProtocol.LivenessEvent.BLINK)) {
                        if (getController().getDetectedLivenessEvents().contains(FaceControllerProtocol.LivenessEvent.PASSIVE)) {
                            setInstructions(R.string.photo_info, R.color.white);
                        } else {
                            setInstructions(R.string.face_blink_detected_liveness_not_detected, R.color.white);
                        }
                    } else {
                        if (getController().getDetectedLivenessEvents().contains(FaceControllerProtocol.LivenessEvent.PASSIVE)) {
                            setInstructions(R.string.face_blink_not_detected_liveness_detected, R.color.white);
                        } else {
                            setInstructions(R.string.face_blink_liveness_not_detected, R.color.white);
                        }
                    }
                } else {
                    if (getController().getDetectedLivenessEvents().contains(FaceControllerProtocol.LivenessEvent.BLINK)) {
                        setInstructions(R.string.photo_info, R.color.white);
                    } else {
                        setInstructions(R.string.face_blink_not_detected, R.color.white);
                    }
                }
            } else {
                if (getController().getDetectedLivenessEvents().contains(FaceControllerProtocol.LivenessEvent.PASSIVE)) {
                    setInstructions(R.string.photo_info, R.color.white);
                } else {
                    setInstructions(R.string.face_liveness_not_detected, R.color.white);
                }
            }
        }
    }


    private boolean checkAndUpdateQualityInfo(Result result) {

        Rect corners = new Rect();
        overlay.getLocalVisibleRect(corners);
        QualityResult quality = result.getQualityResult();

        if (getAlertMessage(result.getLivenessResult().getAlert()) > 0) {
            setWarning(getAlertMessage(result.getLivenessResult().getAlert()));
        } else if (quality.hasMask()) {
            setMaskWarning(R.string.face_quality_mask_detected);
        } else if (!quality.isFaceCentered()) {
            setWarning(R.string.face_not_centered);
        } else if (!quality.hasAcceptableEyeDistance()) {
            setWarning(R.string.move_closer);
        } else if (!result.getQualityResult().hasAcceptableQuality()) {
            boolean goodLighting = quality.hasAcceptableExposure() && quality.hasUniformLighting() && quality.hasAcceptableGrayscaleDensity() && quality.hasFace();
            setWarning((goodLighting) ? R.string.face_quality_unknown : R.string.face_quality_non_uniform_lighting);
        } else {
            // No quality errors
            setWarning(-1);
            setMaskWarning(-1);
            return true;
        }
        return false;
    }

    protected void removePreview() {
        setPreviewFrameCapture(false);
        if (photo != null) photo.setImageDrawable(null);
        if (check != null) check.setImageDrawable(null);
    }

    protected void setCheckMark() {
        if (getActivity() != null) {
            ViewGroup layout = getActivity().findViewById(R.id.preview);
            if (layout != null) {
                ImageView check = new ImageView(getActivity());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END);
                check.setLayoutParams(params);
                check.setImageResource(R.mipmap.verified);
                layout.addView(check);
            }
        }
    }

    protected abstract void done();

    protected abstract int getDoneButtonText();

}
