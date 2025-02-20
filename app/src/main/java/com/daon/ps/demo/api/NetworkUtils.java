package com.daon.ps.demo.api;

import com.daon.ps.demo.Constants;

public class NetworkUtils {

    private static UpdateRegistrationChallengeService updateRegistrationChallengeService;
    private static CreateUserAndRegChallengeService createUserAndRegChallengeService;
    private static CreateAuthenticationRequestService createAuthenticationRequestService;
    private static UpdateAuthenticationRequestService updateAuthenticationRequestService;


    public static UpdateRegistrationChallengeService updateRegistrationChallengeInstance(String url, String username, String password){

        if (updateRegistrationChallengeService == null)
            updateRegistrationChallengeService = RetrofitAdapter.getBasicAuthInstance(url,username, password).create(UpdateRegistrationChallengeService.class);

        return updateRegistrationChallengeService;
    }

    public static CreateUserAndRegChallengeService createUserAndRegChallengeInstance(String url, String username, String password){

        if (createUserAndRegChallengeService == null)
            createUserAndRegChallengeService = RetrofitAdapter.getBasicAuthInstance(url,username, password).create(CreateUserAndRegChallengeService.class);

        return createUserAndRegChallengeService;
    }

    public static CreateAuthenticationRequestService createAuthenticationRequestInstance(String url, String username, String password){

        if (createAuthenticationRequestService == null)
            createAuthenticationRequestService = RetrofitAdapter.getBasicAuthInstance(url,username, password).create(CreateAuthenticationRequestService.class);

        return createAuthenticationRequestService;
    }

    public static UpdateAuthenticationRequestService updateAuthenticationRequestInstance(String url, String username, String password){

        if (updateAuthenticationRequestService == null)
            updateAuthenticationRequestService = RetrofitAdapter.getBasicAuthInstance(url,username, password).create(UpdateAuthenticationRequestService.class);

        return updateAuthenticationRequestService;
    }

}
