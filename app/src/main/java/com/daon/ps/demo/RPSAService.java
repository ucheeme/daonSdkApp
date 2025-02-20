package com.daon.ps.demo;


import android.content.Context;
import android.util.Log;
import com.daon.ps.demo.models.CreateAuthenticationRequest;
import com.daon.ps.demo.models.CreateAuthenticationResponse;
import com.daon.ps.demo.models.CreateUserAndRegChallengeRequest;
import com.daon.ps.demo.models.CreateUserAndRegChallengeResponse;
import com.daon.ps.demo.models.UpdateAuthenticationRequest;
import com.daon.ps.demo.models.UpdateAuthenticationResponse;
import com.daon.ps.demo.models.UpdateRegistrationChallengeRequest;
import com.daon.ps.demo.models.UpdateRegistrationChallengeResponse;
import com.daon.ps.demo.requests.PostCreateAuthenticationRequest;
import com.daon.ps.demo.requests.PostCreateUserAndRegChallenge;
import com.daon.ps.demo.requests.PostUpdateAuthenticationRequest;
import com.daon.ps.demo.requests.PostUpdateRegistrationChallenge;
public class RPSAService implements IRPSAService{

    @Override
    public CreateUserAndRegChallengeResponse createUserAndRegChallenge(CreateUserAndRegChallengeRequest createUserAndRegChallengeRequest) {

        CreateUserAndRegChallengeResponse createUserAndRegChallengeResp = PostCreateUserAndRegChallenge.createUser(createUserAndRegChallengeRequest);

        if(createUserAndRegChallengeResp == null){
            Log.d("CreateUserAndRegChallengeRequest", "null");
        }

        return createUserAndRegChallengeResp;
    }

    @Override
    public UpdateRegistrationChallengeResponse updateRegistrationChallenge(String uafMessage, String fidoRegRequestId, Context context) {
        UpdateRegistrationChallengeRequest registrationChallenge = new UpdateRegistrationChallengeRequest(uafMessage);

        UpdateRegistrationChallengeResponse updateRegChallengeResp = PostUpdateRegistrationChallenge
                .updateRegistrationChallenge(registrationChallenge,fidoRegRequestId);

        if(updateRegChallengeResp == null){
            Log.d("UpdateRegChallenge", "null");
        }

        return updateRegChallengeResp;
    }

    @Override
    public CreateAuthenticationResponse createAuthenticationRequest(CreateAuthenticationRequest authenticationRequest) {

        CreateAuthenticationResponse createAuthenticationRequestResp = PostCreateAuthenticationRequest.createAuthenticationRequest(authenticationRequest);

        if(createAuthenticationRequestResp == null){
            Log.d("CreateAuthRequest", "null");
        }

        return createAuthenticationRequestResp;
    }

    @Override
    public UpdateAuthenticationResponse updateAuthenticationRequest(UpdateAuthenticationRequest authRequest, String authRequestId) {

        UpdateAuthenticationResponse updateAuthenticationResponse = PostUpdateAuthenticationRequest.updateAuthentication(authRequest, authRequestId);

        if(updateAuthenticationResponse == null){
            Log.d("UpdateAuthRequest", "null");
        }

        return updateAuthenticationResponse;
    }


}
