package com.daon.ps.demo;

import android.content.Context;
import com.daon.ps.demo.models.CreateAuthenticationRequest;
import com.daon.ps.demo.models.CreateAuthenticationResponse;
import com.daon.ps.demo.models.CreateUserAndRegChallengeResponse;
import com.daon.ps.demo.models.UpdateAuthenticationRequest;
import com.daon.ps.demo.models.UpdateAuthenticationResponse;
import com.daon.ps.demo.models.UpdateRegistrationChallengeResponse;
import com.daon.ps.demo.models.CreateUserAndRegChallengeRequest;

public interface IRPSAService {

    UpdateRegistrationChallengeResponse updateRegistrationChallenge(String uafMessage, String fidoRegRequestId, Context context);
    CreateUserAndRegChallengeResponse createUserAndRegChallenge(CreateUserAndRegChallengeRequest createUserAndRegChallengeRequest);
    CreateAuthenticationResponse createAuthenticationRequest(CreateAuthenticationRequest authenticationRequest);
    UpdateAuthenticationResponse updateAuthenticationRequest(UpdateAuthenticationRequest authRequest, String authRequestId);
}
