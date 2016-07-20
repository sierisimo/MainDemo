package com.icom.draganddrop;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by davidcordova on 07/03/16.
 */
public class MyBean implements Parcelable {

    public static final Creator<MyBean> CREATOR = new Creator<MyBean>() {
        @Override
        public MyBean createFromParcel(Parcel in) {
            return new MyBean(in);
        }

        @Override
        public MyBean[] newArray(int size) {
            return new MyBean[size];
        }
    };
    private String url;
    private String text;

    public MyBean() {
    }

    public MyBean(String url, String text) {
        this.url = url;
        this.text = text;
    }

    protected MyBean(Parcel in) {
        url = in.readString();
        text = in.readString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(text);
    }

    @Override
    public String toString() {
        return "MyBean{" +
                "url='" + url + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
