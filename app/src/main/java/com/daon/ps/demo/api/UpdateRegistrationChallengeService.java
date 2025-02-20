package com.daon.ps.demo.api;

import com.daon.ps.demo.models.UpdateRegistrationChallengeRequest;
import com.daon.ps.demo.models.UpdateRegistrationChallengeResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UpdateRegistrationChallengeService {

    @POST("IdentityXServices/rest/v1/registrationChallenges/{fidoRegistrationRequestId}")
    Call<UpdateRegistrationChallengeResponse> updateRegistrationChallenge(@Path ("fidoRegistrationRequestId")String fidoRegistrationRequestId,
                                                                          @Body UpdateRegistrationChallengeRequest registrationChallenge);
}
