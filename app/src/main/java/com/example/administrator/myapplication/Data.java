package com.example.administrator.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/26.
 */

public class Data implements Parcelable{
    private int da_id;
    private String da_username;
    private String da_pwd;
    private String da_nickname;

    protected Data(Parcel in) {
        da_id = in.readInt();
        da_username = in.readString();
        da_pwd = in.readString();
        da_nickname = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

//    public Data(int id, String userName, String password, String nickName) {
//        this.da_id=id;
//        this.da_username=userName;
//        this.da_pwd=password;
//        this.da_nickname=nickName;
//
//    }
    public Data(){

    }

    public int getDa_id() {
        return da_id;
    }

    public void setDa_id(int da_id) {
        this.da_id = da_id;
    }

    public String getDa_username() {
        return da_username;
    }

    public void setDa_username(String da_username) {
        this.da_username = da_username;
    }

    public String getDa_pwd() {
        return da_pwd;
    }

    public void setDa_pwd(String da_pwd) {
        this.da_pwd = da_pwd;
    }

    public String getDa_nickname() {
        return da_nickname;
    }

    public void setDa_nickname(String da_nickname) {
        this.da_nickname = da_nickname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(da_id);
        dest.writeString(da_username);
        dest.writeString(da_pwd);
        dest.writeString(da_nickname);
    }
}
