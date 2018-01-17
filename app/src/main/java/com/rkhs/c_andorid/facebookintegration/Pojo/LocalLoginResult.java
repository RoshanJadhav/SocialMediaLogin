package com.rkhs.c_andorid.facebookintegration.Pojo;

/**
 * Created by Admin on 16-01-2018.
 */

public class LocalLoginResult {

    int feedback = 0;
    boolean valid = false;

    public LocalLoginResult() {

    }

    public int getFeedback() {
        return feedback;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    public LocalLoginResult(int feedback, boolean valid) {
        this.feedback = feedback;
        this.valid = valid;
    }


    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
