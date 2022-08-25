package com.example.smartindiahackathon.ui.epubReader.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class MyRecycler extends RecyclerView {

    public MyRecycler(@NonNull Context context) {
        super(context);
    }

    public MyRecycler(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecycler(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean verticalScrollingEnabled = false;

    public void enableVerticalScroll(boolean enabled) {
        verticalScrollingEnabled = enabled;
    }

    public boolean isVerticalScrollingEnabled() {
        return verticalScrollingEnabled;
    }

    @Override
    public int computeVerticalScrollRange() {
        if (isVerticalScrollingEnabled())
            return super.computeVerticalScrollRange();
        return 0;
    }

}
