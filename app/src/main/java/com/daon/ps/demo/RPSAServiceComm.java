package com.daon.ps.demo;

import static com.daon.ps.demo.Constants.APPLICATION_ID;
import static com.daon.ps.demo.Constants.AUTH_POLICY_ID;
import static com.daon.ps.demo.Constants.BASIC_AUTH_URL;
import static com.daon.ps.demo.Constants.PENDING;
import static com.daon.ps.demo.Constants.REG_POLICY_ID;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.daon.fido.client.sdk.ErrorInfo;
import com.daon.fido.client.sdk.IXUAFCommService;
import com.daon.fido.client.sdk.IXUAFCommServiceListener;
import com.daon.fido.client.sdk.ServerCommResult;
import com.daon.fido.client.sdk.model.Operation;
import com.daon.fido.client.sdk.model.UafProtocolMessageBase;
import com.daon.fido.client.sdk.uaf.UafMessageUtils;
import com.daon.fido.client.sdk.util.TaskExecutor;
import com.daon.ps.demo.api.ErrorCode;
import com.daon.ps.demo.models.Application;
import com.daon.ps.demo.models.CreateAuthenticationRequest;
import com.daon.ps.demo.models.CreateAuthenticationResponse;
import com.daon.ps.demo.models.CreateUserAndRegChallengeRequest;
import com.daon.ps.demo.models.CreateUserAndRegChallengeResponse;
import com.daon.ps.demo.models.Policy;
import com.daon.ps.demo.models.Registration;
import com.daon.ps.demo.models.UpdateAuthenticationRequest;
import com.daon.ps.demo.models.UpdateAuthenticationResponse;
import com.daon.ps.demo.models.UpdateRegistrationChallengeResponse;
import com.daon.ps.demo.models.User;

import java.util.UUID;

public class RPSAServiceComm implements IXUAFCommService {

    private final Context mContext;
    private final RPSAService rpsaService;
    private UpdateRegistrationChallengeTask mUpdateRegistrationChallengeTask = null;
    private CreateAuthenticationRequestTask mCreateAuthenticationRequestTask = null;
    private CreateUserAndRegChallengeTask mCreateUserAndRegChallengeTask = null;
    private UpdateAuthenticationRequestTask mUpdateAuthenticationRequestTask = null;
    private String mFidoRegistrationRequestId;
    private String mFidoAuthenticationRequestId;


    public static RPSAServiceComm getInstance(Context context) {
        return new RPSAServiceComm(new RPSAService(), context);
    }

    public RPSAServiceComm(RPSAService rpsaService, Context context) {
        this.mContext = context;
        this.rpsaService = rpsaService;
    }


    //first registration call
    @Override
    public void serviceRequestRegistrationWithUsername(String username, IXUAFCommServiceListener ixuafCommServiceListener) {
        mCreateUserAndRegChallengeTask = new CreateUserAndRegChallengeTask(ixuafCommServiceListener, mContext);
        mCreateUserAndRegChallengeTask.execute();
    }

    //third registration call
    @Override
    public void serviceRegister(String registrationResponse, IXUAFCommServiceListener ixuafCommServiceListener) {

        mUpdateRegistrationChallengeTask = new UpdateRegistrationChallengeTask(registrationResponse,mFidoRegistrationRequestId,ixuafCommServiceListener);
        mUpdateRegistrationChallengeTask.execute();
    }


    //First Authentication Call
    @Override
    public void serviceRequestAuthenticationWithParams(Bundle bundle, IXUAFCommServiceListener ixuafCommServiceListener) {
        mFidoAuthenticationRequestId = null;
        mCreateAuthenticationRequestTask = new CreateAuthenticationRequestTask(ixuafCommServiceListener,mContext);
        mCreateAuthenticationRequestTask.execute();
    }


    //3rd authentication call
    @Override
    public void serviceAuthenticate(String singleShotInfo, String uafMessage, String s2, IXUAFCommServiceListener ixuafCommServiceListener) {
        mUpdateAuthenticationRequestTask = new UpdateAuthenticationRequestTask(uafMessage,ixuafCommServiceListener);
        mUpdateAuthenticationRequestTask.execute();

    }


