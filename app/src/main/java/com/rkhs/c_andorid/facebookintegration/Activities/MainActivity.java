package com.rkhs.c_andorid.facebookintegration.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.rkhs.c_andorid.facebookintegration.Helper.DataShelf;
import com.rkhs.c_andorid.facebookintegration.Interfaces.ILoginView;
import com.rkhs.c_andorid.facebookintegration.Pojo.LocalLoginResult;
import com.rkhs.c_andorid.facebookintegration.Pojo.LoginDetails;
import com.rkhs.c_andorid.facebookintegration.Pojo.UserDetails;
import com.rkhs.c_andorid.facebookintegration.Presenter.LoginPresenter;
import com.rkhs.c_andorid.facebookintegration.R;
import com.rkhs.c_andorid.facebookintegration.utils.Constants;
import com.rkhs.c_andorid.facebookintegration.utils.PrefUtil;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ILoginView, GoogleApiClient.OnConnectionFailedListener {

    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private Button localLoginButton;
    private EditText userName;
    private EditText password;

    private DataShelf dataShelf;
    private LoginPresenter loginPresenter;

    Activity activity;
    AccessToken accessToken = null;
    SharedPreferences sharedPreferences;

    private static final int REQ_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (googleServicesAvailable()) {

            setContentView(R.layout.activity_main);
            f_init();

            //setting the permission for getting the email from the login information...
            loginButton.setReadPermissions("email");
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    accessToken = loginResult.getAccessToken();
                    String kToken = loginResult.getAccessToken().getToken();

                    sharedPreferences = getSharedPreferences("newPrefs",MODE_PRIVATE);
                    sharedPreferences.edit().putString("token",kToken).commit();

                    new PrefUtil(activity).saveAccessToken(kToken);

                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {

                                @Override
                                public void onCompleted(JSONObject jsonObject,
                                                        GraphResponse response) {
                                    // Getting FB User Data
                                    UserDetails userDetails = loginPresenter.collectDetailsFacebook(jsonObject);


                                    //checking if the user is already saved in database...
                                    Cursor c = dataShelf.selectFromLoginDetails(userDetails.getkEmailId());
                                    if (c.getCount() > 0) {
                                        c.moveToFirst();
                                        Log.i("karo","name : "+c.getString(1));
                                    }
                                    else {
                                        boolean res = dataShelf.insertIntoLoginDetails(userDetails);
                                        Log.i("karo",""+res);
                                    }


//                                if (!new PrefUtil(activity).getLoginDetailsEmailPrefs(Constants.kLoginFromFacebook).equals(userDetails.getkEmailId())) {
//                                    //if the user is loggin in for the first time then the info will be inserted in the databse table
//                                    new PrefUtil(activity).setLoginDetailsEmailPrefs(userDetails.getkEmailId(),Constants.kLoginFromFacebook);
//                                    loginPresenter.insertt(userDetails);
//                                }
//
//                                Cursor c = dataShelf.selectFromLoginDetails(userDetails.getkEmailId());
//                                if (c.getCount() > 0) {
//                                    Toast.makeText(getApplicationContext(),"some data found!",Toast.LENGTH_LONG).show();
//                                }
//                                else {
//                                    Toast.makeText(getApplicationContext(),"no data",Toast.LENGTH_LONG).show();
//                                }


                                    startActivity(new Intent(getApplicationContext(),MainScreenActivity.class).putExtra("login_from", Constants.kLoginFromFacebook));
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,first_name,last_name,email,gender");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {Toast.makeText(getApplicationContext(),"Login Canceled",Toast.LENGTH_SHORT).show();}

                @Override
                public void onError(FacebookException error) {

                    deleteAccessToken();
                    Toast.makeText(getApplicationContext(),"Error In Logging In",Toast.LENGTH_SHORT).show();}
            });
        }
        else {
            Toast.makeText(this,"Services Are Not Available App not working.",Toast.LENGTH_SHORT).show();
            finish();
        }

        // if already logged in...



        String currentToken = "";
        sharedPreferences = getSharedPreferences("newPrefs",MODE_PRIVATE);
        currentToken = sharedPreferences.getString("token","NA");

        try {

            if (AccessToken.getCurrentAccessToken().getToken().equals(currentToken)) {
                Log.i("karo","already logged in");
                startActivity(new Intent(getApplicationContext(),MainScreenActivity.class).putExtra("login_from", Constants.kLoginFromFacebook));
            }
        }catch (NullPointerException e) {
        }

    }

    private boolean googleServicesAvailable() {

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            Toast.makeText(this,"Services Are Available",Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (api.isUserResolvableError(isAvailable)) {

            Dialog dialog = api.getErrorDialog(this,isAvailable,0);
            dialog.show();
        }
        else {Toast.makeText(this,"Services Are not Available",Toast.LENGTH_SHORT).show();}

        return false;
    }

    private void deleteAccessToken() {

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                    //User logged out
                    new PrefUtil(activity).clearToken();
                    LoginManager.getInstance().logOut();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //for google login call...
        if (requestCode == REQ_CODE) {

            //google sign in result...
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
        else {
            //facebook signin result...
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_local_login) {
            /*if (validateTheInputs()) {
                LocalLoginResult localLoginResult = loginPresenter.fetchData(userName.getText().toString(),password.getText().toString());
                if (localLoginResult.isValid()) {
                    loginPresenter.loginAction();
                    refreshScreen();
                }
                else {
                    //show a dialog box showing do you want to create new login...
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.ic_launcher_round)
                            .setTitle("Register !")
                            .setMessage("Do you want to register as a new user ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //register the user in the database...

                                    String[] userDetls = {userName.getText().toString(),password.getText().toString()};
                                    LoginDetails loginDetails = new LoginDetails(userDetls);
                                    if (loginPresenter.putData(loginDetails)) {
                                        Toast.makeText(getApplicationContext(),"Registered Successfully !",Toast.LENGTH_SHORT).show();
                                        loginPresenter.loginAction();
                                        refreshScreen();
                                    } else {Toast.makeText(getApplicationContext(),"Registration Failed !",Toast.LENGTH_SHORT).show();}
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //cancel the dialog box...
                                    dialogInterface.dismiss();
                                    refreshScreen();
                                }
                            })
                            .show();
                }
            }*/
        } else if (view.getId() == R.id.btn_gmail_login) {signIn();}
    }

    //validating the inputs entered by the user...
    public boolean validateTheInputs() {
        if (!userName.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            if (password.getText().toString().length() >= 6)
                return true;
            Toast.makeText(this,"Password Should be atleast 6 characters long.",Toast.LENGTH_LONG).show();
            return false;
        }
        else {Toast.makeText(this,"Please Fill Up The Credentials Properly.",Toast.LENGTH_LONG).show();}
        return false;
    }

    private void f_init() {

        activity = this;
        dataShelf = new DataShelf(this);
        loginPresenter = new LoginPresenter(this,dataShelf);
        localLoginButton = (Button) findViewById(R.id.btn_local_login);
        localLoginButton.setOnClickListener(this);

        signInButton = (SignInButton) findViewById(R.id.btn_gmail_login);
        signInButton.setOnClickListener(this);

        loginButton = (LoginButton) findViewById(R.id.btn_login);
        callbackManager = CallbackManager.Factory.create();
        userName = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
                .addApi(Plus.API).build();

    }

    @Override
    public void refreshScreen() {
        userName.setText("");
        password.setText("");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Cannot Log in.",Toast.LENGTH_LONG).show();
    }

    //google sign in related functions...
    private void signIn() {
        //signing in from google...
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    private void signOut() {


    }

    //google sign in related functions...

    /**
     * done colleting the google sign in info...
     * @param signInResult
     */
    private void handleResult(GoogleSignInResult signInResult) {

        if (signInResult.isSuccess()) {

            //getting info of the user on google sign in...
            Person person = Plus.PeopleApi.getCurrentPerson(googleApiClient);
            GoogleSignInAccount account = signInResult.getSignInAccount();
            UserDetails userDetails = loginPresenter.collectDetailsGoogle(account,person);


            //setting the shared prefs...
            if (!new PrefUtil(activity).getLoginDetailsEmailPrefs(Constants.kLoginFromGoogle).equals(userDetails.getkEmailId())) {
                //if the user is loggin in for the first time then the info will be inserted in the databse table
                new PrefUtil(activity).setLoginDetailsEmailPrefs(userDetails.getkEmailId(),Constants.kLoginFromGoogle);
                loginPresenter.insertt(userDetails);
            }


            //Cursor c = dataShelf.selectFromLoginDetails(userDetails.getkEmailId());
            //Log.i("karo",""+c.getCount());
//            if (c.getCount() > 0) {
//                Log.i("karo","has data");
//            }
//            else {
//                Log.i("karo","data not found");
//            }





            //starting activity on successful login from google...
            startActivity(new Intent(getApplicationContext(),MainScreenActivity.class).putExtra("login_from", Constants.kLoginFromGoogle));
            refreshScreen();
        }
    }
}
