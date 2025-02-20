package com.daon.ps.demo.api;

import com.daon.ps.demo.models.CreateAuthenticationRequest;
import com.daon.ps.demo.models.CreateAuthenticationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface CreateAuthenticationRequestService {

    @POST("IdentityXServices/rest/v1/authenticationRequests")
    Call<CreateAuthenticationResponse> createAuthenticationRequest(@Body CreateAuthenticationRequest authRequest);
}