    @Override
    public void serviceUpdate(String authenticationResponse, String username, IXUAFCommServiceListener ixuafCommServiceListener) {

        UafProtocolMessageBase[] uafRequests = UafMessageUtils.validateUafMessage(mContext, authenticationResponse, UafMessageUtils.OpDirection.Response, null);

        if(uafRequests[0].header.op == Operation.Reg){
            //second registration call
            mUpdateRegistrationChallengeTask = new UpdateRegistrationChallengeTask(authenticationResponse,mFidoRegistrationRequestId,ixuafCommServiceListener);
            mUpdateRegistrationChallengeTask.execute();
        }else{
            //second authentication call
            mUpdateAuthenticationRequestTask = new UpdateAuthenticationRequestTask(authenticationResponse,ixuafCommServiceListener);
            mUpdateAuthenticationRequestTask.execute();
        }
    }
    @Override
    public void serviceRequestRegistrationPolicy(IXUAFCommServiceListener ixuafCommServiceListener) {

    }

    @Override
    public void serviceRequestDeregistration(String s, IXUAFCommServiceListener ixuafCommServiceListener) {

    }

    @Override
    public void serviceSubmitFailedAuthData(Bundle bundle, IXUAFCommServiceListener ixuafCommServiceListener) {

    }

    public class UpdateRegistrationChallengeTask extends TaskExecutor<ServerCommResult> {

        private final String mUafMessage;
        private final String mFidoRegRequestId;
        private final IXUAFCommServiceListener mIxuafCommServiceListener;

        UpdateRegistrationChallengeTask(String authenticatorResponse, String fidoRegRequestId, IXUAFCommServiceListener ixuafCommServiceListener) {
            mUafMessage = authenticatorResponse;
            mFidoRegRequestId = fidoRegRequestId;
            mIxuafCommServiceListener = ixuafCommServiceListener;
        }

        @Override
        protected ServerCommResult doInBackground() {
            ServerCommResult result = new ServerCommResult();
            UpdateRegistrationChallengeResponse response = rpsaService.updateRegistrationChallenge(this.mUafMessage, this.mFidoRegRequestId, mContext);

            if(response == null){
                return new ServerCommResult(new ErrorInfo(ErrorCode.UNKNOWN.getValue(), "Register failed"));
            }
            if(response.getFidoResponseCode() == 1200){
                result.setResponse(response.getFidoRegistrationResponse());
                result.setResponseCode((short) response.getFidoResponseCode());
                result.setResponseMessage(response.getFidoResponseMsg());
            }else{
                result = new ServerCommResult(new ErrorInfo(response.getFidoResponseCode(), response.getFidoResponseMsg()));
            }

            return result;
        }

        @Override
        protected void onPostExecute(ServerCommResult result) {
            mUpdateRegistrationChallengeTask = null;
            mIxuafCommServiceListener.onComplete(result);
        }

        @Override
        protected void onCancelled() {
            mUpdateRegistrationChallengeTask = null;
        }
    }


    public class CreateUserAndRegChallengeTask extends TaskExecutor<ServerCommResult> {

        private final IXUAFCommServiceListener mIxuafCommServiceListener;
        private final Context mContext;

        UUID uuid = UUID.randomUUID();
        String serverData = uuid.toString();
        String registrationId = uuid.toString();
        User user = new User(uuid.toString());
        Application application = new Application(BASIC_AUTH_URL+"IdentityXServices/rest/v1/applications/"+APPLICATION_ID);
        Policy policy = new Policy(BASIC_AUTH_URL+"IdentityXServices/rest/v1/policies/"+REG_POLICY_ID,application);
        Registration registration = new Registration(registrationId,user,application);

        CreateUserAndRegChallengeRequest createUser = new CreateUserAndRegChallengeRequest(serverData, registration, policy);

        CreateUserAndRegChallengeTask(IXUAFCommServiceListener ixuafCommServiceListener, Context context) {
            mIxuafCommServiceListener = ixuafCommServiceListener;
            mContext = context;
        }

