package com.cuit.monitorapp.main.cuit.monitorapp;

import android.widget.TextView;

/**
 * Created by syche on 2018/5/18.
 */

public class FollowViewHolder {
    private TextView name;
    private TextView gender;

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getGender() {
        return gender;
    }

    public void setGender(TextView gender) {
        this.gender = gender;
    }

    public TextView getCreateDate() {
        return createDate;
    }

    public void setCreateDate(TextView createDate) {
        this.createDate = createDate;
    }

    private TextView createDate;
}
