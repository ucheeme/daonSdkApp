package com.daon.ps.demo.requests;

import static com.daon.ps.demo.Constants.BASIC_AUTH_URL;
import static com.daon.ps.demo.Constants.PASSWORD;
import static com.daon.ps.demo.Constants.USERNAME;
import android.util.Log;
import com.daon.ps.demo.api.NetworkUtils;
import com.daon.ps.demo.models.CreateAuthenticationRequest;
import com.daon.ps.demo.models.CreateAuthenticationResponse;


import java.io.IOException;

import retrofit2.Response;

public class PostCreateAuthenticationRequest {
    private static final String TAG = "PostCreateAuthenticationRequest";
    public static CreateAuthenticationResponse createAuthenticationRequest(CreateAuthenticationRequest createAuthReq) {
        try {
            Response<CreateAuthenticationResponse> response = NetworkUtils
                    .createAuthenticationRequestInstance(BASIC_AUTH_URL, USERNAME, PASSWORD)
                    .createAuthenticationRequest(createAuthReq)
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
