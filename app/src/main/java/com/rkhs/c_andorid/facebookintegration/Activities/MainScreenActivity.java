package com.rkhs.c_andorid.facebookintegration.Activities;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import com.rkhs.c_andorid.facebookintegration.Adapters.CustomPagerAdapter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.rkhs.c_andorid.facebookintegration.Pojo.LoginDetails;
import com.rkhs.c_andorid.facebookintegration.R;

public class MainScreenActivity extends FragmentActivity {

    boolean logoutFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        int key = getIntent().getIntExtra("login_from",0);
        setLogoutFlag(key);

        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_mainScreen);
        CustomPagerAdapter adapter = new CustomPagerAdapter(fragmentManager,this);
        viewPager.setAdapter(adapter);
    }

    private void setLogoutFlag(int key) {
        switch(key) {
            case 4:
                logoutFlag = false;
                break;
            case 5:
                logoutFlag = false;
                break;
            case 6:
                logoutFlag = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (logoutFlag)
            showLogoutDialog();
        else
            super.onBackPressed();
    }

    private void showLogoutDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher_round)
                .setTitle("Logout !")
                .setMessage("Do you want Logout ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //cancel the dialog box...
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}
