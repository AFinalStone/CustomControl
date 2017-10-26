package com.example.model01_canvas_example;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/10/26.
 */

public class SectorView extends View {

    public SectorView(Context context) {
        this(context, null);
    }

    public SectorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
