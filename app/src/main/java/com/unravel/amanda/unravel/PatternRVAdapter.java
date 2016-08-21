package com.unravel.amanda.unravel;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.List;

import javax.inject.Inject;

public class PatternRVAdapter extends RecyclerView.Adapter<PatternRVAdapter.PatternViewHolder>{
    private static final String TAG = "PatternRVAdapter";
    private final Activity activity;
    private List<Pattern> patterns;
    private DisplayImageOptions _options;
    private ImageLoaderConfiguration _config;
    private ImageLoader _imageLoader;
    private View.OnClickListener _viewOncClickListener;
    @Inject RavelryApi _api;

    public PatternRVAdapter(List<Pattern> patterns, Activity activity) {
        RavelApplication.ravelApplication.getComponent().inject(this);
        this.patterns = patterns;
        this.activity = activity;
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
        if(_currentPattern.first_photo_bitmap!=null) {
            holder.image.setImageBitmap(_currentPattern.first_photo_bitmap);
        } else if(_currentPattern.first_photo != null){
            _config = new ImageLoaderConfiguration.Builder(holder.cardView.getContext()).build();
            _options  = new DisplayImageOptions.Builder().build();
            ImageLoader.getInstance().init(_config);
            _imageLoader = ImageLoader.getInstance();
            getImageBitmap(_currentPattern, holder.image, _currentPattern.first_photo.square_url);
        }
        holder.itemView.setOnClickListener(v -> {
            _api.processRequest(new RavelryApiRequest(new String[]{_currentPattern.id.toString()}, RavelryApiCalls.GET_PATTERN), new HttpCallback() {
                @Override
                public void onSuccess(RavelApiResponse jsonString) {
                     final PatternFull pattern = (PatternFull)jsonString.responses.get(0);
                        try {
                            ((Activity)v.getContext()).runOnUiThread(() -> {
                                View view = ((Activity) v.getContext()).getLayoutInflater().inflate(R.layout.pattern_detail_layout, null);

                                final Dialog settingsDialog = new Dialog(v.getContext());
                                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                ImageView imgView = (ImageView) view.findViewById(R.id.pattern_image);
                                imgView.setImageBitmap(_currentPattern.first_photo_bitmap);

                                TextView textView = (TextView) view.findViewById(R.id.pattern_title);
                                textView.setText(pattern.name);

                                if (pattern.designer != null) {
                                    TextView tv_author = (TextView) view.findViewById(R.id.pattern_author);
                                    tv_author.setText(pattern.designer.name);
                                }
                                else if(pattern.pattern_author!=null)
                                {
                                    TextView tv_author = (TextView) view.findViewById(R.id.pattern_author);
                                    tv_author.setText(pattern.pattern_author.name);
                                }

                        TextView tv_favorites = (TextView) view.findViewById(R.id.favorite_count);
                        tv_favorites.setText("Favorites: " + pattern.favorites_count);

                        TextView tv_projects = (TextView) view.findViewById(R.id.project_count);
                        tv_projects.setText("Projects: " + pattern.projects_count);

                        settingsDialog.setContentView(view);
                        settingsDialog.show();
                            });
                    } catch(Exception ex) {
                        Log.d(TAG, ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Throwable exception) {
                    Log.d(TAG, "Exception: " + exception.getMessage());
                }
            });
//            _currentPattern.setSelected(!_currentPattern.isSelected());
//            if(_currentPattern.isSelected()&&!_selectedPatterns.contains(_currentPattern))
//                _selectedPatterns.add(_currentPattern);
//            else if(!_currentPattern.isSelected()&&_selectedPatterns.contains(_currentPattern))
//                _selectedPatterns.remove(_currentPattern);
//            notifyItemChanged(position);
        });
        holder.image.setOnClickListener(v -> {
            //TODO: Speed this up load the full image when loading the thumbmail on another thread
            View view =activity.getLayoutInflater().inflate(R.layout.image_expanded_layout, null);
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
//            patternType = (TextView) itemView.findViewById(R.id.search_result_pattern_type);
            patternCost = (TextView) itemView.findViewById(R.id.search_result_pattern_cost);
        }
    }
}
