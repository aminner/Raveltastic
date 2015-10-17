package com.unravel.amanda.unravel.ravelryapi.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.InputStream;
import java.util.Date;

/**
 * Created by Amanda on 8/19/2015.
 */
public class Pattern implements Parcelable {
    public PatternAuthor designer;
    public Photo first_photo;
    public Integer	comments_count;
    public Craft [] craft;
    public String	currency;
    public String current_code;
    public String title;
    public Float	difficulty_average;
    public Integer	difficulty_count;
    public DownloadLink download_location;
    public Boolean	downloadable;
    public Integer	favorites_count;
    public Boolean	free;
    public String	gauge;
    public String	gauge_description;
    public String	gauge_divisor;
    public String	gauge_pattern;
    public Integer	id;
    public String	name;
    public String	notes;
    public String	notes_html;
    public Pack	packs;
    public PatternAttribute []	pattern_attributes;
    public PatternSource [] pattern_sources;
    public PatternAuthor pattern_author;
    public PatternCategory	[] pattern_categories;
    public String pattern_needle_sizes;
    public String pattern_type;
    public Boolean pdf_in_library;
    public String	pdf_url;
    public String	permalink;
    public String personal_attributes;
    public Photo []	photos;
    public String	price;
    public Printing[] printings;
    public String product_id;
    public Integer	projects_count;
    public Date published;
    public Integer	queued_projects_count;
    public Float	rating_average;
    public Integer	rating_count;
    public Boolean	ravelry_download;
    public String	row_gauge;
    public String	sizes_available;
    public String	url;
    public String volumes_in_library;
    public Integer	yardage;
    public String	yardage_description;
    public Integer	yardage_max;
    public YarnWeight [] yarn_weight;
    public String	yarn_weight_description;
    public Bitmap first_photo_bitmap;


    protected Pattern(Parcel in) {
        currency = in.readString();
        current_code = in.readString();
        title = in.readString();
        gauge = in.readString();
        gauge_description = in.readString();
        gauge_divisor = in.readString();
        gauge_pattern = in.readString();
        name = in.readString();
        notes = in.readString();
        notes_html = in.readString();
        pattern_needle_sizes = in.readString();
        pattern_type = in.readString();
        pdf_url = in.readString();
        permalink = in.readString();
        personal_attributes = in.readString();
        price = in.readString();
        product_id = in.readString();
        row_gauge = in.readString();
        sizes_available = in.readString();
        url = in.readString();
        volumes_in_library = in.readString();
        yardage_description = in.readString();
        yarn_weight_description = in.readString();
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
        dest.writeString(currency);
        dest.writeString(current_code);
        dest.writeString(title);
        dest.writeString(gauge);
        dest.writeString(gauge_description);
        dest.writeString(gauge_divisor);
        dest.writeString(gauge_pattern);
        dest.writeString(name);
        dest.writeString(notes);
        dest.writeString(notes_html);
        dest.writeString(pattern_needle_sizes);
        dest.writeString(pattern_type);
        dest.writeString(pdf_url);
        dest.writeString(permalink);
        dest.writeString(personal_attributes);
        dest.writeString(price);
        dest.writeString(product_id);
        dest.writeString(row_gauge);
        dest.writeString(sizes_available);
        dest.writeString(url);
        dest.writeString(volumes_in_library);
        dest.writeString(yardage_description);
        dest.writeString(yarn_weight_description);
        dest.writeParcelable(first_photo_bitmap, flags);
    }
}
