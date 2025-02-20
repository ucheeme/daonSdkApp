package com.daon.ps.demo.api;

import com.daon.ps.demo.models.CreateUserAndRegChallengeResponse;
import com.daon.ps.demo.models.CreateUserAndRegChallengeRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface CreateUserAndRegChallengeService {

    @POST("IdentityXServices/rest/v1/registrationChallenges")
    Call<CreateUserAndRegChallengeResponse> createUserAndRegChallenge(@Body CreateUserAndRegChallengeRequest createUserAndRegChallengeRequest);
}
