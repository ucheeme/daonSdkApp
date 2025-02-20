package com.daon.ps.demo.api;

import com.daon.ps.demo.models.UpdateAuthenticationRequest;
import com.daon.ps.demo.models.UpdateAuthenticationResponse;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UpdateAuthenticationRequestService {

    @POST("IdentityXServices/rest/v1/authenticationRequests/{fidoAuthenticationRequestId}")
    Call<UpdateAuthenticationResponse> updateAuthenticationRequest(@Path("fidoAuthenticationRequestId")String fidoAuthenticationRequestId,
                                                                   @Body UpdateAuthenticationRequest updateAuthenticationRequest);
}
