package com.daon.ps.demo.requests;

import static com.daon.ps.demo.Constants.BASIC_AUTH_URL;
import static com.daon.ps.demo.Constants.PASSWORD;
import static com.daon.ps.demo.Constants.USERNAME;

import android.util.Log;

import com.daon.ps.demo.api.NetworkUtils;
import com.daon.ps.demo.models.UpdateRegistrationChallengeRequest;
import com.daon.ps.demo.models.UpdateRegistrationChallengeResponse;

import java.io.IOException;

import retrofit2.Response;

public class PostUpdateRegistrationChallenge {
    private static final String TAG = "PostUpdateRegistrationChallenge";
    public static UpdateRegistrationChallengeResponse updateRegistrationChallenge(UpdateRegistrationChallengeRequest registrationChallenge, String fidoRegReqId) {
        try {
            Response<UpdateRegistrationChallengeResponse> response = NetworkUtils
                    .updateRegistrationChallengeInstance(BASIC_AUTH_URL, USERNAME, PASSWORD)
                    .updateRegistrationChallenge(fidoRegReqId, registrationChallenge)
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
