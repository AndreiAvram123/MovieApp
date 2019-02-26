package com.example.movieapp.activities.Model;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * SPECIAL DIVIDER
 * Used in order to set a custom distance between items
 */
public class CustomDivider extends RecyclerView.ItemDecoration {
    private int height;

    public CustomDivider(int height) {
        this.height = height;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = height;
    }
}