        @Override
        protected ServerCommResult doInBackground() {
            ServerCommResult result = new ServerCommResult();
            CreateUserAndRegChallengeResponse response = rpsaService.createUserAndRegChallenge(createUser);

            if(response == null){
                return new ServerCommResult(new ErrorInfo(ErrorCode.UNKNOWN.getValue(), "Registration failed"));
            }
            if(response.getStatus().equalsIgnoreCase(PENDING)){
                result.setResponse(response.getFidoRegistrationRequest());
                mFidoRegistrationRequestId = response.getId();
                CoreApplication.setUserHref(mContext, response.getRegistration().getUser().getHref());
                CoreApplication.setRegHref(mContext, response.getRegistration().getHref());
                CoreApplication.setUsername(mContext, response.getRegistration().getRegistrationId());

            }else{
                result = new ServerCommResult(new ErrorInfo(ErrorCode.UNKNOWN.getValue(), "Registration Failed"));
            }

            return result;
        }

        @Override
        protected void onPostExecute(ServerCommResult result) {
            mCreateUserAndRegChallengeTask = null;
            mIxuafCommServiceListener.onComplete(result);
        }

        @Override
        protected void onCancelled() {
            mUpdateRegistrationChallengeTask = null;
        }
    }

    public class CreateAuthenticationRequestTask extends TaskExecutor<CreateAuthenticationResponse> {

        private final IXUAFCommServiceListener mIxuafCommServiceListener;
        private final Context mContext;


        CreateAuthenticationRequestTask(IXUAFCommServiceListener ixuafCommServiceListener, Context context) {
            mIxuafCommServiceListener = ixuafCommServiceListener;
            mContext = context;
        }


        @Override
        protected CreateAuthenticationResponse doInBackground() {
            Application application = new Application(BASIC_AUTH_URL+"IdentityXServices/rest/v1/applications/"+APPLICATION_ID);
            Policy policy = new Policy(BASIC_AUTH_URL+"IdentityXServices/rest/v1/policies/"+AUTH_POLICY_ID);
            CreateAuthenticationRequest authRequest = new CreateAuthenticationRequest(application,policy, new User("",CoreApplication.getUserHref(mContext,"")),"Authentication","FI");

            return rpsaService.createAuthenticationRequest(authRequest);
        }

        @Override
        protected void onPostExecute(CreateAuthenticationResponse result) {
            mCreateAuthenticationRequestTask = null;
            if(result != null){
                mFidoAuthenticationRequestId = result.getId();
                mIxuafCommServiceListener.onComplete(new ServerCommResult(result.getFidoAuthenticationRequest()));
            }else{
                Log.d("AuthFailed", "Failed to request authentication");
                mIxuafCommServiceListener.onComplete(new ServerCommResult(new ErrorInfo(ErrorCode.UNKNOWN.getValue(), "Authentication Failed")));
            }
        }

        @Override
        protected void onCancelled() {
            mCreateAuthenticationRequestTask = null;
        }

    }


    public class UpdateAuthenticationRequestTask extends TaskExecutor<ServerCommResult> {

        private final String mAuthenticationResponse;
        private final IXUAFCommServiceListener mIxuafCommServiceListener;

        UpdateAuthenticationRequestTask(String authenticationResponse, IXUAFCommServiceListener ixuafCommServiceListener) {
            mAuthenticationResponse = authenticationResponse;
            mIxuafCommServiceListener = ixuafCommServiceListener;
        }


        @Override
        protected ServerCommResult doInBackground() {
            UpdateAuthenticationRequest authRequest = new UpdateAuthenticationRequest(mAuthenticationResponse);

            ServerCommResult result = new ServerCommResult();
            UpdateAuthenticationResponse response = rpsaService.updateAuthenticationRequest(authRequest, mFidoAuthenticationRequestId);

            if(response == null){
                return new ServerCommResult(new ErrorInfo(ErrorCode.UNKNOWN.getValue(), "Authentication failed"));
            }
            if(response.getFidoResponseCode() == 1200){
                result.setResponse(response.getFidoAuthenticationResponse());
                result.setResponseCode((short) response.getFidoResponseCode());
                result.setResponseMessage(response.getFidoResponseMsg());
            }else{
                result = new ServerCommResult(new ErrorInfo(response.getFidoResponseCode(), response.getFidoResponseMsg()));
            }

            return result;
        }

        @Override
        protected void onPostExecute(ServerCommResult result) {
            mUpdateAuthenticationRequestTask = null;
            mIxuafCommServiceListener.onComplete(result);

        }

        @Override
        protected void onCancelled() {
            mUpdateAuthenticationRequestTask = null;
        }

    }
}
