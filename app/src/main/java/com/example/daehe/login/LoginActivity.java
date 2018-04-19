package com.example.daehe.login;

import android.app.ProgressDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    private int RC_SIGN_IN = 0;
    private User user = null;
    public static GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private Button disconnectButton;
    private LinearLayout signOutView;
    private ProgressDialog mProgressDialog;
    GoogleSignInAccount acct;
    private ImageView pe_logo;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private LoginButton btnFBLogin;
    private CallbackManager cbmFacebook;
    private static String email;
    private static boolean logout = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mGoogleApiClient.connect();

        btnFBLogin = (LoginButton) findViewById(R.id.facebook_sign_in_button);
        signInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        cbmFacebook = CallbackManager.Factory.create();

        btnFBLogin.registerCallback(cbmFacebook, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    email = object.optString("email");
                                }
                            }
                        }).executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        });
        setGooglePlusButtonText(signInButton, "Sign in with Google");
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        /*
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
        */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        cbmFacebook.onActivityResult(requestCode, resultCode, data);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            acct = result.getSignInAccount();
            //Similarly you can get the email and photourl using acct.getEmail() and  acct.getPhotoUrl()

            proceed(true);
        } else {
            // Signed out, show unauthenticated UI.
            proceed(false);
        }
    }

    private void proceed(boolean signedIn) {
        if (signedIn || isLoggedInFB()) {
            if(isGooglePlayServicesInstalled()) {
                Intent mapIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mapIntent);
            }
        } else {
            Toast.makeText(this, "Sign in Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }

    }

    public boolean isGooglePlayServicesInstalled() {
        Log.d(TAG, "isGooglePlayServicesInstalled: Checking Google Play services version");

        int available = GoogleApiAvailability
                .getInstance().isGooglePlayServicesAvailable(LoginActivity.this);
        if(available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isGooglePlayServicesInstalled: Google Play services are working");
            return true;
        } else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isGooglePlayServicesInstalled(): Fixable error occurred");
            Dialog dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(LoginActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Google Play services not installed",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * Checks login status of facebook accounts
     * @return true if logged in false if null
     */
    public static boolean isLoggedInFB()
    {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public static String GetFacebookID()
    {
        return Profile.getCurrentProfile().getId();
    }

    public static String GetDisplayName()
    {
        return Profile.getCurrentProfile().getName();
    }

    public static String GetFacebookEmail()
    {
        return email;
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    public static void LogOut(){
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected())
        {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            mGoogleApiClient.disconnect();
                        }
                    });
        }
    }
}

