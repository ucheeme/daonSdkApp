package com.daon.ps.demo.requests;

import static com.daon.ps.demo.Constants.BASIC_AUTH_URL;
import static com.daon.ps.demo.Constants.PASSWORD;
import static com.daon.ps.demo.Constants.USERNAME;

import android.util.Log;

import com.daon.ps.demo.api.NetworkUtils;
import com.daon.ps.demo.models.CreateUserAndRegChallengeRequest;
import com.daon.ps.demo.models.CreateUserAndRegChallengeResponse;

import java.io.IOException;

import retrofit2.Response;

public class PostCreateUserAndRegChallenge {
    private static final String TAG = "PostCreateUserAndRegChallenge";
    public static CreateUserAndRegChallengeResponse createUser(CreateUserAndRegChallengeRequest createUserAndRegChallengeRequest) {
        try {
            Response<CreateUserAndRegChallengeResponse> response = NetworkUtils
                    .createUserAndRegChallengeInstance(BASIC_AUTH_URL, USERNAME, PASSWORD)
                    .createUserAndRegChallenge(createUserAndRegChallengeRequest)
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




