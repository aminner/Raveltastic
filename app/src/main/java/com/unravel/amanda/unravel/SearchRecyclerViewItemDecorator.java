package com.unravel.amanda.unravel;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Amanda on 10/24/2015.
 */
public class SearchRecyclerViewItemDecorator extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;

    public SearchRecyclerViewItemDecorator(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = mVerticalSpaceHeight;
    }
}
