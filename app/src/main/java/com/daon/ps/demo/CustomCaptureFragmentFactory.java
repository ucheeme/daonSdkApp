package com.daon.ps.demo;

import com.daon.sdk.authenticator.Authenticator;
import com.daon.sdk.authenticator.DefaultCaptureFragmentFactory;

public class CustomCaptureFragmentFactory extends DefaultCaptureFragmentFactory {

    public Class<?> getRegistrationFragment(com.daon.sdk.authenticator.Authenticator.Factor factor, com.daon.sdk.authenticator.Authenticator.Type type) {

        if (factor == com.daon.sdk.authenticator.Authenticator.Factor.FACE){

            return RegisterFaceFragment.class;
        }


        return super.getRegistrationFragment(factor, type);
    }

    public Class<?> getAuthenticationFragment(com.daon.sdk.authenticator.Authenticator.Factor factor, com.daon.sdk.authenticator.Authenticator.Type type) {
        if (factor == com.daon.sdk.authenticator.Authenticator.Factor.FACE)
            return AuthenticateFaceFragment.class;

        return super.getAuthenticationFragment(factor, type);
    }
}
