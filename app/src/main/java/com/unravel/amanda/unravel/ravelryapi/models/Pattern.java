package com.unravel.amanda.unravel.ravelryapi.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amanda on 8/19/2015.
 */
public class Pattern implements Parcelable {
    public boolean free;
    public Integer id;
    public String permalink;
    public String name;
    public Photo first_photo;

    public Bitmap first_photo_bitmap;
    private boolean selected;


    protected Pattern(Parcel in) {

        first_photo_bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Pattern> CREATOR = new Creator<Pattern>() {
        @Override
        public Pattern createFromParcel(Parcel in) {
            return new Pattern(in);
        }

        @Override
        public Pattern[] newArray(int size) {
            return new Pattern[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(first_photo_bitmap, flags);
        selected =false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
}
