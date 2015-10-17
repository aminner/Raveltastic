package com.unravel.amanda.unravel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.unravel.amanda.unravel.ravelryapi.models.Pattern;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Amanda on 10/4/2015.
 */
public class PatternListAdapter extends BaseAdapter {
    private final static String TAG = "PatternListAdapter";
    private Pattern[] _patterns;
    private LayoutInflater _inflater;
    private ImageLoader _imageLoader;
    private ImageLoaderConfiguration _config;
    private DisplayImageOptions _options;
    public ArrayList<RelativeLayout> _selectedViews;
    private Context _context;

    public PatternListAdapter(Context context, Pattern[] patterns)
    {
        _context = context;
        _patterns = patterns;
        _inflater = LayoutInflater.from(context);
        _config = new ImageLoaderConfiguration.Builder(context).build();
        _options  = new DisplayImageOptions.Builder().build();
        ImageLoader.getInstance().init(_config);
        _imageLoader = ImageLoader.getInstance();
        _selectedViews = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return _patterns.length;
    }

    @Override
    public Pattern getItem(int position) {
        Log.d(TAG, "GetItemId - Position: " + position);
        return _patterns[position];
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "GetItemId - Position: " + position);
        return _patterns[position].id;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        try {
            Pattern _currentPattern = _patterns[position];
            if (convertView == null) {
                convertView = _inflater.inflate(R.layout.content_search_result, null);
                convertView.setClickable(true);
                holder = new ViewHolder();
                Log.d(TAG, "Get View : " + _currentPattern.id + " - " + _currentPattern.first_photo.small_url);
                holder.itemDescription = (TextView) convertView.findViewById(R.id.search_result_name);
                holder.itemImage = (ImageView) convertView.findViewById(R.id.search_result_image);
                holder.itemView = (RelativeLayout) convertView.findViewById(R.id.search_result_layout);
                holder.pattern = _currentPattern;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if(_currentPattern.first_photo_bitmap!=null)
                holder.itemImage.setImageBitmap(_currentPattern.first_photo_bitmap);
            else
                getImageBitmap(_currentPattern, holder.itemImage);

            holder.itemDescription.setText(_currentPattern.name);
            convertView.setTag(holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private void getImageBitmap(final Pattern p, final ImageView imageView) {
        ImageSize targetSize = new ImageSize(100, 100);
        _imageLoader.loadImage(p.first_photo.small_url, targetSize, _options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                p.first_photo_bitmap = loadedImage;
                imageView.setImageBitmap(loadedImage);
            }
        });
    }

    public SparseBooleanArray getSelectedIds() {
        return null;
    }

    public void setItems(Pattern[] items) {
        _patterns = items;
    }

    class ViewHolder {
        TextView itemDescription;
        ImageView itemImage;
        RelativeLayout itemView;
        Pattern pattern;
    }

    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Log.d(TAG, "Url: " + url);
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
