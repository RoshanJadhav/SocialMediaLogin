package com.rkhs.c_andorid.facebookintegration.Interfaces;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.plus.model.people.Person;
import com.rkhs.c_andorid.facebookintegration.Pojo.LoginDetails;
import com.rkhs.c_andorid.facebookintegration.Pojo.LocalLoginResult;
import com.rkhs.c_andorid.facebookintegration.Pojo.UserDetails;

import org.json.JSONObject;

/**
 * Created by Admin on 16-01-2018.
 */

public interface ILoginPresenter {
    LocalLoginResult fetchData(String userName, String password);
    boolean putData(LoginDetails loginDetails);
    void loginAction();
    UserDetails collectDetailsFacebook(JSONObject jsonObject);
    UserDetails collectDetailsGoogle(GoogleSignInAccount account,Person person);
    boolean insertt(UserDetails userDetails);
}
