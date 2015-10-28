package com.unravel.amanda.unravel;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.unravel.amanda.unravel.ravelryapi.HttpCallback;
import com.unravel.amanda.unravel.ravelryapi.RavelryApi;
import com.unravel.amanda.unravel.ravelryapi.RavelryApiCalls;
import com.unravel.amanda.unravel.ravelryapi.RavelryApiRequest;
import com.unravel.amanda.unravel.ravelryapi.models.Pattern;
import com.unravel.amanda.unravel.ravelryapi.models.PatternFull;
import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amanda on 10/19/2015.
 */
public class PatternRVAdapter extends RecyclerView.Adapter<PatternRVAdapter.PatternViewHolder>{
    private List<Pattern> patterns;
    private DisplayImageOptions _options;
    private ImageLoaderConfiguration _config;
    private ImageLoader _imageLoader;
    private List<Pattern> _selectedPatterns;
    private View.OnClickListener _viewOncClickListener;

    public PatternRVAdapter(List<Pattern> patterns) {
        this.patterns = patterns;
        _selectedPatterns =new ArrayList<Pattern>();
    }

    public void setOnClickListener(View.OnClickListener onClickListener)
    {
        _viewOncClickListener = onClickListener;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PatternViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_search_result, parent, false);
        final PatternViewHolder pvh = new PatternViewHolder(view);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PatternViewHolder holder, final int position) {
        final Pattern _currentPattern = patterns.get(position);
        holder.title.setText(_currentPattern.name);
        holder.patternCost.setText(_currentPattern.free?"FREE":"PAID");
        //Load Iamge
        if(_currentPattern.first_photo_bitmap!=null)
            holder.image.setImageBitmap(_currentPattern.first_photo_bitmap);
        else if(_currentPattern.first_photo != null){
            _config = new ImageLoaderConfiguration.Builder(holder.cardView.getContext()).build();
            _options  = new DisplayImageOptions.Builder().build();
            ImageLoader.getInstance().init(_config);
            _imageLoader = ImageLoader.getInstance();
            getImageBitmap(_currentPattern, holder.image, _currentPattern.first_photo.square_url);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RavelryApi.processRequest(new RavelryApiRequest("", RavelryApiCalls.GET_PATTERN.replace("$", _currentPattern.id.toString())), new HttpCallback() {
                    @Override
                    public void onSuccess(RavelApiResponse jsonString) {
                        PatternFull pattern = (PatternFull)jsonString.responses.get(0);
                    }

                    @Override
                    public void onFailure(Exception exception) {

                    }
                });
//            _currentPattern.setSelected(!_currentPattern.isSelected());
//            if(_currentPattern.isSelected()&&!_selectedPatterns.contains(_currentPattern))
//                _selectedPatterns.add(_currentPattern);
//            else if(!_currentPattern.isSelected()&&_selectedPatterns.contains(_currentPattern))
//                _selectedPatterns.remove(_currentPattern);
//            notifyItemChanged(position);
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Speed this up load the full image when loading the thumbmail on another thread
                View view = ((Activity)v.getContext()).getLayoutInflater().inflate(R.layout.image_expanded_layout, null);
                final Dialog settingsDialog = new Dialog(v.getContext());
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                ImageView imgView = (ImageView)view.findViewById(R.id.fullsize_image_view);
                String rawUrl =  _currentPattern.first_photo.medium_url.replace("_medium", "");
                getImageBitmap(_currentPattern, imgView, rawUrl);

//                Button btn = (Button)view.findViewById(R.id.fullsize_close_image_view);
//                btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        settingsDialog.dismiss();
//                    }
//                });
                settingsDialog.setContentView(view);
                settingsDialog.show();
            }
        });
        if(_currentPattern.isSelected())
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        else
            holder.itemView.setBackgroundColor(Color.WHITE);

    }
    private void getImageBitmap(final Pattern p, final ImageView imageView, final String photoUrl) {
        _imageLoader.loadImage(photoUrl, _options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(photoUrl.contains("_square"))
                    p.first_photo_bitmap = loadedImage;
                imageView.setImageBitmap(loadedImage);
            }
        });
    }
    @Override
    public int getItemCount() {
        return patterns.size();
    }

    public void setItems(List<Pattern> items) {
        this.patterns = items;
    }

    public static class PatternViewHolder extends RecyclerView.ViewHolder
    {
        View itemView;
        CardView cardView;
        TextView title;
        TextView patternType;
        TextView patternCost;
        ImageView image;

        public PatternViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemView.setClickable(true);
            cardView = (CardView)itemView.findViewById(R.id.search_card_view);
            title = (TextView) itemView.findViewById(R.id.search_result_name);
            image = (ImageView) itemView.findViewById(R.id.search_result_image);
            patternType = (TextView) itemView.findViewById(R.id.search_result_pattern_type);
            patternCost = (TextView) itemView.findViewById(R.id.search_result_pattern_cost);
        }
    }
}
