package com.daon.ps.demo.requests;

import static com.daon.ps.demo.Constants.BASIC_AUTH_URL;
import static com.daon.ps.demo.Constants.PASSWORD;
import static com.daon.ps.demo.Constants.USERNAME;

import android.util.Log;

import com.daon.ps.demo.api.NetworkUtils;
import com.daon.ps.demo.models.UpdateAuthenticationRequest;
import com.daon.ps.demo.models.UpdateAuthenticationResponse;

import java.io.IOException;

import retrofit2.Response;

public class PostUpdateAuthenticationRequest {
    private static final String TAG = "PostUpdateAuthenticationRequest";
    public static UpdateAuthenticationResponse updateAuthentication(UpdateAuthenticationRequest authBody, String authRequestId) {
        try {
            Response<UpdateAuthenticationResponse> response = NetworkUtils.updateAuthenticationRequestInstance(BASIC_AUTH_URL, USERNAME, PASSWORD)
                    .updateAuthenticationRequest(authRequestId,authBody)
                    .execute();
            return response.body();

        } catch (IOException e) {
            String message = e.getMessage();
            if (message != null) {
                Log.d(TAG, message);
            }
            return null;
        }
    }
}
