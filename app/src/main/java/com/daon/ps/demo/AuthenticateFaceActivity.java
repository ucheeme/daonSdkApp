package com.daon.ps.demo;
import android.os.Bundle;
import android.widget.Toast;


public class AuthenticateFaceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_face);

        if(discover()){
            getFido().authenticate(null, new AuthEventListener());
        }else{

            Toast.makeText(this, "Please enroll your face before you attempt to authenticate", Toast.LENGTH_LONG).show();
            finish();
        }
    }


}