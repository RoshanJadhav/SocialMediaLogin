package com.rkhs.c_andorid.facebookintegration.Presenter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.rkhs.c_andorid.facebookintegration.Activities.MainScreenActivity;
import com.rkhs.c_andorid.facebookintegration.Helper.DataShelf;
import com.rkhs.c_andorid.facebookintegration.Interfaces.ILoginPresenter;
import com.rkhs.c_andorid.facebookintegration.Pojo.LoginDetails;
import com.rkhs.c_andorid.facebookintegration.Pojo.LocalLoginResult;
import com.rkhs.c_andorid.facebookintegration.Pojo.UserDetails;
import com.rkhs.c_andorid.facebookintegration.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Admin on 16-01-2018.
 */

public class LoginPresenter implements ILoginPresenter {

    DataShelf dataShelf;
    Context context;

    public LoginPresenter(Context context,DataShelf dataShelf) {
        this.context = context;
        this.dataShelf = dataShelf;
    }

    @Override
    public boolean insertt(UserDetails userDetails) {

        if (userDetails != null) {
            if (dataShelf.insertIntoLoginDetails(userDetails)) {Toast.makeText(context,"inserted",Toast.LENGTH_LONG).show();}
            else {Toast.makeText(context,"not inserted",Toast.LENGTH_LONG).show();}
        } else {Toast.makeText(context,"info sent is null",Toast.LENGTH_LONG).show();}

        return false;
    }

    @Override
    public UserDetails collectDetailsFacebook(JSONObject object) {

        UserDetails userDetails = new UserDetails();
        String f_name = "",l_name = "",email = "",gender = "";
        try {
            String id = object.getString("id");
            if (object.has("first_name"))
                f_name = object.getString("first_name");
            if (object.has("last_name"))
                l_name = object.getString("last_name");
            if (object.has("email"))
                email = object.getString("email");
            if (object.has("gender"))
                gender = object.getString("gender");


            userDetails.setkId(id);
            userDetails.setkFname(f_name);
            userDetails.setkLname(l_name);
            userDetails.setkEmailId(email);
            userDetails.setkGender(gender);

            Log.i("karo",f_name);
            Log.i("karo",l_name);
            Log.i("karo",email);
            Log.i("karo",gender);

        } catch (JSONException e) {
            Log.d("karo", "BUNDLE Exception : "+e.toString());
        }

        return userDetails;
    }

    @Override
    public UserDetails collectDetailsGoogle(GoogleSignInAccount account,Person person) {

        UserDetails userDetails = new UserDetails();

        //getting first name...
        userDetails.setkFname(account.getGivenName());

        //getting last name...
        userDetails.setkLname(account.getFamilyName());

        //get email id
        userDetails.setkEmailId(account.getEmail());

        //getting login id...
        userDetails.setkId(account.getId());

        //getting user details...
        int genderRes = person.getGender();
        if (genderRes == 0)
            userDetails.setkGender("male");
        else
            userDetails.setkGender("female");

        //showing the info collected from the user...
        Log.i("karo",userDetails.getkFname());
        Log.i("karo",userDetails.getkLname());
        Log.i("karo",userDetails.getkEmailId());
        Log.i("karo",userDetails.getkGender());

        return userDetails;
    }

    @Override
    public LocalLoginResult fetchData(String userName, String password) {


        return null;
//        boolean found = false;
//        LocalLoginResult localLoginResult = new LocalLoginResult();
//        Cursor c = dataShelf.selectFromLogin(userName);
//        if (c.getCount() > 0) {
//            int k = 0;
//            c.moveToFirst();
//            while(c.moveToNext()) {
//                if (c.getString(2).equals(password)) {
//                    localLoginResult.setValid(true);
//                    localLoginResult.setFeedback(Constants.kValidUser);
//                    return localLoginResult;
//                }
//                k++;
//            }
//            if (!found) {
//                //this block is tell that user has entered
//                //correct user name but the password is wrong!
//                localLoginResult.setValid(true);
//                localLoginResult.setFeedback(Constants.kInValidUser);
//            }
//        }
//
//        //the user has not registred yet...
//        localLoginResult.setValid(false);
//        localLoginResult.setFeedback(Constants.kUnregisteredUser);
//        return localLoginResult;
    }

    @Override
    public boolean putData(LoginDetails loginDetails) {
        //if (dataShelf.insertIntoLogin(loginDetails)) {return true;}
        return false;
    }

    @Override
    public void loginAction() {context.startActivity(new Intent(getApplicationContext(),MainScreenActivity.class).putExtra("login_from", Constants.kLoginFromApp));}
}
