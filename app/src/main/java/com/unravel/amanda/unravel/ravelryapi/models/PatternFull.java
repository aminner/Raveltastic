package com.unravel.amanda.unravel.ravelryapi.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amanda on 8/19/2015.
 */
public class PatternFull implements Parcelable {
    public PatternAuthor designer;
    public Integer	comments_count;//CHECK
    public Craft craft;//CHECK
    public String	currency;//CHECK
    public String currency_symbol;//CHECK
    public Float	difficulty_average;//CHECK
    public Integer	difficulty_count;//CHECK

    public DownloadLink download_location;//CHECK
    public Boolean	downloadable;//CHECK
    public Integer	favorites_count;//CHECK

    public Boolean	free;//CHECK
    public Float	gauge;//CHECK
    public String	gauge_description;//CHECK
    public Integer	gauge_divisor;//CHECK
    public String	gauge_pattern; //CHECK
    public Integer	id;//CHECK

    public String	name; //CHECK

    public String	notes;//CHECK
    public String	notes_html;//CHECK
    public PackFull[]	packs;//CHECK
    public PatternAttribute []	pattern_attributes;//CHECK

    public PatternAuthor pattern_author;//CHECK

    public PatternCategory	[] pattern_categories;//CHECK
    public NeedleSizes [] pattern_needle_sizes;//CHECK
    public PatternType pattern_type;//CHECK
    public Boolean pdf_in_library;//CHECK
    public String	pdf_url;//CHECK

    public String	permalink; //CHECK
    public PersonalAttributes personal_attributes;//CHECK
    public Photo []	photos;//CHECK
    public String	price;//CHECK
    public Printing[] printings;//CHECK
    public Integer product_id; // CHECK

    public Integer	projects_count;//CHECK
//    public String published;//CHECK
//    public Integer	queued_projects_count; //CHECK
//    public Float	rating_average;
//    public Integer	rating_count;//CHECK
//    public Boolean	ravelry_download;//CHECK
//    public Float	row_gauge; // CHECK
//    public String	sizes_available;//CHECK
//    public String	url;//CHECK
//    public String [] volumes_in_library;//CHECK
//    public Integer	yardage;//CHECK
//    public String	yardage_description;//CHECK
//    public Integer	yardage_max; //CHECK
//    public YarnWeight yarn_weight;//CHECK
//    public String	yarn_weight_description;//CHECK
    public Bitmap first_photo_bitmap;
    private boolean selected;

    protected PatternFull(Parcel in) {
        currency = in.readString();
//        current_code = in.readString();
//        title = in.readString();
//        gauge = Float.parseFloat(in.readString());
//        gauge_description = in.readString();
//        gauge_divisor = in.readString();
//        gauge_pattern = in.readString();
//        name = in.readString();
//        notes = in.readString();
//        notes_html = in.readString();
//        pattern_needle_sizes = in.readString();
//        pattern_type = in.readString();
//        pdf_url = in.readString();
//        permalink = in.readString();
//        personal_attributes = in.readString();
//        price = in.readString();
//        product_id = in.readString();
//        row_gauge = in.readString();
//        sizes_available = in.readString();
//        url = in.readString();
//        volumes_in_library = in.readString();
//        yardage_description = in.readString();
//        yarn_weight_description = in.readString();
//        first_photo_bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<PatternFull> CREATOR = new Creator<PatternFull>() {
        @Override
        public PatternFull createFromParcel(Parcel in) {
            return new PatternFull(in);
        }

        @Override
        public PatternFull[] newArray(int size) {
            return new PatternFull[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currency);
//        dest.writeString(current_code);
//        dest.writeString(title);
//        dest.writeString(gauge.toString());
//        dest.writeString(gauge_description);
//        dest.writeString(gauge_divisor);
//        dest.writeString(gauge_pattern);
//        dest.writeString(name);
//        dest.writeString(notes);
//        dest.writeString(notes_html);
//        dest.writeString(pattern_needle_sizes);
//        dest.writeString(pattern_type);
//        dest.writeString(pdf_url);
//        dest.writeString(permalink);
//        dest.writeString(personal_attributes);
//        dest.writeString(price);
//        dest.writeString(product_id);
//        dest.writeString(row_gauge);
//        dest.writeString(sizes_available);
//        dest.writeString(url);
//        dest.writeString(volumes_in_library);
//        dest.writeString(yardage_description);
//        dest.writeString(yarn_weight_description);
//        dest.writeParcelable(first_photo_bitmap, flags);
//        selected =false;
    }

//    public boolean isSelected() {
//        return selected;
//    }

//    public void setSelected(boolean selected)
//    {
//        this.selected = selected;
//    }
}
